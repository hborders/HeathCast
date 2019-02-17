package com.github.hborders.heathcast.dao;

import android.content.Context;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
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
    final PodcastSearchResultTable podcastSearchResultTable;
    final PodcastEpisodeListTable podcastEpisodeListTable;

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
        podcastEpisodeListTable = new PodcastEpisodeListTable(briteDatabase);
    }

    @Nullable
    public Identifier<PodcastSearch> insertPodcastSearch(PodcastSearch podcastSearch) {
        return podcastSearchTable.upsertPodcastSearch(podcastSearch);
    }

    public void replacePodcastSearchResults(
            Identifier<PodcastSearch> podcastSearchIdentifier,
            List<Podcast> podcasts
    ) {
        try (BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
            podcastSearchResultTable.deletePodcastSearchResultsByPodcastSearchIdentifier(podcastSearchIdentifier);

            final List<Optional<Identifier<Podcast>>> podcastIdentifierOptionals =
                    podcastTable.upsertPodcasts(podcasts);
            for (int i = 0; i < podcastIdentifierOptionals.size(); i++) {
                final Optional<Identifier<Podcast>> podcastIdentifierOptional =
                        podcastIdentifierOptionals.get(i);
                if (podcastIdentifierOptional.isPresent()) {
                    final Identifier<Podcast> podcastIdentifier = podcastIdentifierOptional.get();
                    podcastSearchResultTable.insertPodcastSearchResult(
                            podcastIdentifier,
                            podcastSearchIdentifier,
                            i
                    );
                }
            }

            transaction.markSuccessful();
        }
    }

    public Observable<List<Identified<Podcast>>> observeQueryForPodcastIdentifieds(
            PodcastSearch podcastSearch
    ) {
        return briteDatabase.createQuery(
                Arrays.asList(
                        PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT,
                        PodcastTable.TABLE_PODCAST,
                        PodcastSearchTable.TABLE_PODCAST_SEARCH
                ),
                "SELECT " + PodcastSearchResultTable.PODCAST_ID
                        + "FROM " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + " "
                +"WHERE ",
                null
        ).mapToList(PodcastTable::getPodcastIdentified);
    }

    static final class Schema extends Callback {
        Schema() {
            super(1);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");

            PodcastSearchTable.createPodcastSearchTable(db);
            PodcastTable.createPodcastTable(db);
            EpisodeTable.createEpisodeTable(db);
            PodcastSearchResultTable.createPodcastSearchResultTable(db);
            PodcastEpisodeListTable.createPodcastEpisodeListTable(db);
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
