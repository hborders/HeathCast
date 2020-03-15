package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.EpisodeIdentifiedOpt;
import com.github.hborders.heathcast.models.EpisodeIdentifiedSet;
import com.github.hborders.heathcast.models.EpisodeIdentifier;
import com.github.hborders.heathcast.models.EpisodeIdentifierOpt;
import com.github.hborders.heathcast.models.EpisodeIdentifierOptList;
import com.github.hborders.heathcast.models.EpisodeList;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putDateAsLong;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putDurationAsLong;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableDateFromLong;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableDurationFromLong;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableURLFromString;
import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.TABLE_PODCAST;

final class EpisodeTable<N> extends Table<N> {
    static final String TABLE_EPISODE = "episode";

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

    EpisodeTable(DimDatabase<N> dimDatabase) {
        super(dimDatabase);
    }

    EpisodeIdentifierOpt insertEpisode(
            PodcastIdentifier podcastIdentifier,
            Episode episode
    ) {
        final long id = dimDatabase.insert(
                TABLE_EPISODE,
                CONFLICT_ROLLBACK,
                getEpisodeContentValues(
                        podcastIdentifier,
                        episode
                )
        );
        if (id == -1) {
            return EpisodeIdentifierOpt.EMPTY;
        } else {
            return new EpisodeIdentifierOpt(new EpisodeIdentifier(id));
        }
    }

    int updateEpisodeIdentified(
            PodcastIdentifier podcastIdentifier,
            EpisodeIdentified episodeIdentified) {
        return dimDatabase.update(
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

    EpisodeIdentifierOptList upsertEpisodes(
            PodcastIdentifier podcastIdentifier,
            EpisodeList episodes
    ) {
        final UpsertAdapter<String> upsertAdapter = new EpisodeTableUpsertAdapter(podcastIdentifier);

        return super.upsertModels(
                upsertAdapter,
                String.class,
                episodes,
                episode -> episode.url.toExternalForm(),
                EpisodeIdentifier::new,
                EpisodeIdentified::new,
                episode -> insertEpisode(
                        podcastIdentifier,
                        episode
                ),
                episodeIdentified -> updateEpisodeIdentified(
                        podcastIdentifier,
                        episodeIdentified
                ),
                EpisodeIdentifierOpt.FACTORY,
                EpisodeIdentifierOptList::new
        );
    }

    int deleteEpisode(EpisodeIdentifier episodeIdentifier) {
        return dimDatabase.delete(
                TABLE_EPISODE,
                ID + " = ?",
                Long.toString(episodeIdentifier.id)
        );
    }

    int deleteEpisodes(Collection<EpisodeIdentifier> episodeIdentifiers) {
        final String[] idStrings = idStrings(episodeIdentifiers);
        return dimDatabase.delete(
                TABLE_EPISODE,
                ID + inPlaceholderClause(episodeIdentifiers.size()),
                idStrings
        );
    }

    Observable<EpisodeIdentifiedSet> observeQueryForAllEpisodeIdentifieds() {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_EPISODE)
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID)
                        .create();

        return dimDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToList(EpisodeTable::getEpisodeIdentified)
                .map(EpisodeIdentifiedSet::new);
    }

