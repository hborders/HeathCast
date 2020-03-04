package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.PodcastSearchIdentified;
import com.github.hborders.heathcast.models.PodcastSearchIdentifier;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;

final class PodcastSearchTable<N> extends Table<N> {
    static final String TABLE_PODCAST_SEARCH = "podcast_search";

    static final String ID = "_id";
    static final String SEARCH = "search";
    static final String SORT = "sort";

    static final String FOREIGN_KEY_PODCAST_SEARCH = TABLE_PODCAST_SEARCH + "_id";
    static final String CREATE_FOREIGN_KEY_PODCAST_SEARCH =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST_SEARCH + ") REFERENCES " + TABLE_PODCAST_SEARCH + "(" + ID + ")";

    private static final String[] COLUMNS_SORT = new String[]{
            SORT
    };
    private static final String[] COLUMNS_ALL_BUT_SORT = new String[]{
            ID,
            SEARCH,
    };
    private static final UpsertAdapter<String> upsertAdapter = new SingleColumnSecondaryKeyUpsertAdapter<>(
            TABLE_PODCAST_SEARCH,
            ID,
            SEARCH,
            cursor -> CursorUtil.getNonnullString(cursor, SEARCH)
    );

    PodcastSearchTable(DimDatabase<N> dimDatabase) {
        super(dimDatabase);
    }

    Optional<PodcastSearchIdentifier> upsertPodcastSearch(PodcastSearch podcastSearch) {
        return upsertModel(
                upsertAdapter,
                String.class,
                podcastSearch,
                PodcastSearch::getSearch,
                PodcastSearchIdentifier::new,
                PodcastSearchIdentified::new,
                this::insertPodcastSearch,
                this::updatePodcastSearchIdentified);
    }

    private Optional<PodcastSearchIdentifier> insertPodcastSearch(PodcastSearch podcastSearch) {
        final long id = dimDatabase.insert(
                TABLE_PODCAST_SEARCH,
                CONFLICT_ABORT,
                getPodcastSearchContentValues(podcastSearch)
        );
        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(new PodcastSearchIdentifier(id));
        }
    }

    private int updatePodcastSearchIdentified(
            PodcastSearchIdentified podcastSearchIdentified) {
        return dimDatabase.update(
                TABLE_PODCAST_SEARCH,
                CONFLICT_ABORT,
                getPodcastSearchIdentifiedContentValues(podcastSearchIdentified),
                ID + " = ?",
                Long.toString(podcastSearchIdentified.identifier.id)
        );
    }

    int deletePodcastSearchById(Identifier<PodcastSearch> podcastSearchIdentifier) {
        return dimDatabase.delete(
                TABLE_PODCAST_SEARCH,
                ID + " = ?",
                Long.toString(podcastSearchIdentifier.id)
        );
    }

    int deletePodcastSearchesByIds(Collection<Identifier<PodcastSearch>> podcastSearchIdentifiers) {
        final String[] idStrings = new String[podcastSearchIdentifiers.size()];
        int i = 0;
        for (Identifier<PodcastSearch> podcastSearchIdentifier : podcastSearchIdentifiers) {
            idStrings[i] = Long.toString(podcastSearchIdentifier.id);
            i++;
        }
        return dimDatabase.delete(
                TABLE_PODCAST_SEARCH,
                ID + inPlaceholderClause(podcastSearchIdentifiers.size()),
                idStrings
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
        try (final Cursor sortCursor = dimDatabase.query(query)) {
            if (sortCursor.moveToNext()) {
                return sortCursor.getLong(0);
            } else {
                return null;
            }
        }
    }

    Observable<List<PodcastSearchIdentified>> observeQueryForAllPodcastSearchIdentifieds() {
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

        return dimDatabase
                .createQuery(TABLE_PODCAST_SEARCH, query)
                .mapToList(PodcastSearchTable::getPodcastSearchIdentified);
    }

    Observable<Optional<PodcastSearchIdentified>> observeQueryForPodcastSearchIdentified(
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

        return dimDatabase
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
                "CREATE TRIGGER " + TABLE_PODCAST_SEARCH + "_sort_after_insert_trigger"
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
                "CREATE TRIGGER " + TABLE_PODCAST_SEARCH + "_sort_after_update_trigger"
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
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH + "__" + SEARCH
                + " ON " + TABLE_PODCAST_SEARCH + "(" + SEARCH + ")");
        db.execSQL("CREATE INDEX " + TABLE_PODCAST_SEARCH + "__" + SORT
                + " ON " + TABLE_PODCAST_SEARCH + "(" + SORT + ")");
    }

    static PodcastSearchIdentified getPodcastSearchIdentified(Cursor cursor) {
        return new PodcastSearchIdentified(
                new PodcastSearchIdentifier(
                        getNonnullInt(
                                cursor,
                                ID
                        )
                ),
                new PodcastSearch(
                        getNonnullString(
                                cursor,
                                SEARCH
                        )
                )
        );
    }

    static ContentValues getPodcastSearchContentValues(PodcastSearch podcastSearch) {
        final ContentValues values = new ContentValues(3);
        values.put(SEARCH, podcastSearch.search);
        return values;
    }

    static ContentValues getPodcastSearchIdentifiedContentValues(PodcastSearchIdentified identifiedPodcastSearch) {
        final ContentValues values = getPodcastSearchContentValues(identifiedPodcastSearch.model);

        putIdentifier(values, ID, identifiedPodcastSearch);

        return values;
    }
}
