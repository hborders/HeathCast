package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.SupportSQLiteStatement;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.dao.Database.Schema.EpisodeTable.CREATE_FOREIGN_KEY_EPISODE;
import static com.github.hborders.heathcast.dao.Database.Schema.EpisodeTable.FOREIGN_KEY_EPISODE;
import static com.github.hborders.heathcast.dao.Database.Schema.EpisodeTable.createEpisodeTable;
import static com.github.hborders.heathcast.dao.Database.Schema.PodcastSearchTable.CREATE_FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.Database.Schema.PodcastSearchTable.FOREIGN_KEY_PODCAST_SEARCH;
import static com.github.hborders.heathcast.dao.Database.Schema.PodcastSearchTable.createPodcastSearchTable;
import static com.github.hborders.heathcast.dao.Database.Schema.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.Database.Schema.PodcastTable.FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.Database.Schema.PodcastTable.createPodcastTable;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putDurationAsLong;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableDurationFromLong;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableURLFromString;

public final class Database {
    private final BriteDatabase mBriteDatabase;

    public Database(
            Context context,
            @Nullable String name
    ) {
        final Configuration configuration = Configuration
                .builder(context)
                .callback(new Schema())
                .name(name)
                .build();
        final Factory factory = new FrameworkSQLiteOpenHelperFactory();
        final SupportSQLiteOpenHelper supportSQLiteOpenHelper = factory.create(configuration);
        final SqlBrite sqlBrite = new SqlBrite.Builder().build();
        this.mBriteDatabase = sqlBrite.wrapDatabaseHelper(supportSQLiteOpenHelper, Schedulers.io());
    }

    @Nullable
    public Identifier<PodcastSearch> insertPodcastSearch(PodcastSearch podcastSearch) {
        return Schema.PodcastSearchTable.insertPodcastSearch(mBriteDatabase, podcastSearch);
    }

    public Observable<Optional<Identified<PodcastSearch>>> observeQueryForPodcastSearch(
            Identifier<PodcastSearch> podcastSearchIdentifier
    ) {
        return Schema.PodcastSearchTable.observeQueryForPodcastSearch(mBriteDatabase, podcastSearchIdentifier);
    }

    private static void putIdentifier(ContentValues contentValues, String key, Identified<?> identified) {
        putIdentifier(contentValues, key, identified.mIdentifier);
    }

    private static void putIdentifier(ContentValues contentValues, String key, Identifier<?> identifier) {
        contentValues.put(key, identifier.mId);
    }

    static final class Schema extends Callback {
        Schema() {
            super(1);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");

            createPodcastSearchTable(db);
            createPodcastTable(db);
            createEpisodeTable(db);
        }

        @Override
        public void onUpgrade(
                SupportSQLiteDatabase db,
                int oldVersion,
                int newVersion
        ) {
            throw new AssertionError();
        }

        static final class PodcastSearchTable {
            private static final String TABLE_PODCAST_SEARCH = "podcast_search";

            private static final String ID = "_id";
            private static final String SEARCH = "search";

            static final String FOREIGN_KEY_PODCAST_SEARCH = TABLE_PODCAST_SEARCH + "_id";
            static final String CREATE_FOREIGN_KEY_PODCAST_SEARCH =
                    "FOREIGN KEY(" + FOREIGN_KEY_PODCAST_SEARCH + ") NOT NULL REFERENCES " + TABLE_PODCAST_SEARCH + "(" + ID + ")";

            private PodcastSearchTable() {
            }

            static void createPodcastSearchTable(SupportSQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + TABLE_PODCAST_SEARCH + " ("
                        + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                        + SEARCH + " TEXT NOT NULL "
                        + ")"
                );
            }

            @Nullable
            static Identifier<PodcastSearch> insertPodcastSearch(BriteDatabase briteDatabase, PodcastSearch podcastSearch) {
                final long id = briteDatabase.insert(
                        TABLE_PODCAST_SEARCH,
                        CONFLICT_ABORT,
                        getPodcastSearchContentValues(podcastSearch)
                );
                if (id == -1) {
                    return null;
                } else {
                    return new Identifier<PodcastSearch>(
                            PodcastSearch.class,
                            id
                    );
                }
            }

            static Observable<Optional<Identified<PodcastSearch>>> observeQueryForPodcastSearch(
                    BriteDatabase briteDatabase,
                    Identifier<PodcastSearch> podcastSearchIdentifier
            ) {
                final SupportSQLiteQuery query =
                        SupportSQLiteQueryBuilder
                                .builder(TABLE_PODCAST_SEARCH)
                                .columns(new String[]{
                                                ID,
                                                SEARCH
                                        }
                                )
                                .selection(
                                        ID + "= ?",
                                        new Object[]{
                                                podcastSearchIdentifier.mId
                                        }
                                ).create();

                return briteDatabase
                        .createQuery(TABLE_PODCAST_SEARCH, query)
                        .mapToOptional(PodcastSearchTable::getIdentifiedPodcastSearch);
            }

