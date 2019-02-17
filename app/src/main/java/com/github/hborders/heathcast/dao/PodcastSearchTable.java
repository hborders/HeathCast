package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.SqlUtil.inPlaceholderClause;

final class PodcastSearchTable extends Table {
    private static final String TABLE_PODCAST_SEARCH = "podcast_search";

    private static final String ID = "_id";
    private static final String SEARCH = "search";
    private static final String SORT = "sort";

    static final String FOREIGN_KEY_PODCAST_SEARCH = TABLE_PODCAST_SEARCH + "_id";
    static final String CREATE_FOREIGN_KEY_PODCAST_SEARCH =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST_SEARCH + ") REFERENCES " + TABLE_PODCAST_SEARCH + "(" + ID + ")";

    private static final String[] COLUMNS_ID = new String[]{
            ID
    };
    private static final String[] COLUMNS_SORT = new String[]{
            SORT
    };
    private static final String[] COLUMNS_ALL_BUT_SORT = new String[]{
            ID,
            SEARCH,
    };

    PodcastSearchTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
    }

    @Nullable
    Identifier<PodcastSearch> upsertPodcastSearch(PodcastSearch podcastSearch) {
        try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
            final SupportSQLiteQuery idQuery =
                    SupportSQLiteQueryBuilder
                            .builder(TABLE_PODCAST_SEARCH)
                            .columns(COLUMNS_ID)
                            .selection(
                                    SEARCH + " = ?",
                                    new Object[]{podcastSearch.search})
                            .create();
            try (final Cursor idCursor = briteDatabase.query(idQuery)) {
                @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier;
                if (idCursor.moveToNext()) {
                    podcastSearchIdentifier = new Identifier<>(
                            PodcastSearch.class,
                            idCursor.getLong(0)
                    );

                    updatePodcastSearchIdentified(
                            new Identified<>(
                                    podcastSearchIdentifier,
                                    podcastSearch
                            )
                    );
                } else {
                    podcastSearchIdentifier = insertPodcastSearch(podcastSearch);
                }

                transaction.markSuccessful();

                return podcastSearchIdentifier;
            }
        }
    }

    @Nullable
    private Identifier<PodcastSearch> insertPodcastSearch(PodcastSearch podcastSearch) {
        final long id = briteDatabase.insert(
                TABLE_PODCAST_SEARCH,
                CONFLICT_ABORT,
                getPodcastSearchContentValues(podcastSearch)
        );
        if (id == -1) {
            return null;
        } else {
            return new Identifier<>(
                    PodcastSearch.class,
                    id
            );
        }
    }

    private int updatePodcastSearchIdentified(
            Identified<PodcastSearch> podcastSearchIdentified) {
        return briteDatabase.update(
                TABLE_PODCAST_SEARCH,
                CONFLICT_ABORT,
                getPodcastSearchIdentifiedContentValues(podcastSearchIdentified),
                ID + " = ?",
                Long.toString(podcastSearchIdentified.identifier.id)
        );
    }

    int deletePodcastSearchById(Identifier<PodcastSearch> podcastSearchIdentifier) {
        return briteDatabase.delete(
                TABLE_PODCAST_SEARCH,
                ID + " = ?",
                Long.toString(podcastSearchIdentifier.id)
        );
    }

    int deletePodcastSearch(PodcastSearch podcastSearch) {
        return briteDatabase.delete(
                TABLE_PODCAST_SEARCH,
                SEARCH + " = ?",
                podcastSearch.search
        );
    }

    int deletePodcastSearchesByIds(Collection<Identifier<PodcastSearch>> podcastSearchIdentifiers) {
        final String[] idStrings = new String[podcastSearchIdentifiers.size()];
        int i = 0;
        for (Identifier<PodcastSearch> podcastSearchIdentifier : podcastSearchIdentifiers) {
            idStrings[i] = Long.toString(podcastSearchIdentifier.id);
            i++;
        }
        return briteDatabase.delete(
                TABLE_PODCAST_SEARCH,
                ID + inPlaceholderClause(podcastSearchIdentifiers.size()),
                idStrings
        );
    }

    int deletePodcastSearches(Collection<PodcastSearch> podcastSearches) {
        final String[] searches = new String[podcastSearches.size()];
        int i = 0;
        for (PodcastSearch podcastSearch : podcastSearches) {
            searches[i] = podcastSearch.search;
            i++;
        }
        return briteDatabase.delete(
                TABLE_PODCAST_SEARCH,
                SEARCH + inPlaceholderClause(podcastSearches.size()),
                searches
        );
    }

    @Nullable
    Long sortForPodcastSearch(Identifier<PodcastSearch> podcastSearchIdentifier) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder2
                        .builder(TABLE_PODCAST_SEARCH)
                        .columns(COLUMNS_SORT)
                        .selection(
                                ID + " = ?",
                                new Object[]{podcastSearchIdentifier.id})
                        .create();
        try (final Cursor sortCursor = briteDatabase.query(query)) {
            if (sortCursor.moveToNext()) {
                return sortCursor.getLong(0);
            } else {
                return null;
            }
        }
    }

    Observable<List<Identified<PodcastSearch>>> observeQueryForAllPodcastSearchIdentifieds() {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder2
                        .builder(TABLE_PODCAST_SEARCH)
                        // including UPDATE_DATE didn't help
                        .columns(COLUMNS_ALL_BUT_SORT)
                        .orderBy(
                                SORT,
                                true
                        )
                        .create();

        return briteDatabase
                .createQuery(TABLE_PODCAST_SEARCH, query)
                .mapToList(PodcastSearchTable::getPodcastSearchIdentified);
    }

    Observable<Optional<Identified<PodcastSearch>>> observeQueryForPodcastSearchIdentified(
            Identifier<PodcastSearch> podcastSearchIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST_SEARCH)
                        .columns(COLUMNS_ALL_BUT_SORT)
                        .selection(
                                ID + "= ?",
                                new Object[]{podcastSearchIdentifier.id}
                        ).create();

        return briteDatabase
                .createQuery(TABLE_PODCAST_SEARCH, query)
                .mapToOptional(PodcastSearchTable::getPodcastSearchIdentified);
    }

    static void createPodcastSearchTable(SupportSQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_PODCAST_SEARCH + " ("
                        + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                        + SEARCH + " TEXT NOT NULL UNIQUE, "
                        + SORT + " INTEGER NOT NULL UNIQUE DEFAULT 0"
                        + ")"
        );
        db.execSQL(
                "CREATE TRIGGER sort_after_insert_trigger"
                        + "  AFTER INSERT ON " + TABLE_PODCAST_SEARCH + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + TABLE_PODCAST_SEARCH
                        + "      SET " + SORT + " = (" +
                        "          SELECT" +
                        "            IFNULL(MAX(" + SORT + "), 0) + 1 " +
                        "          FROM " + TABLE_PODCAST_SEARCH
                        + "      )"
                        + "      WHERE " + ID + " = NEW." + ID + ";"
                        + "    END"
        );
        db.execSQL(
                "CREATE TRIGGER sort_after_update_trigger"
                        + "  AFTER UPDATE ON " + TABLE_PODCAST_SEARCH + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + TABLE_PODCAST_SEARCH
                        + "      SET " + SORT + " = (" +
                        "          SELECT" +
                        "            MAX(" + SORT + ") + 1 " +
                        "          FROM " + TABLE_PODCAST_SEARCH
                        + "      )"
                        + "      WHERE " + ID + " = OLD." + ID + " AND" +
                        "          " + SORT + " != (" +
                        "            SELECT MAX(" + SORT + ") " +
                        "            FROM " + TABLE_PODCAST_SEARCH +
                        "          );"
                        + "    END"
        );
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH + "__" + SORT
                + " ON " + TABLE_PODCAST_SEARCH + "(" + SORT + ")");
    }

    static Identified<PodcastSearch> getPodcastSearchIdentified(Cursor cursor) {
        return new Identified<>(new Identifier<>(
                PodcastSearch.class,
                getNonnullInt(cursor, ID)
        ),
                new PodcastSearch(
                        getNonnullString(cursor, SEARCH)
                )
        );
    }

    static ContentValues getPodcastSearchContentValues(PodcastSearch podcastSearch) {
        final ContentValues values = new ContentValues(3);
        values.put(SEARCH, podcastSearch.search);
        return values;
    }

    static ContentValues getPodcastSearchIdentifiedContentValues(Identified<PodcastSearch> identifiedPodcastSearch) {
        final ContentValues values = getPodcastSearchContentValues(identifiedPodcastSearch.model);

        putIdentifier(values, ID, identifiedPodcastSearch);

        return values;
    }
}
