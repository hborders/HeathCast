package com.github.hborders.heathcast.dao;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.squareup.sqlbrite3.BriteDatabase;

import static com.github.hborders.heathcast.dao.PodcastSearchTable.CREATE_FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.PodcastSearchTable.FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;

final class PodcastSearchResultTable {
    private static final String TABLE_PODCAST_SEARCH_RESULT = "podcast_search_result";

    private static final String ID = "_id";
    private static final String PODCAST_SEARCH_ID = FOREIGN_KEY_PODCAST_SEARCH;
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;

    private final BriteDatabase mBriteDatabase;

    PodcastSearchResultTable(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    static void createPodcastSearchResultTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST_SEARCH_RESULT + " ("
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_SEARCH_ID + " INTEGER NOT NULL, "
                + PODCAST_ID + " INTEGER NOT NULL, "
                + CREATE_FOREIGN_KEY_PODCAST_SEARCH + " ON DELETE CASCADE, "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE"
                + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH_RESULT + "__" + PODCAST_SEARCH_ID
                + " ON " + TABLE_PODCAST_SEARCH_RESULT + "(" + PODCAST_SEARCH_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH_RESULT + "__" + PODCAST_ID
                + " ON " + TABLE_PODCAST_SEARCH_RESULT + "(" + PODCAST_ID + ")");
    }
}
