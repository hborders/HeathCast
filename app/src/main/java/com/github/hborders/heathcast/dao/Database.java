package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.content.Context;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import javax.annotation.Nullable;

import io.reactivex.Scheduler;

public final class Database {
    public final PodcastSearchTable mPodcastSearchTable;
    public final PodcastTable mPodcastTable;
    public final EpisodeTable mEpisodeTable;
    public final PodcastSearchResultTable mPodcastSearchResultTable;
    public final PodcastEpisodeListTable mPodcastEpisodeListTable;

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
        BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(
                supportSQLiteOpenHelper,
                scheduler
        );
        mPodcastSearchTable = new PodcastSearchTable(briteDatabase);
        mPodcastTable = new PodcastTable(briteDatabase);
        mEpisodeTable = new EpisodeTable(briteDatabase);
        mPodcastSearchResultTable = new PodcastSearchResultTable(briteDatabase);
        mPodcastEpisodeListTable = new PodcastEpisodeListTable(briteDatabase);
    }

    static void putIdentifier(ContentValues contentValues, String key, Identified<?> identified) {
        putIdentifier(contentValues, key, identified.mIdentifier);
    }

    static void putIdentifier(ContentValues contentValues, String key, Identifier<?> identifier) {
        contentValues.put(key, identifier.mId);
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
