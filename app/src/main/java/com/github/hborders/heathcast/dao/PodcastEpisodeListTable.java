package com.github.hborders.heathcast.dao;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.squareup.sqlbrite3.BriteDatabase;

import static com.github.hborders.heathcast.dao.EpisodeTable.CREATE_FOREIGN_KEY_EPISODE;
import static com.github.hborders.heathcast.dao.EpisodeTable.FOREIGN_KEY_EPISODE;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;

final class PodcastEpisodeListTable {
    private static final String TABLE_PODCAST_EPISODE_LIST = "podcast_episode_list";

    private static final String ID = "_id";
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String EPISODE_ID = FOREIGN_KEY_EPISODE;

    private final BriteDatabase mBriteDatabase;

    PodcastEpisodeListTable(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    static void createPodcastEpisodeListTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST_EPISODE_LIST + " ("
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_ID + " INTEGER NOT NULL, "
                + EPISODE_ID + " INTEGER NOT NULL, "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE, "
                + CREATE_FOREIGN_KEY_EPISODE + " ON DELETE CASCADE"
                + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_EPISODE_LIST + "__" + PODCAST_ID
                + " ON " + TABLE_PODCAST_EPISODE_LIST + "(" + PODCAST_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_EPISODE_LIST + "__" + EPISODE_ID
                + " ON " + TABLE_PODCAST_EPISODE_LIST + "(" + EPISODE_ID + ")");
    }
}
