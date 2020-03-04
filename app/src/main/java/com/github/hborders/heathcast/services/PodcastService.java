package com.github.hborders.heathcast.services;

import android.content.Context;

import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.core.Tuple;
import com.github.hborders.heathcast.dao.Database;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.PodcastSearchIdentified;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;
import com.google.gson.Gson;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class PodcastService {
    private final Database<Object> database;
    private final Scheduler scheduler;
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter;

    private final ConcurrentHashMap<PodcastSearch, BehaviorSubject<ServiceRequestState>> serviceRequestStateBehaviorSubjectsByPodcastSearch =
            new ConcurrentHashMap<>();

    public PodcastService(Context context) {
        this(
                context,
                Schedulers.io()
        );
    }

    private PodcastService(
            Context context,
            Scheduler scheduler
    ) {
        this(
                new Database<>(
                        context,
                        null,
                        scheduler
                ),
                scheduler
        );
    }

    public PodcastService(
            Database<Object> database,
            Scheduler scheduler
    ) {
        this(
                database,
                scheduler,
                new OkHttpClient()
        );
    }

    public PodcastService(
            Database<Object> database,
            Scheduler scheduler,
            OkHttpClient okHttpClient
    ) {
        this(
                database,
                scheduler,
                okHttpClient,
                new Gson(),
                ReactivexOkHttpCallAdapter.createWithScheduler(scheduler)
        );
    }

    public PodcastService(
            Database<Object> database,
            Scheduler scheduler,
            OkHttpClient okHttpClient,
            Gson gson,
            ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter
    ) {
        this.database = database;
        this.scheduler = scheduler;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.reactivexOkHttpCallAdapter = reactivexOkHttpCallAdapter;
    }

    public Observable<List<PodcastSearchIdentified>> observeQueryForAllPodcastSearchIdentifieds() {
        return database.observeQueryForAllPodcastSearchIdentifieds();
    }

    public Observable<Optional<PodcastIdentified>> observeQueryForPodcastIdentified(
            PodcastIdentifier podcastIdentifier
    ) {
        return database.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    public Observable<Optional<SubscriptionIdentifier>> observeQueryForSubscriptionIdentifier(
            PodcastIdentifier podcastIdentifier
    ) {
        return database.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    public Single<Optional<SubscriptionIdentifier>> subscribe(PodcastIdentifier podcastIdentifier) {
        return Single.<Optional<SubscriptionIdentifier>>create(source ->
                source.onSuccess(
                        database.subscribe(podcastIdentifier)
                )
        ).subscribeOn(scheduler);
    }

    public Single<Result> unsubscribe(SubscriptionIdentifier subscriptionIdentifier) {
        return Single.<Result>create(source ->
                source.onSuccess(
                        database.unsubscribe(subscriptionIdentifier)
                )
        ).subscribeOn(scheduler);
    }

    public Observable<ServiceResponse1<PodcastIdentifiedList>> searchForPodcasts2(
            PodcastSearch podcastSearch
    ) {
        return searchForPodcasts2(
                null,
                podcastSearch
        );
    }

    public Observable<ServiceResponse1<PodcastIdentifiedList>> searchForPodcasts2(
            @Nullable NetworkPauser networkPauser,
            PodcastSearch podcastSearch
    ) {
        final Optional<PodcastSearchIdentified> podcastSearchIdentifiedOptional =
                database.upsertPodcastSearch(podcastSearch);

        return podcastSearchIdentifiedOptional.map(
                podcastSearchIdentified -> {
                    final Object marker = new Object();
                    final Observable<Tuple<Boolean, PodcastIdentifiedList>> sawMarkerAndPodcastIdentifiedListObservable =
                            database
                                    .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified.identifier)
                                    .scan(
                                            new Tuple<>(
                                                    false,
                                                    new PodcastIdentifiedList()
                                            ),
                                            (sawMarkerAndPodcastIdentifiedList, podcastIdentifiedListMarkedValue) -> {
                                                final boolean oldSawMarker = sawMarkerAndPodcastIdentifiedList.first;
                                                final boolean newSawMarker = oldSawMarker || podcastIdentifiedListMarkedValue.markers.contains(marker);
                                                return new Tuple<>(
                                                        newSawMarker,
                                                        podcastIdentifiedListMarkedValue.value
                                                );
                                            }
                                    )
                                    // scan emits the initial accumulator value as the first itme
                                    // and we don't want that
                                    .skip(1);
                    final Observable<ServiceResponse0> podcastSearchServiceResponseObservable =
                            searchForPodcasts(
                                    networkPauser,
                                    podcastSearch.search
                            )
                                    .subscribeOn(scheduler)
                                    .observeOn(scheduler)
                                    .doOnSuccess(podcastSearchResponse ->
                                            database.replacePodcastSearchPodcasts(
                                                    marker,
                                                    podcastSearchIdentified,
                                                    podcastSearchResponse.podcasts
                                            )
                                    )
                                    .map(ignored -> ServiceResponse0.complete())
                                    .doOnError(ignored -> database.markPodcastSearchFailure(marker))
                                    .onErrorReturnItem(ServiceResponse0.failed())
                                    .toObservable().startWith(ServiceResponse0.loading());

                    return Observable.combineLatest(
                            podcastSearchServiceResponseObservable,
                            sawMarkerAndPodcastIdentifiedListObservable,
                            (podcastSearchServiceResponse, sawMarkerAndPodcastIdentifiedList) -> {
                                final boolean sawMarker = sawMarkerAndPodcastIdentifiedList.first;
                                final PodcastIdentifiedList podcastIdentifiedList = sawMarkerAndPodcastIdentifiedList.second;
                                return podcastSearchServiceResponse.reduce(
                                        loading -> {
                                            // we don't care if we saw the marker because we don't know if
                                            // we completed or failed
                                            return ServiceResponse1.loading(
                                                    PodcastIdentifiedList.class,
                                                    podcastIdentifiedList
                                            );
                                        },
                                        complete -> {
                                            if (sawMarker) {
                                                return ServiceResponse1.complete(
                                                        PodcastIdentifiedList.class,
                                                        podcastIdentifiedList
                                                );
                                            } else {
                                                // we haven't seen the marker yet, so
                                                // even though the service finished, the database
                                                // transaction hasn't completed, so stay loading
                                                return ServiceResponse1.loading(
                                                        PodcastIdentifiedList.class,
                                                        podcastIdentifiedList
                                                );
                                            }
                                        },
                                        failed -> {
                                            if (sawMarker) {
                                                return ServiceResponse1.failed(
                                                        PodcastIdentifiedList.class,
                                                        podcastIdentifiedList
                                                );
                                            } else {
                                                // we haven't seen the marker yet, so
                                                // even though the service finished, the database
                                                // transaction hasn't completed, so stay loading
                                                return ServiceResponse1.loading(
                                                        PodcastIdentifiedList.class,
                                                        podcastIdentifiedList
                                                );
                                            }
                                        }
                                );
                            });
                }
        ).orElse(Observable.error(new UpsertPodcastSearchException(podcastSearch)));
    }

    private Single<PodcastSearchResponse> searchForPodcasts(
            @Nullable NetworkPauser networkPauser,
            String query
    ) {
        // https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/
        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("itunes.apple.com")
                .addPathSegments("search")
                // either titleTerm, languageTerm, authorTerm, genreIndex,
                // artistTerm, ratingIndex, keywordsTerm, descriptionTerm
                // .addQueryParameter("attribute", "titleTerm")
                .addQueryParameter("entity", "podcast")
                .addQueryParameter("explicit", "Yes")
                .addQueryParameter("limit", "200") // max is 200
                .addQueryParameter("media", "mPodcast")
                .addQueryParameter("term", query)
                .addQueryParameter("version", "2")
                .build();

        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);
        final Single<Response> responseSingle = reactivexOkHttpCallAdapter
                .single(call);
        final Single<Response> maybePausedResponseSingle;
        if (networkPauser == null) {
            // slow requests to show instance database feedback
            maybePausedResponseSingle = responseSingle.delay(2, TimeUnit.SECONDS);
        } else {
            maybePausedResponseSingle =
                    networkPauser.completable.andThen(responseSingle);
        }

        return maybePausedResponseSingle
                .subscribeOn(Schedulers.io())
                .flatMap(
                        response -> {
                            final int responseCode = response.code();
                            if (responseCode == 200) {
                                @Nullable final ResponseBody responseBody = response.body();
                                if (responseBody == null) {
                                    return Single.error(new Exception("No responseBody"));
                                } else {
                                    final PodcastSearchResultsJson podcastSearchResultsJson;
                                    try {
                                        podcastSearchResultsJson = gson.fromJson(
                                                responseBody.charStream(),
                                                PodcastSearchResultsJson.class
                                        );
                                    } finally {
                                        responseBody.close();
                                        response.close();
                                    }

                                    final List<Podcast> podcasts =
                                            Optional
                                                    .ofNullable(podcastSearchResultsJson.results)
                                                    .filter(Objects::nonNull)
                                                    .map(List::stream)
                                                    .orElseGet(Stream::empty)
                                                    .map(PodcastJson::toPodcast)
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toList());
                                    return Single.just(new PodcastSearchResponse(podcasts));
                                }
                            } else {
                                return Single.error(new Exception("HTTP Error: " + responseCode));
                            }
                        }
                );
    }

