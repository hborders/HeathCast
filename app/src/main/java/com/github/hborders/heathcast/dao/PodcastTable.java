package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.squareup.sqlbrite3.BriteDatabase;

import static com.github.hborders.heathcast.utils.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableURLFromString;

final class PodcastTable {
    private static final String TABLE_PODCAST = "podcast";

    private static final String ARTWORK_URL = "artwork_url";
    private static final String AUTHOR = "author";
    private static final String FEED_URL = "feed_url";
    private static final String ID = "_id";
    private static final String NAME = "name";

    static final String FOREIGN_KEY_PODCAST = TABLE_PODCAST + "_id";
    static final String CREATE_FOREIGN_KEY_PODCAST =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST + ") REFERENCES " + TABLE_PODCAST + "(" + ID + ")";

    private final BriteDatabase mBriteDatabase;

    PodcastTable(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    static void createPodcastTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST + " ("
                + ARTWORK_URL + " TEXT, "
                + AUTHOR + " TEXT, "
                + FEED_URL + " TEXT NOT NULL, "
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL "
                + ")"
        );
    }

    static Identified<Podcast> getIdentifiedPodcast(Cursor cursor) {
        return new Identified<>(
                new Identifier<>(
                        Podcast.class,
                        getNonnullInt(cursor, ID)
                ),
                new Podcast(
                        getNullableURLFromString(cursor, ARTWORK_URL),
                        getNullableString(cursor, AUTHOR),
                        getNonnullURLFromString(cursor, FEED_URL),
                        getNonnullString(cursor, NAME)
                )
        );
    }

    static ContentValues getPodcastContentValues(Podcast podcast) {
        final ContentValues values = new ContentValues(5);

        putURLAsString(values, ARTWORK_URL, podcast.mArtworkURL);
        values.put(AUTHOR, podcast.mAuthor);
        putURLAsString(values, FEED_URL, podcast.mFeedURL);
        values.put(NAME, podcast.mName);

        return values;
    }

    static ContentValues getIdentifiedPodcastContentValues(Identified<Podcast> identifiedPodcast) {
        final ContentValues values = getPodcastContentValues(identifiedPodcast.mModel);
        Database.putIdentifier(values, ID, identifiedPodcast);
        return values;
    }
}
