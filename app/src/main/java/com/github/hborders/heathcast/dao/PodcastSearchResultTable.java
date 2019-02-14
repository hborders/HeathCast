package com.github.hborders.heathcast.dao;

import android.content.ContentValues;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.squareup.sqlbrite3.BriteDatabase;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.dao.PodcastSearchTable.CREATE_FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.PodcastSearchTable.FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;

final class PodcastSearchResultTable extends Table {
    private static final String TABLE_PODCAST_SEARCH_RESULT = "podcast_search_result";

    private static final String ID = "_id";
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String PODCAST_SEARCH_ID = FOREIGN_KEY_PODCAST_SEARCH;
    private static final String SORT = "sort";

    PodcastSearchResultTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
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

    final long insertPodcastSearchResult(
            Identifier<Podcast> podcast,
            Identifier<PodcastSearch> podcastSearchIdentifier,
            int sort
    ) {
        final ContentValues values = new ContentValues(3);
        values.put(PODCAST_ID, podcast.id);
        values.put(PODCAST_SEARCH_ID, podcastSearchIdentifier.id);
        values.put(SORT, sort);

        return briteDatabase.insert(
                TABLE_PODCAST_SEARCH_RESULT,
                CONFLICT_ABORT,
                values
        );
    }
}
