package com.github.hborders.heathcast.dao;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.ListUtil;
import com.github.hborders.heathcast.core.Opt2;
import com.github.hborders.heathcast.core.Result;
import com.stealthmountain.sqldim.DimDatabase;
import com.stealthmountain.sqldim.SqlDim;
import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public final class Database<
        MarkerType,
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

    private final Identified2.IdentifiedFactory2<
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > podcastIdentifiedFactory;
    private final CollectionFactory.Capacity<
            PodcastIdentifiedListType,
            PodcastIdentifiedType
            > podcastIdentifiedListCapacityFactory;
    private final Identified2.IdentifiedFactory2<
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifierType,
            PodcastSearchType
            > podcastSearchIdentifiedFactory;
    private final Opt2.OptEmptyFactory<
            PodcastSearchIdentifiedOptType,
            PodcastSearchIdentifiedType
            > podcastSearchIdentifiedOptEmptyFactory;
    private final Opt2.OptNonEmptyFactory<
            PodcastSearchIdentifiedOptType,
            PodcastSearchIdentifiedType
            > podcastSearchIdentifiedOptNonEmptyFactory;
    private final Subscription2.SubscriptionFactory2<
            SubscriptionType,
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > subscriptionFactory;
    private final Identified2.IdentifiedFactory2<
            SubscriptionIdentifiedType,
            SubscriptionIdentifierType,
            SubscriptionType
            > subscriptionIdentifiedFactory;

    private final DimDatabase<MarkerType> dimDatabase;
    final EpisodeTable<
            MarkerType,
            EpisodeType,
            EpisodeIdentifiedType,
            EpisodeIdentifiedListType,
            EpisodeIdentifiedSetType,
            EpisodeIdentifierType,
            EpisodeIdentifierOptType,
            EpisodeIdentifierOptListType,
            EpisodeListType,
            PodcastIdentifierType
            > episodeTable;
    final PodcastTable<
            MarkerType,
            PodcastType,
            PodcastIdentifiedType,
            PodcastIdentifiedSetType,
            PodcastIdentifierType,
            PodcastIdentifierOptType,
            PodcastIdentifierOptListType
            > podcastTable;
    final PodcastSearchTable<
            MarkerType,
            PodcastSearchType,
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifiedListType,
            PodcastSearchIdentifierType,
            PodcastSearchIdentifierOptType
            > podcastSearchTable;
    private final PodcastSearchResultTable<
            MarkerType,
            PodcastIdentifierType,
            PodcastSearchIdentifierType
            > podcastSearchResultTable;
    final SubscriptionTable<
            MarkerType,
            PodcastType,
            PodcastIdentifiedType,
            PodcastIdentifierType,
            SubscriptionType,
            SubscriptionIdentifiedType,
            SubscriptionIdentifiedListType,
            SubscriptionIdentifierType
            > subscriptionTable;

    public Database(
            Context context,
            @Nullable String name,
            Scheduler scheduler,
            Episode2.EpisodeFactory2<EpisodeType> episodeFactory,
            Identifier2.IdentifierFactory2<
                    EpisodeIdentifierType
                    > episodeIdentifierFactory,
            Identified2.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            Opt2.OptEmptyFactory<
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierType
                    > episodeIdentifierOptEmptyFactory,
            Opt2.OptNonEmptyFactory<
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierType
                    > episodeIdentifierOptNonEmptyFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListCapacityFactory,
            CollectionFactory.Collection<
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedSetCollectionFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifierOptListType,
                    EpisodeIdentifierOptType
                    > episodeIdentifierOptListCapacityFactory,
            Podcast2.PodcastFactory2<PodcastType> podcastFactory,
            Identifier2.IdentifierFactory2<
                    PodcastIdentifierType
                    > podcastIdentifierFactory,
            Identified2.IdentifiedFactory2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > podcastIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    > podcastIdentifiedListCapacityFactory,
            Opt2.OptEmptyFactory<
                    PodcastIdentifierOptType,
                    PodcastIdentifierType
                    > podcastIdentifierOptEmptyFactory,
            Opt2.OptNonEmptyFactory<
                    PodcastIdentifierOptType,
                    PodcastIdentifierType
                    > podcastIdentifierOptNonEmptyFactory,
            CollectionFactory.Collection<
                    PodcastIdentifiedSetType,
                    PodcastIdentifiedType
                    > podcastIdentifiedSetCollectionFactory,
            CollectionFactory.Capacity<
                    PodcastIdentifierOptListType,
                    PodcastIdentifierOptType
                    > podcastIdentifierOptListCapacityFactory,
            PodcastSearch2.PodcastSearchFactory2<PodcastSearchType> podcastSearchFactory,
            Identifier2.IdentifierFactory2<PodcastSearchIdentifierType> podcastSearchIdentifierFactory,
            Identified2.IdentifiedFactory2<
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifierType,
                    PodcastSearchType
                    > podcastSearchIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedListCapacityFactory,
            Opt2.OptEmptyFactory<
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedOptEmptyFactory,
            Opt2.OptNonEmptyFactory<
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedOptNonEmptyFactory,
            Opt2.OptEmptyFactory<
                    PodcastSearchIdentifierOptType,
                    PodcastSearchIdentifierType
                    > podcastSearchIdentifierOptEmptyFactory,
            Opt2.OptNonEmptyFactory<
                    PodcastSearchIdentifierOptType,
                    PodcastSearchIdentifierType
                    > podcastSearchIdentifierOptNonEmptyFactory,
            Subscription2.SubscriptionFactory2<
                    SubscriptionType,
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > subscriptionFactory,
            Identifier2.IdentifierFactory2<
                    SubscriptionIdentifierType
                    > subscriptionIdentifierFactory,
            Identified2.IdentifiedFactory2<
                    SubscriptionIdentifiedType,
                    SubscriptionIdentifierType,
                    SubscriptionType
                    > subscriptionIdentifiedFactory,
            CollectionFactory.Capacity<
                    SubscriptionIdentifiedListType,
                    SubscriptionIdentifiedType
                    > subscriptionIdentifiedListCapacityFactory
    ) {
        this.podcastIdentifiedFactory = podcastIdentifiedFactory;
        this.podcastIdentifiedListCapacityFactory = podcastIdentifiedListCapacityFactory;
        this.podcastSearchIdentifiedFactory = podcastSearchIdentifiedFactory;
        this.podcastSearchIdentifiedOptEmptyFactory = podcastSearchIdentifiedOptEmptyFactory;
        this.podcastSearchIdentifiedOptNonEmptyFactory = podcastSearchIdentifiedOptNonEmptyFactory;
        this.subscriptionFactory = subscriptionFactory;
        this.subscriptionIdentifiedFactory = subscriptionIdentifiedFactory;

        final Configuration configuration = Configuration
                .builder(context)
                .callback(new Schema())
                .name(name)
                .build();
        final Factory factory = new FrameworkSQLiteOpenHelperFactory();
        final SupportSQLiteOpenHelper supportSQLiteOpenHelper = factory.create(configuration);
        final SqlDim<MarkerType> sqlBrite = new SqlDim.Builder<MarkerType>().build();
        dimDatabase = sqlBrite.wrapDatabaseHelper(
                supportSQLiteOpenHelper,
                scheduler
        );
        episodeTable = new EpisodeTable<>(
                dimDatabase,
                episodeFactory,
                episodeIdentifierFactory,
                episodeIdentifiedFactory,
                episodeIdentifierOptEmptyFactory,
                episodeIdentifierOptNonEmptyFactory,
                episodeIdentifiedListCapacityFactory,
                episodeIdentifiedSetCollectionFactory,
                episodeIdentifierOptListCapacityFactory
        );
        podcastTable = new PodcastTable<>(
                dimDatabase,
                podcastFactory,
                podcastIdentifierFactory,
                podcastIdentifiedFactory,
                podcastIdentifierOptEmptyFactory,
                podcastIdentifierOptNonEmptyFactory,
                podcastIdentifiedSetCollectionFactory,
                podcastIdentifierOptListCapacityFactory
        );
        podcastSearchTable = new PodcastSearchTable<>(
                dimDatabase,
                podcastSearchFactory,
                podcastSearchIdentifierFactory,
                podcastSearchIdentifiedFactory,
                podcastSearchIdentifiedListCapacityFactory,
                podcastSearchIdentifierOptEmptyFactory,
                podcastSearchIdentifierOptNonEmptyFactory
        );
        podcastSearchResultTable = new PodcastSearchResultTable<>(
                dimDatabase
        );
        subscriptionTable = new SubscriptionTable<>(
                dimDatabase,
                subscriptionFactory,
                subscriptionIdentifierFactory,
                subscriptionIdentifiedFactory,
                podcastFactory,
                podcastIdentifierFactory,
                podcastIdentifiedFactory,
                subscriptionIdentifiedListCapacityFactory
        );
    }

    public PodcastSearchIdentifiedOptType upsertPodcastSearch(PodcastSearchType podcastSearch) {
        return podcastSearchTable
                .upsertPodcastSearch(podcastSearch)
                .map(
                        podcastSearchIdentifiedOptEmptyFactory,
                        podcastSearchIdentifiedOptNonEmptyFactory,
                        podcastSearchIdentifier -> podcastSearchIdentifiedFactory.newIdentified(
                                podcastSearchIdentifier,
                                podcastSearch
                        )
                );
    }

    public void replacePodcastSearchPodcasts(
            MarkerType marker,
            PodcastSearchIdentifiedType podcastSearchIdentified,
            List<PodcastType> podcasts
    ) {
        try (final DimDatabase.Transaction<MarkerType> transaction = dimDatabase.newTransaction()) {
            podcastSearchResultTable.deletePodcastSearchResultsByPodcastSearchIdentifier(podcastSearchIdentified.getIdentifier());
            ListUtil.indexedStream(podcasts).forEach(indexedPodcast ->
                    podcastTable.upsertPodcast(indexedPodcast.second).act(
                            podcastIdentifier ->
                                    podcastSearchResultTable.insertPodcastSearchResult(
                                            podcastIdentifier,
                                            podcastSearchIdentified.getIdentifier(),
                                            indexedPodcast.first
                                    )
                    )
            );

            if (podcasts.isEmpty()) {
                podcastTable.triggerMarked(marker);
            }
            transaction.markSuccessful(marker);
        }
    }

    public void markPodcastSearchFailure(MarkerType marker) {
        podcastTable.triggerMarked(marker);
    }

    public Optional<PodcastIdentifiedType> upsertPodcast(PodcastType podcast) {
        return podcastTable
                .upsertPodcast(podcast)
                .toOptional()
                .map(
                        podcastIdentifier -> podcastIdentifiedFactory.newIdentified(
                                podcastIdentifier,
                                podcast
                        )
                );
    }

    public void upsertEpisodesForPodcast(
            PodcastIdentifierType podcastIdentifier,
            EpisodeListType episodes
    ) {
        episodeTable.upsertEpisodes(
                podcastIdentifier,
                episodes
        );
    }

    public Optional<SubscriptionIdentifiedType> subscribe(PodcastIdentifiedType podcastIdentified) {
        return subscribe(podcastIdentified.getIdentifier())
                .map(
                        subscriptionIdentifier -> subscriptionIdentifiedFactory.newIdentified(
                                subscriptionIdentifier,
                                subscriptionFactory.newSubscription(podcastIdentified)
                        )
                );
    }

    public Optional<SubscriptionIdentifierType> subscribe(PodcastIdentifierType podcastIdentifier) {
        return subscriptionTable.insertSubscription(podcastIdentifier);
    }

    public Result unsubscribe(SubscriptionIdentifierType subscriptionIdentifier) {
        final int deleteCount = subscriptionTable.deleteSubscription(subscriptionIdentifier);
        return deleteCount > 0 ? Result.SUCCESS : Result.FAILURE;
    }

    public Observable<PodcastSearchIdentifiedListType> observeQueryForAllPodcastSearchIdentifieds() {
        return podcastSearchTable.observeQueryForAllPodcastSearchIdentifieds();
    }

    public Observable<PodcastIdentifiedListType> observeQueryForPodcastIdentifieds(
            PodcastSearchIdentifierType podcastSearchIdentifier
    ) {
        return dimDatabase.createQuery(
                Arrays.asList(
                        PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT,
                        PodcastTable.TABLE_PODCAST,
                        PodcastSearchTable.TABLE_PODCAST_SEARCH
                ),
                "SELECT "
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ARTWORK_URL + " AS " + PodcastTable.ARTWORK_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.AUTHOR + " AS " + PodcastTable.AUTHOR + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.FEED_URL + " AS " + PodcastTable.FEED_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " AS " + PodcastTable.ID + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.NAME + " AS " + PodcastTable.NAME + " "
                        + "FROM " + PodcastTable.TABLE_PODCAST + " "
                        + "INNER JOIN " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + " "
                        + "  ON " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_ID + " "
                        + "    = " + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " "
                        + "WHERE " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_SEARCH_ID + " = ?",
                podcastSearchIdentifier.getId()
        ).mapToSpecificList(
                podcastTable::getPodcastIdentified,
                podcastIdentifiedListCapacityFactory::newCollection
        );
    }

    public Observable<
            MarkedValue<
                    MarkerType,
                    PodcastIdentifiedListType
                    >
            > observeMarkedQueryForPodcastIdentifieds(
            PodcastSearchIdentifierType podcastSearchIdentifier
    ) {
        return dimDatabase.createMarkedQuery(
                Arrays.asList(
                        PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT,
                        PodcastTable.TABLE_PODCAST,
                        PodcastSearchTable.TABLE_PODCAST_SEARCH
                ),
                "SELECT "
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ARTWORK_URL + " AS " + PodcastTable.ARTWORK_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.AUTHOR + " AS " + PodcastTable.AUTHOR + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.FEED_URL + " AS " + PodcastTable.FEED_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " AS " + PodcastTable.ID + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.NAME + " AS " + PodcastTable.NAME + " "
                        + "FROM " + PodcastTable.TABLE_PODCAST + " "
                        + "INNER JOIN " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + " "
                        + "  ON " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_ID + " "
                        + "    = " + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " "
                        + "WHERE " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_SEARCH_ID + " = ?",
                podcastSearchIdentifier.getId()
        )
                .mapToSpecificList(
                        (cursor, ns) -> podcastTable.getPodcastIdentified(cursor),
                        podcastIdentifiedListCapacityFactory::newCollection
                );
    }

    public Observable<Optional<PodcastIdentifiedType>> observeQueryForPodcastIdentified(
            PodcastIdentifierType podcastIdentifier
    ) {
        return podcastTable.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    public Observable<SubscriptionIdentifiedListType> observeQueryForSubscriptions() {
        return subscriptionTable.observeQueryForSubscriptions();
    }

    public Observable<Optional<SubscriptionIdentifierType>> observeQueryForSubscriptionIdentifier(
            PodcastIdentifierType podcastIdentifier
    ) {
        return subscriptionTable.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    public Observable<EpisodeIdentifiedListType> observeQueryForEpisodeIdentifiedsForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        return episodeTable.observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier);
    }

    public boolean deletePodcast(PodcastIdentifierType podcastIdentifier) {
        final int deleteCount = podcastTable.deletePodcast(podcastIdentifier);
        return deleteCount > 0;
    }

    public boolean deletePodcastSearch(PodcastSearchIdentifierType podcastSearchIdentifier) {
        final int deleteCount = podcastSearchTable.deletePodcastSearchById(podcastSearchIdentifier);
        return deleteCount > 0;
    }

    static final class Schema extends Callback {
        Schema() {
            super(1);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            PodcastSearchTable.createPodcastSearchTable(db);
            PodcastTable.createPodcastTable(db);
            EpisodeTable.createEpisodeTable(db);
            PodcastSearchResultTable.createPodcastSearchResultTable(db);
            SubscriptionTable.createSubscriptionTable(db);
        }

        public void onConfigure(SupportSQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }

        @Override
        public void onUpgrade(
                SupportSQLiteDatabase db,
                int oldVersion,
                int newVersion
        ) {
            throw new AssertionError();
        }
    }
}
