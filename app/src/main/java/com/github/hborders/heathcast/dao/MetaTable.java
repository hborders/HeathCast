package com.github.hborders.heathcast.dao;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.stealthmountain.sqldim.DimDatabase;

final class MetaTable<MarkerType> extends Table<MarkerType> {
    static final String TABLE_META = "meta";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_TABLE_NAME = "table_name";
    static final String COLUMN_VERSION = "version";

    static final int ID_EPISODE_TABLE = 1;
    static final int ID_PODCAST_TABLE = 2;
    static final int ID_PODCAST_SEARCH_TABLE = 3;
    static final int ID_PODCAST_SEARCH_RESULT_TABLE = 4;
    static final int ID_SUBSCRIPTION_TABLE = 5;

    static void createMetaTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_META + " ("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, " // not AUTOINCREMENT!
                + COLUMN_TABLE_NAME + " TEXT NOT NULL, "
                + COLUMN_VERSION + " INTEGER NOT NULL, "
                + "UNIQUE("
                + "  " + COLUMN_TABLE_NAME + " "
                + ") "
                + ")"
        );
        // Earlier SQLite versions don't support a simple syntax for inserting multiple rows at once
        // https://stackoverflow.com/a/5009740/9636
        db.execSQL(" INSERT INTO " + TABLE_META + " ( "
                + COLUMN_ID + ", " + COLUMN_TABLE_NAME + ", " + COLUMN_VERSION
                + " ) VALUES ( "
                + ID_EPISODE_TABLE + ", \"" + EpisodeTable.TABLE_EPISODE + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + COLUMN_ID + ", " + COLUMN_TABLE_NAME + ", " + COLUMN_VERSION
                + " ) VALUES ( "
                + ID_PODCAST_TABLE + ", \"" + PodcastTable.TABLE_PODCAST + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + COLUMN_ID + ", " + COLUMN_TABLE_NAME + ", " + COLUMN_VERSION
                + " ) VALUES ( "
                + ID_PODCAST_SEARCH_TABLE + ", \"" + PodcastSearchTable.TABLE_PODCAST_SEARCH + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + COLUMN_ID + ", " + COLUMN_TABLE_NAME + ", " + COLUMN_VERSION
                + " ) VALUES ( "
                + ID_PODCAST_SEARCH_RESULT_TABLE + ", \"" + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "\", " + "1"
                + " ); "
                + " INSERT INTO " + TABLE_META + " ( "
                + COLUMN_ID + ", " + COLUMN_TABLE_NAME + ", " + COLUMN_VERSION
                + " ) VALUES ( "
                + ID_SUBSCRIPTION_TABLE + ", \"" + SubscriptionTable.TABLE_SUBSCRIPTION + "\", " + "1"
                + " ); "
        );
    }

    static void createUpdateVersionTriggers(
            SupportSQLiteDatabase db,
            String tableName,
            int id
    ) {
        db.execSQL(
                "CREATE TRIGGER " + tableName + "_version_after_insert_trigger"
                        + "  AFTER INSERT ON " + tableName + " FOR EACH ROW "
                        + " BEGIN"
                        + " UPDATE " + TABLE_META
                        + " SET " + COLUMN_VERSION + " = ("
                        + "   SELECT MAX(" + COLUMN_VERSION + ") + 1 "
                        + "   FROM " + TABLE_META
                        + "   WHERE " + COLUMN_ID + " = " + id
                        + " )"
                        + " WHERE " + COLUMN_ID + " = " + id + ";"
                        + " END"
        );

        db.execSQL(
                "CREATE TRIGGER " + tableName + "_version_after_update_trigger"
                        + "  AFTER UPDATE ON " + tableName + " FOR EACH ROW "
                        + " BEGIN"
                        + " UPDATE " + TABLE_META
                        + " SET " + COLUMN_VERSION + " = ("
                        + "   SELECT MAX(" + COLUMN_VERSION + ") + 1 "
                        + "   FROM " + TABLE_META
                        + "   WHERE " + COLUMN_ID + " = " + id
                        + " )"
                        + " WHERE " + COLUMN_ID + " = " + id + ";"
                        + " END"
        );

        db.execSQL(
                "CREATE TRIGGER " + tableName + "_version_after_delete_trigger"
                        + "  AFTER DELETE ON " + tableName + " FOR EACH ROW "
                        + " BEGIN"
                        + " UPDATE " + TABLE_META
                        + " SET " + COLUMN_VERSION + " = ("
                        + "   SELECT MAX(" + COLUMN_VERSION + ") + 1 "
                        + "   FROM " + TABLE_META
                        + "   WHERE " + COLUMN_ID + " = " + id
                        + " )"
                        + " WHERE " + COLUMN_ID + " = " + id + ";"
                        + " END"
        );
    }

    MetaTable(DimDatabase<MarkerType> dimDatabase) {
        super(dimDatabase);
    }
}