//    public Observable<ServiceResponse<PodcastIdentifiedList>> fetchEpisodes2(
//            @Nullable NetworkPauser networkPauser,
//            URL url
//    ) {
//        // need to cache fetched episodes
//        // need to build component for performing network fetches that supports client caching
//        // so I can reuse all the code in searchForPodcasts2
//    }

    public Single<List<EpisodeIdentified>> fetchEpisodes(URL url) {
        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);

        return reactivexOkHttpCallAdapter
                .single(call)
                .subscribeOn(Schedulers.io())
                .flatMap(
                        response -> {
                            try {
                                final int responseCode = response.code();
                                if (responseCode == 200) {
                                    @Nullable final ResponseBody responseBody = response.body();
                                    if (responseBody == null) {
                                        return Single.error(new Exception("No responseBody"));
                                    } else {
                                        try {
                                            final List<EpisodeIdentified> identifiedEpisodes =
                                                    XmlParser.parseEpisodeList(responseBody.byteStream());
                                            return Single.just(identifiedEpisodes);
                                        } finally {
                                            responseBody.close();
                                        }
                                    }
                                } else {
                                    return Single.error(new Exception("HTTP Error: " + responseCode));
                                }
                            } finally {
                                response.close();
                            }
                        }
                );
    }

    private static class PodcastSearchResponse {
        private final List<Podcast> podcasts;

        private PodcastSearchResponse(List<Podcast> podcasts) {
            this.podcasts = podcasts;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PodcastSearchResponse that = (PodcastSearchResponse) o;
            return podcasts.equals(that.podcasts);
        }

        @Override
        public int hashCode() {
            return Objects.hash(podcasts);
        }

        @Override
        public String toString() {
            return "PodcastSearchResponse{" +
                    "podcasts=" + podcasts +
                    '}';
        }
    }
}
