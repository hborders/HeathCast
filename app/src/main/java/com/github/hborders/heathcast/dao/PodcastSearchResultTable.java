package com.github.hborders.heathcast.dao;

import android.content.ContentValues;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.stealthmountain.sqldim.DimDatabase;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.dao.PodcastSearchTable.CREATE_FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.PodcastSearchTable.FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;

final class PodcastSearchResultTable<
        MarkerType,
        PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
        PodcastSearchIdentifierType extends PodcastSearch2.PodcastSearchIdentifier2
        > extends Table<MarkerType> {
    static final String TABLE_PODCAST_SEARCH_RESULT = "podcast_search_result";

    static final String ID = "_id";
    static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    static final String PODCAST_SEARCH_ID = FOREIGN_KEY_PODCAST_SEARCH;
    static final String SORT = "sort";

    private static final String[] COLUMNS_ALL_BUT_SORT = new String[]{
            ID,
            PODCAST_ID,
            PODCAST_SEARCH_ID
    };

    PodcastSearchResultTable(DimDatabase<MarkerType> dimDatabase) {
        super(dimDatabase);
    }

    long insertPodcastSearchResult(
            PodcastIdentifierType podcastIdentifier,
            PodcastSearchIdentifierType podcastSearchIdentifier,
            int sort
    ) {
        final ContentValues values = new ContentValues(3);
        values.put(
                PODCAST_ID,
                podcastIdentifier.getId()
        );
        values.put(
                PODCAST_SEARCH_ID,
                podcastSearchIdentifier.getId()
        );
        values.put(
                SORT,
                sort
        );

        return dimDatabase.insert(
                TABLE_PODCAST_SEARCH_RESULT,
                CONFLICT_ABORT,
                values
        );
    }

    int deletePodcastSearchResultsByPodcastSearchIdentifier(PodcastSearchIdentifierType podcastSearchIdentifier) {
        return dimDatabase.delete(
                TABLE_PODCAST_SEARCH_RESULT,
                PODCAST_SEARCH_ID + " = ?",
                Long.toString(podcastSearchIdentifier.getId())
        );
    }

    static void createPodcastSearchResultTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST_SEARCH_RESULT + " ("
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_ID + " INTEGER NOT NULL, "
                + PODCAST_SEARCH_ID + " INTEGER NOT NULL, "
                + SORT + " INTEGER NOT NULL, "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE, "
                + CREATE_FOREIGN_KEY_PODCAST_SEARCH + " ON DELETE CASCADE"
                + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH_RESULT + "__" + PODCAST_ID
                + " ON " + TABLE_PODCAST_SEARCH_RESULT + "(" + PODCAST_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH_RESULT + "__" + PODCAST_SEARCH_ID
                + " ON " + TABLE_PODCAST_SEARCH_RESULT + "(" + PODCAST_SEARCH_ID + ")");
    }
}
