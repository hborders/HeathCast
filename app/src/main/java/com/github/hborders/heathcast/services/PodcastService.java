package com.github.hborders.heathcast.services;

import android.content.Context;

import com.github.hborders.heathcast.core.Either;
import com.github.hborders.heathcast.core.NonnullPair;
import com.github.hborders.heathcast.dao.Database;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;
import com.google.gson.Gson;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class PodcastService {
    @Nullable
    private static PodcastService instance;

    public static synchronized PodcastService getInstance(Context context) {
        @Nullable final PodcastService oldInstance = instance;
        if (oldInstance == null) {
            final PodcastService newInstance = new PodcastService(
                    new Database(
                            context,
                            null,
                            Schedulers.io()
                    ),
                    Schedulers.io()
            );
            instance = newInstance;
            return newInstance;
        } else {
            return oldInstance;
        }
    }

    private final Database database;
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter;

    private final ConcurrentHashMap<PodcastSearch, BehaviorSubject<ServiceRequestState>> serviceRequestStateBehaviorSubjectsByPodcastSearch =
            new ConcurrentHashMap<>();

    public PodcastService(
            Database database,
            Scheduler scheduler) {
        this(
                database,
                new OkHttpClient(),
                new Gson(),
                ReactivexOkHttpCallAdapter.createWithScheduler(scheduler)
        );
    }

    public PodcastService(
            Database database,
            OkHttpClient okHttpClient,
            Gson gson,
            ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter
    ) {
        this.database = database;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.reactivexOkHttpCallAdapter = reactivexOkHttpCallAdapter;
    }

    public Observable<List<Identified<PodcastSearch>>> observeQueryForAllPodcastSearchIdentifieds() {
        return database.observeQueryForAllPodcastSearchIdentifieds();
    }

//    public Observable<Identified<Podcast>> observeQueryForPodcastIdentified(
//            Identifier<Podcast> podcastIdentifier
//    ) {
//        return database.observeQueryForPodcastIdentified(podcastIdentifier);
//    }

    public Observable<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>> searchForPodcasts(
            PodcastSearch podcastSearch
    ) {
        return searchForPodcasts(
                null,
                podcastSearch
        );
    }

    public Observable<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>> searchForPodcasts(
            @Nullable NetworkPauser networkPauser,
            PodcastSearch podcastSearch
    ) {
        final Optional<Identified<PodcastSearch>> podcastSearchIdentifiedOptional =
                database.upsertPodcastSearch(podcastSearch);
        return podcastSearchIdentifiedOptional.map(
                podcastSearchIdentified -> {
                    Observable<List<Identified<Podcast>>> podcastIdentifiedsObservable =
                            database.observeQueryForPodcastIdentifieds(podcastSearchIdentified.identifier);
                    final BehaviorSubject<ServiceRequestState> serviceRequestStateBehaviorSubject =
                            serviceRequestStateBehaviorSubjectsByPodcastSearch.computeIfAbsent(
                                    podcastSearch,
                                    podcastSearch_ -> BehaviorSubject.createDefault(
                                            ServiceRequestState.loading()
                                    )
                            );

                    final Disposable searchForPodcastsDisposable =
                            searchForPodcasts(
                                    networkPauser,
                                    podcastSearch.search
                            )
                                    .map(
                                            podcastSearchResponse ->
                                                    Either.liftLeft(
                                                            PodcastSearchResponse.class,
                                                            ServiceRequestState.class,
                                                            podcastSearchResponse
                                                    )
                                    )
                                    .onErrorReturn(
                                            throwable ->
                                                    Either.right(
                                                            ServiceRequestState.class,
                                                            ServiceRequestState.remoteFailure(throwable)
                                                    )
                                    )
                                    .map(
                                            podcastSearchResponseOrServiceRequestState ->
                                                    podcastSearchResponseOrServiceRequestState.reduce(
                                                            podcastSearchResponse -> {
                                                                database.replacePodcastSearchPodcasts(
                                                                        podcastSearchIdentified,
                                                                        podcastSearchResponse.podcasts
                                                                );
                                                                return ServiceRequestState.loaded();
                                                            },
                                                            Function.identity()
                                                    )
                                    )
                                    .onErrorReturn(ServiceRequestState.LocalFailure::new)
                                    .subscribe(serviceRequestStateBehaviorSubject::onNext);


                    return Observable.combineLatest(
                            podcastIdentifiedsObservable,
                            serviceRequestStateBehaviorSubject,
                            NonnullPair::new
                    ).doOnDispose(searchForPodcastsDisposable::dispose);
                }
        ).orElse(
                Single.<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>>just(
                        new NonnullPair<>(
                                Collections.emptyList(),
                                ServiceRequestState.localFailure(
                                        new UpsertPodcastSearchException(podcastSearch)
                                )
                        )
                ).toObservable()
        );
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

    public Single<List<Identified<Episode>>> fetchEpisodes(URL url) {
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
                                            final List<Identified<Episode>> identifiedEpisodes =
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
        public boolean equals(Object o) {
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
