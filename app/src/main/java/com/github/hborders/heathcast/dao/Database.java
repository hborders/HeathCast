package com.github.hborders.heathcast.dao;

import android.content.Context;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.core.ListUtil;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.models.PodcastSearchIdentified;
import com.stealthmountain.sqldim.DimDatabase;
import com.stealthmountain.sqldim.SqlDim;
import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public final class Database<
        MarkerType,
        PodcastSearchType extends PodcastSearch2,
        PodcastSearchIdentifiedType extends PodcastSearch2.PodcastSearchIdentified2<
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifierType extends PodcastSearch2.PodcastSearchIdentifier2,
        PodcastSearchIdentifiedListType extends PodcastSearch2.PodcastSearchIdentified2.PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType,
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifierOptType extends PodcastSearch2.PodcastSearchIdentifier2.PodcastSearchIdentifierOpt2<
                PodcastSearchIdentifierType
                >,
        PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
        PodcastType extends Podcast2,
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
        PodcastIdentifierOptListType extends Podcast2.PodcastIdentifier2.PodcastIdentifierOpt2.PodcastIdentifierOptList2<
                PodcastIdentifierOptType,
                PodcastIdentifierType
                >,
        PodcastIdentifierOptType extends Podcast2.PodcastIdentifier2.PodcastIdentifierOpt2<PodcastIdentifierType>,
        EpisodeIdentifiedType extends Episode2.EpisodeIdentified2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode2.EpisodeIdentifier2,
        EpisodeType extends Episode2,
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
        EpisodeIdentifierOptListType extends Episode2.EpisodeIdentifier2.EpisodeIdentifierOpt2.EpisodeIdentifierOptList2<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType
                >,
        EpisodeIdentifierOptType extends Episode2.EpisodeIdentifier2.EpisodeIdentifierOpt2<EpisodeIdentifierType>,
        EpisodeListType extends EpisodeList2<
                EpisodeType
                >,
        SubscriptionIdentifiedType extends Subscription2.SubscriptionIdentified2<
                SubscriptionIdentifierType,
                SubscriptionType,
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifierType extends Subscription2.SubscriptionIdentifier2,
        SubscriptionType extends Subscription2<
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
        SubscriptionIdentifierOptType extends Subscription2.SubscriptionIdentifier2.SubscriptionIdentifierOpt2<SubscriptionIdentifierType>
        > {
    private final DimDatabase<MarkerType> dimDatabase;
    final PodcastSearchTable<
            MarkerType,
            PodcastSearchType,
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifierType,
            PodcastSearchIdentifiedListType,
            PodcastSearchIdentifierOptType
            > podcastSearchTable;
    final PodcastTable<
            MarkerType,
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType,
            PodcastIdentifiedSetType,
            PodcastIdentifierOptListType,
            PodcastIdentifierOptType
            > podcastTable;
    final EpisodeTable<
            MarkerType,
            EpisodeIdentifiedType,
            EpisodeIdentifierType,
            EpisodeType,
            EpisodeIdentifiedListType,
            EpisodeIdentifiedSetType,
            EpisodeIdentifierOptListType,
            EpisodeIdentifierOptType,
            EpisodeListType,
            PodcastIdentifierType
            > episodeTable;
    private final PodcastSearchResultTable<
            MarkerType,
            PodcastIdentifierType,
            PodcastSearchIdentifierType
            > podcastSearchResultTable;
    final SubscriptionTable<
            MarkerType,
            SubscriptionIdentifiedType,
            SubscriptionIdentifierType,
            SubscriptionType,
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType,
            SubscriptionIdentifiedListType,
            SubscriptionIdentifierOptType
            > subscriptionTable;

    public Database(
            Context context,
            @Nullable String name,
            Scheduler scheduler
    ) {
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
        podcastSearchTable = new PodcastSearchTable<>(dimDatabase);
        podcastTable = new PodcastTable<>(dimDatabase);
        episodeTable = new EpisodeTable<>(dimDatabase);
        podcastSearchResultTable = new PodcastSearchResultTable<>(dimDatabase);
        subscriptionTable = new SubscriptionTable<>(dimDatabase);
    }

    public PodcastSearchIdentifiedOptType upsertPodcastSearch(PodcastSearchType podcastSearch) {
        return podcastSearchTable
                .upsertPodcastSearch(podcastSearch)
                .map(
                        PodcastSearchIdentifiedOpt.FACTORY,
                        podcastSearchIdentifier -> new PodcastSearchIdentified(
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
            podcastSearchResultTable.deletePodcastSearchResultsByPodcastSearchIdentifier(podcastSearchIdentified.identifier);
            ListUtil.indexedStream(podcasts).forEach(indexedPodcast ->
                    podcastTable.upsertPodcast(indexedPodcast.second).act(
                            podcastIdentifier ->
                                    podcastSearchResultTable.insertPodcastSearchResult(
                                            podcastIdentifier,
                                            podcastSearchIdentified.identifier,
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

    public PodcastIdentifiedOptType upsertPodcast(PodcastType podcast) {
        return podcastTable
                .upsertPodcast(podcast)
                .map(
                        PodcastIdentifiedOpt.FACTORY,
                        podcastIdentifier -> new PodcastIdentified(
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

    public SubscriptionIdentifiedOptType subscribe(PodcastIdentifiedType podcastIdentified) {
        return subscribe(podcastIdentified.identifier)
                .map(
                        SubscriptionIdentifiedOpt.FACTORY,
                        subscriptionIdentifier -> new SubscriptionIdentified(
                                subscriptionIdentifier,
                                new Subscription(podcastIdentified)
                        )
                );
    }

    public SubscriptionIdentifierOptType subscribe(PodcastIdentifierType podcastIdentifier) {
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
                podcastSearchIdentifier.id
        ).mapToSpecificList(
                PodcastTable::getPodcastIdentified,
                PodcastIdentifiedList::new
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
                        (cursor, ns) -> PodcastTable.getPodcastIdentified(cursor),
                        PodcastIdentifiedList::new
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
