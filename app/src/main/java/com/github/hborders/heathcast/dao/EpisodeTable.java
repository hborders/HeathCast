package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.utils.CursorUtil;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.TABLE_PODCAST;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putDateAsLong;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putDurationAsLong;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableDateFromLong;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableDurationFromLong;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableURLFromString;

final class EpisodeTable extends Table {
    private static final String TABLE_EPISODE = "episode";

    private static final String ARTWORK_URL = "artwork_url";
    private static final String DURATION = "duration";
    private static final String ID = "_id";
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String PUBLISH_TIME_MILLIS = "publish_times_millis";
    private static final String SORT = "sort";
    private static final String SUMMARY = "summary";
    private static final String TITLE = "title";
    private static final String URL = "url";

    private static final String[] COLUMNS_ALL_BUT_PODCAST_ID = new String[]{
            ARTWORK_URL,
            DURATION,
            ID,
            PUBLISH_TIME_MILLIS,
            SORT,
            SUMMARY,
            TITLE,
            URL,
    };

    static final String FOREIGN_KEY_EPISODE = TABLE_EPISODE + "_id";
    static final String CREATE_FOREIGN_KEY_EPISODE =
            "FOREIGN KEY(" + FOREIGN_KEY_EPISODE + ") REFERENCES " + TABLE_EPISODE + "(" + ID + ")";

    EpisodeTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
    }

    List<Optional<Identifier<Episode>>> upsertEpisodes(
            Identifier<Podcast> podcastIdentifier,
            List<Episode> episodes
    ) {
        return super.upsertModels(
                TABLE_EPISODE,
                ID,
                URL,
                Episode.class,
                episodes,
                episode -> episode.url.toExternalForm(),
                cursor -> CursorUtil.getNonnullURLFromString(
                        cursor,
                        URL
                ),
                episode -> insertEpisode(
                        podcastIdentifier,
                        episode
                ),
                episodeIdentified -> updateEpisodeIdentified(
                        podcastIdentifier,
                        episodeIdentified
                )
        );
    }

    Observable<Set<Identified<Episode>>> observeQueryForAllEpisodeIdentifieds() {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_EPISODE)
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID)
                        .create();

        return briteDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToList(EpisodeTable::getEpisodeIdentified)
                .map(HashSet::new);
    }

    Observable<Set<Identified<Episode>>> observeQueryForEpisodeIdentifiedsForPodcast(Identifier<Podcast> podcastIdentifier) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_EPISODE)
                        .selection(
                                PODCAST_ID + " = ?",
                                new Object[]{podcastIdentifier.id}
                        )
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID).create();

        return briteDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToList(EpisodeTable::getEpisodeIdentified)
                .map(HashSet::new);
    }

    Observable<Optional<Identified<Episode>>> observeQueryForEpisodeIdentified(
            Identifier<Episode> episodeIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_EPISODE)
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID)
                        .selection(
                                ID + "= ?",
                                new Object[]{episodeIdentifier.id}
                        ).create();

        return briteDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToOptional(EpisodeTable::getEpisodeIdentified);
    }

    private Optional<Identifier<Episode>> insertEpisode(
            Identifier<Podcast> podcastIdentifier,
            Episode episode
    ) {
        final long id = briteDatabase.insert(
                TABLE_EPISODE,
                CONFLICT_ROLLBACK,
                getEpisodeContentValues(
                        podcastIdentifier,
                        episode
                )
        );
        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(
                    new Identifier<>(
                            Episode.class,
                            id
                    )
            );
        }
    }

    private int updateEpisodeIdentified(
            Identifier<Podcast> podcastIdentifier,
            Identified<Episode> episodeIdentified) {
        return briteDatabase.update(
                TABLE_EPISODE,
                CONFLICT_ROLLBACK,
                getEpisodeIdentifiedContentValues(
                        podcastIdentifier,
                        episodeIdentified
                ),
                ID + " = ?",
                Long.toString(episodeIdentified.identifier.id)
        );
    }

    static void createEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EPISODE + " ("
                + ARTWORK_URL + " TEXT, "
                + DURATION + " INTEGER, "
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_ID + " INTEGER NOT NULL, "
                + PUBLISH_TIME_MILLIS + " INTEGER NOT NULL, "
                + SORT + " INTEGER NOT NULL DEFAULT 0, "
                + SUMMARY + " TEXT, "
                + TITLE + " TEXT NOT NULL, "
                + URL + " TEXT NOT NULL, "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                + ")"
        );
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + PODCAST_ID
                + " ON " + TABLE_EPISODE + "(" + PODCAST_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + PUBLISH_TIME_MILLIS
                + " ON " + TABLE_EPISODE + "(" + PUBLISH_TIME_MILLIS + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + SORT
                + " ON " + TABLE_EPISODE + "(" + SORT + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + URL
                + " ON " + TABLE_EPISODE + "(" + URL + ")");
    }

    static Identified<Episode> getEpisodeIdentified(Cursor cursor) {
        return new Identified<>(
                new Identifier<>(
                        Episode.class,
                        getNonnullInt(cursor, ID)
                ),
                new Episode(
                        getNullableURLFromString(cursor, ARTWORK_URL),
                        getNullableDurationFromLong(cursor, DURATION),
                        getNullableDateFromLong(cursor, PUBLISH_TIME_MILLIS),
                        getNullableString(cursor, SUMMARY),
                        getNonnullString(cursor, TITLE),
                        getNonnullURLFromString(cursor, URL)
                )
        );
    }

    static ContentValues getEpisodeContentValues(
            Identifier<Podcast> podcastIdentifier,
            Episode episode
    ) {
        final ContentValues values = new ContentValues(8);

        putURLAsString(values, ARTWORK_URL, episode.artworkURL);
        putDurationAsLong(values, DURATION, episode.duration);
        putIdentifier(values, PODCAST_ID, podcastIdentifier);
        putDateAsLong(values, PUBLISH_TIME_MILLIS, episode.publishDate);
        values.put(SUMMARY, episode.summary);
        values.put(TITLE, episode.title);
        putURLAsString(values, URL, episode.url);

        return values;
    }

    static ContentValues getEpisodeIdentifiedContentValues(
            Identifier<Podcast> podcastIdentifier,
            Identified<Episode> identifiedEpisode
    ) {
        final ContentValues values = getEpisodeContentValues(
                podcastIdentifier,
                identifiedEpisode.model
        );

        putIdentifier(values, ID, identifiedEpisode);

        return values;
    }
}