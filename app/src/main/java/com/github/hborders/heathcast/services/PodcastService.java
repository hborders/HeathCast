package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.core.Tuple;
import com.github.hborders.heathcast.core.URLUtil;
import com.github.hborders.heathcast.dao.Database;
import com.github.hborders.heathcast.dao.Episode2;
import com.github.hborders.heathcast.dao.Identified2;
import com.github.hborders.heathcast.dao.Identifier2;
import com.github.hborders.heathcast.dao.Podcast2;
import com.github.hborders.heathcast.dao.PodcastSearch2;
import com.github.hborders.heathcast.dao.Subscription2;
import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;
import com.google.gson.Gson;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class PodcastService<
        EpisodeType extends Episode2,
        EpisodeIdentifiedType extends Episode2.EpisodeIdentified2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedListType extends Episode2.EpisodeIdentified2.EpisodeIdentifiedList2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedSetType extends Episode2.EpisodeIdentified2.EpisodeIdentifiedSet2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode2.EpisodeIdentifier2,
        EpisodeIdentifierOptType extends Episode2.EpisodeIdentifier2.EpisodeIdentifierOpt2<EpisodeIdentifierType>,
        EpisodeIdentifierOptListType extends Episode2.EpisodeIdentifier2.EpisodeIdentifierOpt2.EpisodeIdentifierOptList2<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType
                >,
        EpisodeListType extends Episode2.EpisodeList2<
                EpisodeType
                >,
        PodcastType extends Podcast2,
        PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedListType extends Podcast2.PodcastIdentified2.PodcastIdentifiedList2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedOptType extends Podcast2.PodcastIdentified2.PodcastIdentifiedOpt2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedSetType extends Podcast2.PodcastIdentified2.PodcastIdentifiedSet2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
        PodcastIdentifierOptType extends Podcast2.PodcastIdentifier2.PodcastIdentifierOpt2<PodcastIdentifierType>,
        PodcastIdentifierOptListType extends Podcast2.PodcastIdentifier2.PodcastIdentifierOpt2.PodcastIdentifierOptList2<
                PodcastIdentifierOptType,
                PodcastIdentifierType
                >,
        PodcastListType extends Podcast2.PodcastList2<PodcastType>,
        PodcastSearchType extends PodcastSearch2,
        PodcastSearchIdentifiedType extends PodcastSearch2.PodcastSearchIdentified2<
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifiedListType extends PodcastSearch2.PodcastSearchIdentified2.PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType,
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifiedOptType extends PodcastSearch2.PodcastSearchIdentified2.PodcastSearchIdentifiedOpt2<
                PodcastSearchIdentifiedType,
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifierType extends PodcastSearch2.PodcastSearchIdentifier2,
        PodcastSearchIdentifierOptType extends PodcastSearch2.PodcastSearchIdentifier2.PodcastSearchIdentifierOpt2<
                PodcastSearchIdentifierType
                >,
        SubscriptionType extends Subscription2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifiedType extends Subscription2.SubscriptionIdentified2<
                SubscriptionIdentifierType,
                SubscriptionType,
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifiedListType extends Subscription2.SubscriptionIdentified2.SubscriptionIdentifiedList2<
                SubscriptionIdentifiedType,
                SubscriptionIdentifierType,
                SubscriptionType,
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifierType extends Subscription2.SubscriptionIdentifier2
        > {
    private static class PodcastSearchResponse<PodcastListType> {
        private final PodcastListType podcasts;

        private PodcastSearchResponse(PodcastListType podcasts) {
            this.podcasts = podcasts;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PodcastSearchResponse<?> that = (PodcastSearchResponse<?>) o;
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

    private final Identified2.IdentifiedFactory2<
            EpisodeIdentifiedType,
            EpisodeIdentifierType,
            EpisodeType
            > episodeIdentifiedFactory;
    private final CollectionFactory.Empty<
            EpisodeIdentifiedListType,
            EpisodeIdentifiedType
            > episodeIdentifiedListEmptyFactory;
    private final Identifier2.IdentifierFactory2<EpisodeIdentifierType> episodeIdentifierFactory;
    private final Podcast2.PodcastFactory2<PodcastType> podcastFactory;
    private final CollectionFactory.Empty<
            PodcastIdentifiedListType,
            PodcastIdentifiedType
            > podcastIdentifiedListEmptyFactory;
    private final CollectionFactory.Empty<
            PodcastListType,
            PodcastType
            > podcastListEmptyFactory;
    private final XmlParser<
            EpisodeListType,
            EpisodeType
            > xmlParser;

    private final Database<
            Object,
            EpisodeType,
            EpisodeIdentifiedType,
            EpisodeIdentifiedListType,
            EpisodeIdentifiedSetType,
            EpisodeIdentifierType,
            EpisodeIdentifierOptType,
            EpisodeIdentifierOptListType,
            EpisodeListType,
            PodcastType,
            PodcastIdentifiedType,
            PodcastIdentifiedListType,
            PodcastIdentifiedSetType,
            PodcastIdentifierType,
            PodcastIdentifierOptType,
            PodcastIdentifierOptListType,
            PodcastSearchType,
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifiedListType,
            PodcastSearchIdentifiedOptType,
            PodcastSearchIdentifierType,
            PodcastSearchIdentifierOptType,
            SubscriptionType,
            SubscriptionIdentifiedType,
            SubscriptionIdentifiedListType,
            SubscriptionIdentifierType
            > database;
    private final Scheduler scheduler;
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter;

    public PodcastService(
            Episode2.EpisodeFactory2<EpisodeType> episodeFactory,
            Identified2.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            CollectionFactory.Empty<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListEmptyFactory,
            Identifier2.IdentifierFactory2<EpisodeIdentifierType> episodeIdentifierFactory,
            CollectionFactory.Capacity<
                    EpisodeListType,
                    EpisodeType
                    > episodeListCapacityFactory,
            CollectionFactory.Empty<
                    EpisodeListType,
                    EpisodeType
                    > episodeListEmptyFactory,
            Podcast2.PodcastFactory2<PodcastType> podcastFactory,
            CollectionFactory.Empty<
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    > podcastIdentifiedListEmptyFactory,
            CollectionFactory.Empty<
                    PodcastListType,
                    PodcastType
                    > podcastListEmptyFactory,
            Database<
                    Object,
                    EpisodeType,
                    EpisodeIdentifiedType,
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifierType,
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierOptListType,
                    EpisodeListType,
                    PodcastType,
                    PodcastIdentifiedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedSetType,
                    PodcastIdentifierType,
                    PodcastIdentifierOptType,
                    PodcastIdentifierOptListType,
                    PodcastSearchType,
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifierType,
                    PodcastSearchIdentifierOptType,
                    SubscriptionType,
                    SubscriptionIdentifiedType,
                    SubscriptionIdentifiedListType,
                    SubscriptionIdentifierType
                    > database,
            Scheduler scheduler // Schedulers.io()
    ) {
        this(
                episodeFactory,
                episodeIdentifiedFactory,
                episodeIdentifiedListEmptyFactory,
                episodeIdentifierFactory,
                episodeListCapacityFactory,
                episodeListEmptyFactory,
                podcastFactory,
                podcastIdentifiedListEmptyFactory,
                podcastListEmptyFactory,
                database,
                scheduler,
                new OkHttpClient()
        );
    }

    public PodcastService(
            Episode2.EpisodeFactory2<EpisodeType> episodeFactory,
            Identified2.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            CollectionFactory.Empty<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListEmptyFactory,
            Identifier2.IdentifierFactory2<EpisodeIdentifierType> episodeIdentifierFactory,
            CollectionFactory.Capacity<
                    EpisodeListType,
                    EpisodeType
                    > episodeListCapacityFactory,
            CollectionFactory.Empty<
                    EpisodeListType,
                    EpisodeType
                    > episodeListEmptyFactory,
            Podcast2.PodcastFactory2<PodcastType> podcastFactory,
            CollectionFactory.Empty<
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    > podcastIdentifiedListEmptyFactory,
            CollectionFactory.Empty<
                    PodcastListType,
                    PodcastType
                    > podcastListEmptyFactory,
            Database<
                    Object,
                    EpisodeType,
                    EpisodeIdentifiedType,
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifierType,
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierOptListType,
                    EpisodeListType,
                    PodcastType,
                    PodcastIdentifiedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedSetType,
                    PodcastIdentifierType,
                    PodcastIdentifierOptType,
                    PodcastIdentifierOptListType,
                    PodcastSearchType,
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifierType,
                    PodcastSearchIdentifierOptType,
                    SubscriptionType,
                    SubscriptionIdentifiedType,
                    SubscriptionIdentifiedListType,
                    SubscriptionIdentifierType
                    > database,
            Scheduler scheduler,
            OkHttpClient okHttpClient
    ) {
        this(
                episodeIdentifiedFactory,
                episodeIdentifiedListEmptyFactory,
                episodeIdentifierFactory,
                podcastFactory,
                podcastIdentifiedListEmptyFactory,
                podcastListEmptyFactory,
                database,
                scheduler,
                okHttpClient,
                new Gson(),
                ReactivexOkHttpCallAdapter.createWithScheduler(scheduler),
                new XmlParser<>(
                        episodeListCapacityFactory,
                        episodeListEmptyFactory,
                        episodeFactory
                )
        );
    }

    public PodcastService(
            Identified2.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            CollectionFactory.Empty<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListEmptyFactory,
            Identifier2.IdentifierFactory2<EpisodeIdentifierType> episodeIdentifierFactory,
            Podcast2.PodcastFactory2<PodcastType> podcastFactory,
            CollectionFactory.Empty<
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    > podcastIdentifiedListEmptyFactory,
            CollectionFactory.Empty<
                    PodcastListType,
                    PodcastType
                    > podcastListEmptyFactory,
            Database<
                    Object,
                    EpisodeType,
                    EpisodeIdentifiedType,
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifierType,
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierOptListType,
                    EpisodeListType,
                    PodcastType,
                    PodcastIdentifiedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedSetType,
                    PodcastIdentifierType,
                    PodcastIdentifierOptType,
                    PodcastIdentifierOptListType,
                    PodcastSearchType,
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifierType,
                    PodcastSearchIdentifierOptType,
                    SubscriptionType,
                    SubscriptionIdentifiedType,
                    SubscriptionIdentifiedListType,
                    SubscriptionIdentifierType
                    > database,
            Scheduler scheduler,
            OkHttpClient okHttpClient,
            Gson gson,
            ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter,
            XmlParser<
                    EpisodeListType,
                    EpisodeType
                    > xmlParser
    ) {
        this.episodeIdentifiedFactory = episodeIdentifiedFactory;
        this.episodeIdentifiedListEmptyFactory = episodeIdentifiedListEmptyFactory;
        this.episodeIdentifierFactory = episodeIdentifierFactory;
        this.podcastFactory = podcastFactory;
        this.podcastIdentifiedListEmptyFactory = podcastIdentifiedListEmptyFactory;
        this.podcastListEmptyFactory = podcastListEmptyFactory;
        this.database = database;
        this.scheduler = scheduler;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.reactivexOkHttpCallAdapter = reactivexOkHttpCallAdapter;
        this.xmlParser = xmlParser;
    }

    public Observable<PodcastSearchIdentifiedListType> observeQueryForAllPodcastSearchIdentifieds() {
        return database.observeQueryForAllPodcastSearchIdentifieds();
    }

    public Observable<Optional<PodcastIdentifiedType>> observeQueryForPodcastIdentified(
            PodcastIdentifierType podcastIdentifier
    ) {
        return database.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    public Observable<Optional<SubscriptionIdentifierType>> observeQueryForSubscriptionIdentifier(
            PodcastIdentifierType podcastIdentifier
    ) {
        return database.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    public Single<Optional<SubscriptionIdentifierType>> subscribe(PodcastIdentifierType podcastIdentifier) {
        return Single.<Optional<SubscriptionIdentifierType>>create(source ->
                source.onSuccess(
                        database.subscribe(podcastIdentifier)
                )
        ).subscribeOn(scheduler);
    }

    public Single<Result> unsubscribe(SubscriptionIdentifierType subscriptionIdentifier) {
        return Single.<Result>create(source ->
                source.onSuccess(
                        database.unsubscribe(subscriptionIdentifier)
                )
        ).subscribeOn(scheduler);
    }

    public <
            PodcastListServiceResponseFactoryLoadingType extends PodcastListServiceResponse.PodcastListServiceResponseFactory<
                    PodcastListServiceResponseType,
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >,
            PodcastListServiceResponseFactoryCompleteType extends PodcastListServiceResponse.PodcastListServiceResponseFactory<
                    PodcastListServiceResponseType,
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >,
            PodcastListServiceResponseFactoryFailedType extends PodcastListServiceResponse.PodcastListServiceResponseFactory<
                    PodcastListServiceResponseType,
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >,
            PodcastListServiceResponseType extends PodcastListServiceResponse<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >,
            PodcastListServiceResponseLoadingType extends PodcastListServiceResponse.PodcastListServiceResponseLoading<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >,
            PodcastListServiceResponseCompleteType extends PodcastListServiceResponse.PodcastListServiceResponseComplete<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >,
            PodcastListServiceResponseFailedType extends PodcastListServiceResponse.PodcastListServiceResponseFailed<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    >
            > Observable<PodcastListServiceResponseType> searchForPodcasts2(
            PodcastListServiceResponseFactoryLoadingType loadingFactory,
            PodcastListServiceResponseFactoryCompleteType completeFactory,
            PodcastListServiceResponseFactoryFailedType failedFactory,
            @Nullable NetworkPauser networkPauser,
            PodcastSearchType podcastSearch
    ) {
        final PodcastSearchIdentifiedOptType podcastSearchIdentifiedOpt =
                database.upsertPodcastSearch(podcastSearch);

        final class SawMarkerAndPodcastIdentifiedList extends Tuple<Boolean, PodcastIdentifiedListType> {
            public SawMarkerAndPodcastIdentifiedList(
                    Boolean sawMarker,
                    PodcastIdentifiedListType podcastIdentifiedList
            ) {
                super(
                        sawMarker,
                        podcastIdentifiedList
                );
            }
        }

        return podcastSearchIdentifiedOpt.toOptional().map(
                podcastSearchIdentified -> {
                    final Object marker = new Object();
                    final Observable<SawMarkerAndPodcastIdentifiedList> sawMarkerAndPodcastIdentifiedListObservable =
                            database
                                    .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified.getIdentifier())
                                    .scan(
                                            new SawMarkerAndPodcastIdentifiedList(
                                                    false,
                                                    podcastIdentifiedListEmptyFactory.newCollection()
                                            ),
                                            (sawMarkerAndPodcastIdentifiedList, podcastIdentifiedListMarkedValue) -> {
                                                final boolean oldSawMarker = sawMarkerAndPodcastIdentifiedList.first;
                                                final boolean newSawMarker = oldSawMarker || podcastIdentifiedListMarkedValue.markers.contains(marker);
                                                return new SawMarkerAndPodcastIdentifiedList(
                                                        newSawMarker,
                                                        podcastIdentifiedListMarkedValue.value
                                                );
                                            }
                                    )
                                    // scan emits the initial accumulator value as the first itme
                                    // and we don't want that
                                    .skip(1);
                    final Observable<EmptyServiceResponse> podcastSearchServiceResponseObservable =
                            searchForPodcasts(
                                    networkPauser,
                                    podcastSearch.getSearch()
                            )
                                    .subscribeOn(scheduler)
                                    .observeOn(scheduler)
                                    .doOnSuccess(
                                            podcastSearchResponse ->
                                                    database.replacePodcastSearchPodcasts(
                                                            marker,
                                                            podcastSearchIdentified,
                                                            podcastSearchResponse.podcasts
                                                    )
                                    )
                                    .map(ignored -> EmptyServiceResponse.COMPLETE)
                                    .doOnError(
                                            ignored ->
                                                    database.markPodcastSearchFailure(marker)
                                    )
                                    .onErrorReturnItem(EmptyServiceResponse.FAILED)
                                    .toObservable().startWith(EmptyServiceResponse.LOADING);

                    return Observable.combineLatest(
                            podcastSearchServiceResponseObservable,
                            sawMarkerAndPodcastIdentifiedListObservable,
                            (podcastSearchServiceResponse, sawMarkerAndPodcastIdentifiedList) -> {
                                final boolean sawMarker = sawMarkerAndPodcastIdentifiedList.first;
                                final PodcastIdentifiedListType podcastIdentifiedList = sawMarkerAndPodcastIdentifiedList.second;
                                return podcastSearchServiceResponse.reduce(
                                        loading -> {
                                            // we don't care if we saw the marker because we don't know if
                                            // we completed or failed
                                            return loadingFactory.newPodcastListServiceResponse(
                                                    podcastIdentifiedList
                                            );
                                        },
                                        complete -> {
                                            if (sawMarker) {
                                                return completeFactory.newPodcastListServiceResponse(
                                                        podcastIdentifiedList
                                                );
                                            } else {
                                                // we haven't seen the marker yet, so
                                                // even though the service finished, the database
                                                // transaction hasn't completed, so stay loading
                                                return failedFactory.newPodcastListServiceResponse(
                                                        podcastIdentifiedList
                                                );
                                            }
                                        },
                                        failed -> {
                                            if (sawMarker) {
                                                return failedFactory.newPodcastListServiceResponse(
                                                        podcastIdentifiedList
                                                );
                                            } else {
                                                // we haven't seen the marker yet, so
                                                // even though the service finished, the database
                                                // transaction hasn't completed, so stay loading
                                                return loadingFactory.newPodcastListServiceResponse(
                                                        podcastIdentifiedList
                                                );
                                            }
                                        }
                                );
                            });
                }
        ).orElse(Observable.error(new UpsertPodcastSearchException(podcastSearch)));
    }

    private Single<PodcastSearchResponse<PodcastListType>> searchForPodcasts(
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
            maybePausedResponseSingle = networkPauser.pauseSingle(responseSingle);
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

                                    final PodcastListType podcasts =
                                            Optional
                                                    .ofNullable(podcastSearchResultsJson.results)
                                                    .filter(Objects::nonNull)
                                                    .map(List::stream)
                                                    .orElseGet(Stream::empty)
                                                    .map(this::toPodcast)
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toCollection(podcastListEmptyFactory::newCollection));
                                    return Single.just(new PodcastSearchResponse<>(podcasts));
                                }
                            } else {
                                return Single.error(new Exception("HTTP Error: " + responseCode));
                            }
                        }
                );
    }

    @Nullable
    private PodcastType toPodcast(PodcastJson podcastJson) {
        @Nullable final String artistName = podcastJson.artistName;
        @Nullable final String artwork30URLString = podcastJson.artworkUrl30;
        @Nullable final String artwork60URLString = podcastJson.artworkUrl60;
        @Nullable final String artwork100URLString = podcastJson.artworkUrl100;
        @Nullable final String artwork600URLString = podcastJson.artworkUrl600;
        @Nullable final String collectionName = podcastJson.collectionName;
        @Nullable final String feedURLString = podcastJson.feedUrl;

        if (
                artistName == null ||
                        collectionName == null ||
                        feedURLString == null
        ) {
            return null;
        } else {
            final Optional<URL> artworkURLOptional =
                    Stream.of(
                            artwork600URLString,
                            artwork100URLString,
                            artwork60URLString,
                            artwork30URLString
                    )
                            .map(URLUtil::fromString)
                            .filter(Objects::nonNull)
                            .findFirst();
            @Nullable final URL feedURL = URLUtil.fromString(feedURLString);
            if (feedURL == null) {
                return null;
            } else {
                return podcastFactory.newPodcast(
                        artworkURLOptional.orElse(null),
                        artistName,
                        feedURL,
                        collectionName
                );
            }
        }
    }

    public Single<EpisodeIdentifiedListType> fetchEpisodeIdentifieds(URL url) {
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
                                            final AtomicInteger fakeIdAtomicInteger = new AtomicInteger(-1);
                                            final EpisodeIdentifiedListType episodes =
                                                    xmlParser
                                                            .parseEpisodeList(responseBody.byteStream())
                                                            .stream()
                                                            .map(episode ->
                                                                    episodeIdentifiedFactory.newIdentified(
                                                                            episodeIdentifierFactory.newIdentifier(
                                                                                    fakeIdAtomicInteger.getAndDecrement()
                                                                            ),
                                                                            episode
                                                                    )
                                                            )
                                                            .collect(
                                                                    Collectors.toCollection(
                                                                            episodeIdentifiedListEmptyFactory::newCollection
                                                                    )
                                                            );
                                            return Single.just(episodes);
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
}
