package com.github.hborders.heathcast.dao;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.stealthmountain.sqldim.DimDatabase;

final class MetaTable<MarkerType> extends Table<MarkerType> {
    static final String TABLE_META = "meta";

    static final int EPISODE_TABLE_ID = 1;
    static final int PODCAST_TABLE_ID = 2;
    static final int PODCAST_SEARCH_TABLE_ID = 3;
    static final int PODCAST_SEARCH_RESULT_TABLE_ID = 4;
    static final int SUBSCRIPTION_TABLE_ID = 5;

    private static final String ID = "_id";
    private static final String TABLE_NAME = "table_name";
    private static final String VERSION = "version";

    static void createMetaTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_META + " ("
                + ID + " INTEGER NOT NULL PRIMARY KEY, " // not AUTOINCREMENT!
                + TABLE_NAME + " TEXT NOT NULL, "
                + VERSION + " INTEGER NOT NULL, "
                + "UNIQUE("
                + "  " + TABLE_NAME + " "
                + ") "
                + ")"
        );
        // Earlier SQLite versions don't support a simple syntax for inserting multiple rows at once
        // https://stackoverflow.com/a/5009740/9636
        db.execSQL(" INSERT INTO " + TABLE_META + " ( "
                + ID + ", " + TABLE_NAME + ", " + VERSION
                + " ) VALUES ( "
                + EPISODE_TABLE_ID + ", \"" + EpisodeTable.TABLE_EPISODE + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + ID + ", " + TABLE_NAME + ", " + VERSION
                + " ) VALUES ( "
                + PODCAST_TABLE_ID + ", \"" + PodcastTable.TABLE_PODCAST + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + ID + ", " + TABLE_NAME + ", " + VERSION
                + " ) VALUES ( "
                + PODCAST_SEARCH_TABLE_ID + ", \"" + PodcastSearchTable.TABLE_PODCAST_SEARCH + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + ID + ", " + TABLE_NAME + ", " + VERSION
                + " ) VALUES ( "
                + PODCAST_SEARCH_RESULT_TABLE_ID + ", \"" + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + ID + ", " + TABLE_NAME + ", " + VERSION
                + " ) VALUES ( "
                + SUBSCRIPTION_TABLE_ID + ", \"" + SubscriptionTable.TABLE_SUBSCRIPTION + "\", " + "1"
                + " ); "
        );
    }

    MetaTable(DimDatabase<MarkerType> dimDatabase) {
        super(dimDatabase);
    }
}
