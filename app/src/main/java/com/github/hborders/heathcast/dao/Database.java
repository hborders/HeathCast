package com.github.hborders.heathcast.dao;

import android.content.Context;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.List;

import javax.annotation.Nullable;

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

    public void replacePodcastSearchResults(
            PodcastSearch podcastSearch,
            List<Podcast> podcasts
    ) {
        briteDatabase.getWritableDatabase().beginTransaction();
        podcastSearchTable.insertPodcastSearch(podcastSearch);
        for (Podcast podcast : podcasts) {

        }

        briteDatabase.getWritableDatabase().endTransaction();
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
