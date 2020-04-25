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
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.Subscription;
import com.stealthmountain.sqldim.DimDatabase;
import com.stealthmountain.sqldim.SqlDim;
import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;

public final class Database<
        MarkerType,
        EpisodeType extends Episode,
        EpisodeIdentifiedType extends Episode.EpisodeIdentified<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
        EpisodeIdentifiedListType extends Episode.EpisodeIdentified.EpisodeIdentifiedList2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedSetType extends Episode.EpisodeIdentified.EpisodeIdentifiedSet2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode.EpisodeIdentifier,
        EpisodeIdentifierOptType extends Episode.EpisodeIdentifier.EpisodeIdentifierOpt<EpisodeIdentifierType>,
        EpisodeIdentifierOptListType extends Episode.EpisodeIdentifier.EpisodeIdentifierOpt.EpisodeIdentifierOptList2<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType
                >,
        EpisodeListType extends Episode.EpisodeList2<
                EpisodeType
                >,
        PodcastType extends Podcast,
        PodcastIdentifiedType extends Podcast.PodcastIdentified<
                        PodcastIdentifierType,
                        PodcastType
                        >,
        PodcastIdentifiedListType extends Podcast.PodcastIdentified.PodcastIdentifiedList2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedSetType extends Podcast.PodcastIdentified.PodcastIdentifiedSet2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast.PodcastIdentifier,
        PodcastIdentifierOptType extends Podcast.PodcastIdentifier.PodcastIdentifierOpt<PodcastIdentifierType>,
        PodcastIdentifierOptListType extends Podcast.PodcastIdentifier.PodcastIdentifierOpt.PodcastIdentifierOptList2<
                PodcastIdentifierOptType,
                PodcastIdentifierType
                >,
        PodcastSearchType extends PodcastSearch,
        PodcastSearchIdentifiedType extends PodcastSearch.PodcastSearchIdentified<
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
        PodcastSearchIdentifiedListType extends PodcastSearch.PodcastSearchIdentified.PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType,
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifiedOptType extends PodcastSearch.PodcastSearchIdentified.PodcastSearchIdentifiedOpt<
                        PodcastSearchIdentifiedType,
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
        PodcastSearchIdentifierType extends PodcastSearch.PodcastSearchIdentifier,
        PodcastSearchIdentifierOptType extends PodcastSearch.PodcastSearchIdentifier.PodcastSearchIdentifierOpt<
                        PodcastSearchIdentifierType
                        >,
        SubscriptionType extends Subscription<
                                PodcastIdentifiedType,
                                PodcastIdentifierType,
                                PodcastType
                                >,
        SubscriptionIdentifiedType extends Subscription.SubscriptionIdentified<
                        SubscriptionIdentifierType,
                        SubscriptionType,
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
        SubscriptionIdentifiedListType extends Subscription.SubscriptionIdentified.SubscriptionIdentifiedList2<
                SubscriptionIdentifiedType,
                SubscriptionIdentifierType,
                SubscriptionType,
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifierType extends Subscription.SubscriptionIdentifier
        > {

    private final Identified.IdentifiedFactory2<
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > podcastIdentifiedFactory;
    private final CollectionFactory.Capacity<
            PodcastIdentifiedListType,
            PodcastIdentifiedType
            > podcastIdentifiedListCapacityFactory;
    private final Identified.IdentifiedFactory2<
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifierType,
            PodcastSearchType
            > podcastSearchIdentifiedFactory;
    private final Opt.OptEmptyFactory<
            PodcastSearchIdentifiedOptType,
            PodcastSearchIdentifiedType
            > podcastSearchIdentifiedOptEmptyFactory;
    private final Opt.OptNonEmptyFactory<
            PodcastSearchIdentifiedOptType,
            PodcastSearchIdentifiedType
            > podcastSearchIdentifiedOptNonEmptyFactory;
    private final Subscription.SubscriptionFactory2<
            SubscriptionType,
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > subscriptionFactory;
    private final Identified.IdentifiedFactory2<
            SubscriptionIdentifiedType,
            SubscriptionIdentifierType,
            SubscriptionType
            > subscriptionIdentifiedFactory;

    private final DimDatabase<MarkerType> dimDatabase;
    final MetaTable<MarkerType> metaTable;
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
            Episode.EpisodeFactory2<EpisodeType> episodeFactory,
            Identifier.IdentifierFactory2<
                    EpisodeIdentifierType
                    > episodeIdentifierFactory,
            Identified.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            Opt.OptEmptyFactory<
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierType
                    > episodeIdentifierOptEmptyFactory,
            Opt.OptNonEmptyFactory<
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
            Podcast.PodcastFactory2<PodcastType> podcastFactory,
            Identifier.IdentifierFactory2<
                    PodcastIdentifierType
                    > podcastIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > podcastIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    > podcastIdentifiedListCapacityFactory,
            Opt.OptEmptyFactory<
                    PodcastIdentifierOptType,
                    PodcastIdentifierType
                    > podcastIdentifierOptEmptyFactory,
            Opt.OptNonEmptyFactory<
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
            PodcastSearch.PodcastSearchFactory2<PodcastSearchType> podcastSearchFactory,
            Identifier.IdentifierFactory2<PodcastSearchIdentifierType> podcastSearchIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifierType,
                    PodcastSearchType
                    > podcastSearchIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedListCapacityFactory,
            Opt.OptEmptyFactory<
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedOptEmptyFactory,
            Opt.OptNonEmptyFactory<
                    PodcastSearchIdentifiedOptType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedOptNonEmptyFactory,
            Opt.OptEmptyFactory<
                    PodcastSearchIdentifierOptType,
                    PodcastSearchIdentifierType
                    > podcastSearchIdentifierOptEmptyFactory,
            Opt.OptNonEmptyFactory<
                    PodcastSearchIdentifierOptType,
                    PodcastSearchIdentifierType
                    > podcastSearchIdentifierOptNonEmptyFactory,
            Subscription.SubscriptionFactory2<
                    SubscriptionType,
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > subscriptionFactory,
            Identifier.IdentifierFactory2<
                    SubscriptionIdentifierType
                    > subscriptionIdentifierFactory,
            Identified.IdentifiedFactory2<
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
        metaTable = new MetaTable<>(dimDatabase);
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
            MetaTable.createMetaTable(db);
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
