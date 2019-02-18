package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.hborders.heathcast.core.Ranged;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.List;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putDurationAsLong;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableDurationFromLong;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableURLFromString;

final class EpisodeTable extends Table {
    private static final String TABLE_EPISODE = "episode";

    private static final String ARTWORK_URL = "artwork_url";
    private static final String DURATION = "duration";
    private static final String ID = "_id";
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String SORT = "sort";
    private static final String SUMMARY = "summary";
    private static final String TITLE = "title";
    private static final String URL = "url";

    static final String FOREIGN_KEY_EPISODE = TABLE_EPISODE + "_id";
    static final String CREATE_FOREIGN_KEY_EPISODE =
            "FOREIGN KEY(" + FOREIGN_KEY_EPISODE + ") REFERENCES " + TABLE_EPISODE + "(" + ID + ")";

    EpisodeTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
    }

    @Nullable
    List<Identifier<Episode>> leftOuterReplaceEpisodes(
            Identifier<Podcast> podcastIdentifier,
            List<Episode> episodes
    ) {

    }

    Ranged<Integer> findEpisodeSortRanged(
            Identifier<Podcast> podcastIdentifier,
            List<Episode> episodes
    ) {

    }

    static void createEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EPISODE + " ("
                + ARTWORK_URL + " TEXT, "
                + DURATION + " INTEGER, "
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_ID + " INTEGER NOT NULL, "
                + SORT + " INTEGER NOT NULL DEFAULT 0, "
                + SUMMARY + " TEXT, "
                + TITLE + " TEXT NOT NULL, "
                + URL + " TEXT NOT NULL, "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                + ")"
        );
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + PODCAST_ID
                + " ON " + TABLE_EPISODE + "(" + PODCAST_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + SORT
                + " ON " + TABLE_EPISODE + "(" + SORT + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + URL
                + " ON " + TABLE_EPISODE + "(" + URL + ")");
    }

    static Identified<Episode> getIdentifiedEpisode(Cursor cursor) {
        return new Identified<>(
                new Identifier<>(
                        Episode.class,
                        getNonnullInt(cursor, ID)
                ),
                new Episode(
                        getNullableURLFromString(cursor, ARTWORK_URL),
                        getNullableDurationFromLong(cursor, DURATION),
                        getNullableString(cursor, SUMMARY),
                        getNonnullString(cursor, TITLE),
                        getNonnullURLFromString(cursor, URL)
                )
        );
    }

    static ContentValues getEpisodeContentValues(Episode episode) {
        final ContentValues values = new ContentValues(7);

        putURLAsString(values, ARTWORK_URL, episode.artworkURL);
        putDurationAsLong(values, DURATION, episode.duration);
        values.put(SUMMARY, episode.summary);
        values.put(TITLE, episode.title);
        putURLAsString(values, URL, episode.url);

        return values;
    }

    static ContentValues getIdentifiedEpisodeContentValues(
            Identified<Episode> identifiedEpisode,
            @Nullable Identifier<Podcast> podcastIdentifier
    ) {
        final ContentValues values = getEpisodeContentValues(identifiedEpisode.model);

        putIdentifier(values, ID, identifiedEpisode);
        if (podcastIdentifier != null) {
            putIdentifier(values, PODCAST_ID, podcastIdentifier);
        }

        return values;
    }
}
