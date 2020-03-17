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
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.EpisodeList;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastIdentifiedOpt;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.models.PodcastList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.PodcastSearchIdentified;
import com.github.hborders.heathcast.models.PodcastSearchIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearchIdentifiedOpt;
import com.github.hborders.heathcast.models.PodcastSearchIdentifier;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.models.SubscriptionIdentified;
import com.github.hborders.heathcast.models.SubscriptionIdentifiedList;
import com.github.hborders.heathcast.models.SubscriptionIdentifiedOpt;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
import com.github.hborders.heathcast.models.SubscriptionIdentifierOpt;
import com.stealthmountain.sqldim.DimDatabase;
import com.stealthmountain.sqldim.SqlDim;
import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public final class Database<N> {
    private final DimDatabase<N> dimDatabase;
    final PodcastSearchTable<N> podcastSearchTable;
    final PodcastTable<N> podcastTable;
    final EpisodeTable<N> episodeTable;
    private final PodcastSearchResultTable<N> podcastSearchResultTable;
    final SubscriptionTable<N> subscriptionTable;

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
        final SqlDim<N> sqlBrite = new SqlDim.Builder<N>().build();
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

    public PodcastSearchIdentifiedOpt upsertPodcastSearch(PodcastSearch podcastSearch) {
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
            N marker,
            PodcastSearchIdentified podcastSearchIdentified,
            PodcastList podcasts
    ) {
        try (final DimDatabase.Transaction<N> transaction = dimDatabase.newTransaction()) {
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

    public void markPodcastSearchFailure(N marker) {
        podcastTable.triggerMarked(marker);
    }

    public PodcastIdentifiedOpt upsertPodcast(Podcast podcast) {
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
            PodcastIdentifier podcastIdentifier,
            EpisodeList episodes
    ) {
        episodeTable.upsertEpisodes(
                podcastIdentifier,
                episodes
        );
    }

    public SubscriptionIdentifiedOpt subscribe(PodcastIdentified podcastIdentified) {
        return subscribe(podcastIdentified.identifier)
                .map(
                        SubscriptionIdentifiedOpt.FACTORY,
                        subscriptionIdentifier -> new SubscriptionIdentified(
                                subscriptionIdentifier,
                                new Subscription(podcastIdentified)
                        )
                );
    }

    public SubscriptionIdentifierOpt subscribe(PodcastIdentifier podcastIdentifier) {
        return subscriptionTable.insertSubscription(podcastIdentifier);
    }

    public Result unsubscribe(SubscriptionIdentifier subscriptionIdentifier) {
        final int deleteCount = subscriptionTable.deleteSubscription(subscriptionIdentifier);
        return deleteCount > 0 ? Result.SUCCESS : Result.FAILURE;
    }

    public Observable<PodcastSearchIdentifiedList> observeQueryForAllPodcastSearchIdentifieds() {
        return podcastSearchTable.observeQueryForAllPodcastSearchIdentifieds();
    }

    public Observable<PodcastIdentifiedList> observeQueryForPodcastIdentifieds(
            PodcastSearchIdentifier podcastSearchIdentifier
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

    public Observable<MarkedValue<N, PodcastIdentifiedList>> observeMarkedQueryForPodcastIdentifieds(
            PodcastSearchIdentifier podcastSearchIdentifier
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
                podcastSearchIdentifier.id
        )
                .mapToSpecificList(
                        (cursor, ns) -> PodcastTable.getPodcastIdentified(cursor),
                        PodcastIdentifiedList::new
                );
    }

    public Observable<PodcastIdentifiedOpt> observeQueryForPodcastIdentified(
            PodcastIdentifier podcastIdentifier
    ) {
        return podcastTable.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    public Observable<SubscriptionIdentifiedList> observeQueryForSubscriptions() {
        return subscriptionTable.observeQueryForSubscriptions();
    }

    public Observable<Optional<SubscriptionIdentifier>> observeQueryForSubscriptionIdentifier(
            PodcastIdentifier podcastIdentifier
    ) {
        return subscriptionTable.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    public Observable<EpisodeIdentifiedList> observeQueryForEpisodeIdentifiedsForPodcast(
            PodcastIdentifier podcastIdentifier
    ) {
        return episodeTable.observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier);
    }

    public boolean deletePodcast(PodcastIdentifier podcastIdentifier) {
        final int deleteCount = podcastTable.deletePodcast(podcastIdentifier);
        return deleteCount > 0;
    }

    public boolean deletePodcastSearch(PodcastSearchIdentifier podcastSearchIdentifier) {
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