            static Identified<PodcastSearch> getIdentifiedPodcastSearch(Cursor cursor) {
                return new Identified<>(new Identifier<>(
                        PodcastSearch.class,
                        getNonnullInt(cursor, ID)
                ),
                        new PodcastSearch(
                                getNonnullString(cursor, SEARCH)
                        )
                );
            }

            static SupportSQLiteStatement compileInsertPodcastSearch(SupportSQLiteDatabase db) {
                return db.compileStatement("INSERT INTO " + TABLE_PODCAST_SEARCH + " ("
                        + SEARCH
                        + ") VALUES (?)");
            }

            static ContentValues getPodcastSearchContentValues(PodcastSearch podcastSearch) {
                final ContentValues values = new ContentValues(2);
                values.put(SEARCH, podcastSearch.mSearch);
                return values;
            }

            static ContentValues getIdentifiedPodcastSearchContentValues(Identified<PodcastSearch> identifiedPodcastSearch) {
                final ContentValues values = getPodcastSearchContentValues(identifiedPodcastSearch.mModel);

                putIdentifier(values, ID, identifiedPodcastSearch);

                return values;
            }
        }

        static final class PodcastSearchResultTable {
            private static final String TABLE_PODCAST_SEARCH_RESULT = "podcast_search_result";

            private static final String ID = "_id";
            private static final String PODCAST_SEARCH_ID = FOREIGN_KEY_PODCAST_SEARCH;
            private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;

            private PodcastSearchResultTable() {
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

        static final class PodcastTable {
            private static final String TABLE_PODCAST = "podcast";

            private static final String ARTWORK_URL = "artwork_url";
            private static final String AUTHOR = "author";
            private static final String FEED_URL = "feed_url";
            private static final String ID = "_id";
            private static final String NAME = "name";

            static final String FOREIGN_KEY_PODCAST = TABLE_PODCAST + "_id";
            static final String CREATE_FOREIGN_KEY_PODCAST =
                    "FOREIGN KEY(" + FOREIGN_KEY_PODCAST + ") REFERENCES " + TABLE_PODCAST + "(" + ID + ")";

            private PodcastTable() {
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
                putIdentifier(values, ID, identifiedPodcast);
                return values;
            }
        }

        static final class PodcastEpisodeList {
            private static final String TABLE_PODCAST_EPISODE_LIST = "podcast_episode_list";

            private static final String ID = "_id";
            private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
            private static final String EPISODE_ID = FOREIGN_KEY_EPISODE;

            private PodcastEpisodeList() {
            }

            static void createPodcastSearchResultTable(SupportSQLiteDatabase db) {
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

        static final class EpisodeTable {
            private static final String TABLE_EPISODE = "episode";

            private static final String ARTWORK_URL = "artwork_url";
            private static final String DURATION = "duration";
            private static final String ID = "_id";
            private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
            private static final String SUMMARY = "summary";
            private static final String TITLE = "title";
            private static final String URL = "url";

            static final String FOREIGN_KEY_EPISODE = TABLE_EPISODE + "_id";
            static final String CREATE_FOREIGN_KEY_EPISODE =
                    "FOREIGN KEY(" + FOREIGN_KEY_EPISODE + ") REFERENCES " + TABLE_EPISODE + "(" + ID + ")";

            private EpisodeTable() {
            }

            static void createEpisodeTable(SupportSQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + TABLE_EPISODE + " ("
                        + ARTWORK_URL + " TEXT, "
                        + DURATION + " INTEGER, "
                        + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                        + PODCAST_ID + " INTEGER NOT NULL, "
                        + SUMMARY + " TEXT, "
                        + TITLE + " TEXT NOT NULL, "
                        + URL + " TEXT NOT NULL, "
                        + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                        + ")"
                );
                db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + PODCAST_ID
                        + " ON " + TABLE_EPISODE + "(" + PODCAST_ID + ")");
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

                putURLAsString(values, ARTWORK_URL, episode.mArtworkURL);
                putDurationAsLong(values, DURATION, episode.mDuration);
                values.put(SUMMARY, episode.mSummary);
                values.put(TITLE, episode.mTitle);
                putURLAsString(values, URL, episode.mURL);

                return values;
            }

            static ContentValues getIdentifiedEpisodeContentValues(
                    Identified<Episode> identifiedEpisode,
                    @Nullable Identifier<Podcast> podcastIdentifier
            ) {
                final ContentValues values = getEpisodeContentValues(identifiedEpisode.mModel);

                putIdentifier(values, ID, identifiedEpisode);
                if (podcastIdentifier != null) {
                    putIdentifier(values, PODCAST_ID, podcastIdentifier);
                }

                return values;
            }
        }
    }
}
