package com.github.hborders.heathcast.dao;

import android.content.Context;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.core.ListUtil;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public final class Database {
    private final BriteDatabase briteDatabase;
    final PodcastSearchTable podcastSearchTable;
    final PodcastTable podcastTable;
    final EpisodeTable episodeTable;
    private final PodcastSearchResultTable podcastSearchResultTable;
    final SubscriptionTable subscriptionTable;

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
        final SqlBrite sqlBrite = new SqlBrite.Builder().build();
        briteDatabase = sqlBrite.wrapDatabaseHelper(
                supportSQLiteOpenHelper,
                scheduler
        );
        podcastSearchTable = new PodcastSearchTable(briteDatabase);
        podcastTable = new PodcastTable(briteDatabase);
        episodeTable = new EpisodeTable(briteDatabase);
        podcastSearchResultTable = new PodcastSearchResultTable(briteDatabase);
        subscriptionTable = new SubscriptionTable(briteDatabase);
    }

    public Optional<Identified<PodcastSearch>> upsertPodcastSearch(PodcastSearch podcastSearch) {
        return podcastSearchTable
                .upsertPodcastSearch(podcastSearch)
                .map(podcastSearchIdentifier -> new Identified<>(
                                podcastSearchIdentifier,
                                podcastSearch
                        )
                );
    }

    public void replacePodcastSearchPodcasts(
            Identified<PodcastSearch> podcastSearchIdentified,
            List<Podcast> podcasts
    ) {
        try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
            podcastSearchResultTable.deletePodcastSearchResultsByPodcastSearchIdentifier(podcastSearchIdentified.identifier);
            ListUtil.indexedStream(podcasts).forEach(indexedPodcast ->
                    podcastTable.upsertPodcast(indexedPodcast.second).ifPresent(
                            podcastIdentifier ->
                                    podcastSearchResultTable.insertPodcastSearchResult(
                                            podcastIdentifier,
                                            podcastSearchIdentified.identifier,
                                            indexedPodcast.first
                                    )
                    )
            );

            transaction.markSuccessful();
        }
    }

    public Optional<Identified<Podcast>> upsertPodcast(Podcast podcast) {
        return podcastTable
                .upsertPodcast(podcast)
                .map(podcastIdentifier -> new Identified<>(
                                podcastIdentifier,
                                podcast
                        )
                );
    }

    public void upsertEpisodesForPodcast(
            Identifier<Podcast> podcastIdentifier,
            List<Episode> episodes
    ) {
        episodeTable.upsertEpisodes(
                podcastIdentifier,
                episodes
        );
    }

    public Optional<Identified<Subscription>> subscribe(Identified<Podcast> podcastIdentified) {
        return subscribe(podcastIdentified.identifier)
                .map(subscriptionIdentifier -> new Identified<>(
                                subscriptionIdentifier,
                                new Subscription(podcastIdentified)
                        )
                );
    }

    public Optional<Identifier<Subscription>> subscribe(Identifier<Podcast> podcastIdentifier) {
        return subscriptionTable.insertSubscription(podcastIdentifier);
    }

    public boolean unsubscribe(Identifier<Subscription> subscriptionIdentifier) {
        final int deleteCount = subscriptionTable.deleteSubscription(subscriptionIdentifier);
        return deleteCount > 0;
    }

    public Observable<List<Identified<PodcastSearch>>> observeQueryForAllPodcastSearchIdentifieds() {
        return podcastSearchTable.observeQueryForAllPodcastSearchIdentifieds();
    }

    public Observable<List<Identified<Podcast>>> observeQueryForPodcastIdentifieds(
            Identifier<PodcastSearch> podcastSearchIdentifier
    ) {
        return briteDatabase.createQuery(
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
        ).mapToList(PodcastTable::getPodcastIdentified);
    }

    public Observable<Optional<Identified<Podcast>>> observeQueryForPodcastIdentified(
            Identifier<Podcast> podcastIdentifier
    ) {
        return podcastTable.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    public Observable<List<Identified<Subscription>>> observeQueryForSubscriptions() {
        return subscriptionTable.observeQueryForSubscriptions();
    }

    public Observable<Optional<Identifier<Subscription>>> observeQueryForSubscriptionIdentifier(
            Identifier<Podcast> podcastIdentifier
    ) {
        return subscriptionTable.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    public Observable<List<Identified<Episode>>> observeQueryForEpisodeIdentifiedsForPodcast(
            Identifier<Podcast> podcastIdentifier
    ) {
        return episodeTable.observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier);
    }

    public boolean deletePodcast(Identifier<Podcast> podcastIdentifier) {
        final int deleteCount = podcastTable.deletePodcast(podcastIdentifier);
        return deleteCount > 0;
    }

    public boolean deletePodcastSearch(Identifier<PodcastSearch> podcastSearchIdentifier) {
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