    Observable<EpisodeIdentifiedList> observeQueryForEpisodeIdentifiedsForPodcast(PodcastIdentifier podcastIdentifier) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder2
                        .builder(TABLE_EPISODE)
                        .selection(
                                PODCAST_ID + " = ?",
                                new Object[]{podcastIdentifier.id}
                        )
                        .orderBy(SORT)
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID)
                        .create();

        return dimDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToSpecificList(
                        EpisodeTable::getEpisodeIdentified,
                        EpisodeIdentifiedList::new
                );
    }

    Observable<EpisodeIdentifiedOpt> observeQueryForEpisodeIdentified(
            EpisodeIdentifier episodeIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_EPISODE)
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID)
                        .selection(
                                ID + "= ?",
                                new Object[]{episodeIdentifier.id}
                        ).create();

        return dimDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToOptional(EpisodeTable::getEpisodeIdentified)
                .map(EpisodeIdentifiedOpt.FACTORY::fromOptional);
    }

    static void createEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EPISODE + " ("
                + ARTWORK_URL + " TEXT, "
                + DURATION + " INTEGER, "
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_ID + " INTEGER NOT NULL, "
                + PUBLISH_TIME_MILLIS + " INTEGER, "
                + SORT + " INTEGER NOT NULL UNIQUE DEFAULT 0, "
                + SUMMARY + " TEXT, "
                + TITLE + " TEXT NOT NULL, "
                + URL + " TEXT NOT NULL, "
                + "UNIQUE("
                + "  " + PODCAST_ID + ", "
                + "  " + URL + " "
                + "), "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                + ")"
        );
        db.execSQL(
                "CREATE TRIGGER " + TABLE_EPISODE + "_sort_after_insert_trigger"
                        + "  AFTER INSERT ON " + TABLE_EPISODE + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + TABLE_EPISODE
                        + "      SET " + SORT + " = (" +
                        "          SELECT" +
                        "            IFNULL(MAX(" + SORT + "), 0) + 1 " +
                        "          FROM " + TABLE_EPISODE
                        + "      )"
                        + "      WHERE " + ID + " = NEW." + ID + ";"
                        + "    END"
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

    static EpisodeIdentified getEpisodeIdentified(Cursor cursor) {
        return new EpisodeIdentified(
                new EpisodeIdentifier(
                        getNonnullInt(
                                cursor,
                                ID
                        )
                ),
                new Episode(
                        getNullableURLFromString(
                                cursor,
                                ARTWORK_URL
                        ),
                        getNullableDurationFromLong(
                                cursor,
                                DURATION
                        ),
                        getNullableDateFromLong(
                                cursor,
                                PUBLISH_TIME_MILLIS
                        ),
                        getNullableString(
                                cursor,
                                SUMMARY
                        ),
                        getNonnullString(
                                cursor,
                                TITLE
                        ),
                        getNonnullURLFromString(
                                cursor,
                                URL
                        )
                )
        );
    }

    static ContentValues getEpisodeContentValues(
            PodcastIdentifier podcastIdentifier,
            Episode episode
    ) {
        final ContentValues values = new ContentValues(8);

        putURLAsString(
                values,
                ARTWORK_URL,
                episode.artworkURL
        );
        putDurationAsLong(
                values,
                DURATION,
                episode.duration
        );
        putIdentifier(
                values,
                PODCAST_ID,
                podcastIdentifier
        );
        putDateAsLong(
                values,
                PUBLISH_TIME_MILLIS,
                episode.publishDate
        );
        values.put(
                SUMMARY,
                episode.summary
        );
        values.put(
                TITLE,
                episode.title
        );
        putURLAsString(
                values,
                URL,
                episode.url
        );

        return values;
    }

    static ContentValues getEpisodeIdentifiedContentValues(
            PodcastIdentifier podcastIdentifier,
            EpisodeIdentified episodeIdentified
    ) {
        final ContentValues values = getEpisodeContentValues(
                podcastIdentifier,
                episodeIdentified.model
        );

        putIdentifier(
                values,
                ID,
                episodeIdentified
        );

        return values;
    }

    private static class EpisodeTableUpsertAdapter implements UpsertAdapter<String> {
        private final PodcastIdentifier podcastIdentifier;

        public EpisodeTableUpsertAdapter(PodcastIdentifier podcastIdentifier) {
            this.podcastIdentifier = podcastIdentifier;
        }

        @Override
        public SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<String> secondaryKeys) {
            final String selection =
                    URL + inPlaceholderClause(secondaryKeys.size())
                            + " AND " + PODCAST_ID + " = ? ";
            final Object[] bindArgs = new Object[secondaryKeys.size() + 1];
            secondaryKeys.toArray(bindArgs);
            bindArgs[secondaryKeys.size()] = podcastIdentifier.id;
            return SupportSQLiteQueryBuilder
                    .builder(TABLE_EPISODE)
                    .columns(new String[]{
                            ID,
                            URL
                    })
                    .selection(
                            selection,
                            bindArgs
                    )
                    .create();
        }

        @Override
        public long getPrimaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return CursorUtil.getNonnullLong(primaryAndSecondaryKeyCursor, ID);
        }

        @Override
        public String getSecondaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return CursorUtil.getNonnullString(primaryAndSecondaryKeyCursor, URL);
        }
    }
}
