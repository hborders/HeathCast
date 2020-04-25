package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Collection;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;

final class PodcastSearchTable<
        MarkerType,
        PodcastSearchType extends PodcastSearch,
        PodcastSearchIdentifiedType extends PodcastSearch.PodcastSearchIdentified<
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
        PodcastSearchIdentifiedListType extends PodcastSearch.PodcastSearchIdentified.PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType,
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifierType extends PodcastSearch.PodcastSearchIdentifier,
        PodcastSearchIdentifierOptType extends PodcastSearch.PodcastSearchIdentifier.PodcastSearchIdentifierOpt<
                        PodcastSearchIdentifierType
                        >
        > extends Table<MarkerType> {
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

    private final PodcastSearch.PodcastSearchFactory2<PodcastSearchType> podcastSearchFactory;
    private final Identifier.IdentifierFactory2<PodcastSearchIdentifierType> podcastSearchIdentifierFactory;
    private final Identified.IdentifiedFactory2<
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifierType,
            PodcastSearchType
            > podcastSearchIdentifiedFactory;
    private final CollectionFactory.Capacity<
            PodcastSearchIdentifiedListType,
            PodcastSearchIdentifiedType
            > podcastSearchIdentifiedListCapacityFactory;
    private final Opt.OptEmptyFactory<
            PodcastSearchIdentifierOptType,
            PodcastSearchIdentifierType
            > podcastSearchIdentifierOptEmptyFactory;
    private final Opt.OptNonEmptyFactory<
            PodcastSearchIdentifierOptType,
            PodcastSearchIdentifierType
            > podcastSearchIdentifierOptNonEmptyFactory;

    PodcastSearchTable(
            DimDatabase<MarkerType> dimDatabase,
            PodcastSearch.PodcastSearchFactory2<PodcastSearchType> podcastSearchFactory,
            Identifier.IdentifierFactory2<PodcastSearchIdentifierType> podcastSearchIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifierType,
                    PodcastSearchType
                    > podcastSearchIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedListCapacityFactory,
            Opt.OptEmptyFactory<
                    PodcastSearchIdentifierOptType,
                    PodcastSearchIdentifierType
                    > podcastSearchIdentifierOptEmptyFactory,
            Opt.OptNonEmptyFactory<
                    PodcastSearchIdentifierOptType,
                    PodcastSearchIdentifierType
                    > podcastSearchIdentifierOptNonEmptyFactory
    ) {
        super(dimDatabase);

        this.podcastSearchFactory = podcastSearchFactory;
        this.podcastSearchIdentifierFactory = podcastSearchIdentifierFactory;
        this.podcastSearchIdentifiedFactory = podcastSearchIdentifiedFactory;
        this.podcastSearchIdentifiedListCapacityFactory = podcastSearchIdentifiedListCapacityFactory;
        this.podcastSearchIdentifierOptEmptyFactory = podcastSearchIdentifierOptEmptyFactory;
        this.podcastSearchIdentifierOptNonEmptyFactory = podcastSearchIdentifierOptNonEmptyFactory;
    }

    PodcastSearchIdentifierOptType upsertPodcastSearch(PodcastSearchType podcastSearch) {
        return upsertModel2(
                upsertAdapter,
                String.class,
                podcastSearch,
                PodcastSearch::getSearch,
                podcastSearchIdentifierFactory,
                podcastSearchIdentifiedFactory,
                this::insertPodcastSearch,
                this::updatePodcastSearchIdentified,
                podcastSearchIdentifierOptEmptyFactory,
                podcastSearchIdentifierOptNonEmptyFactory
        );
    }

    private PodcastSearchIdentifierOptType insertPodcastSearch(PodcastSearchType podcastSearch) {
        final long id = dimDatabase.insert(
                TABLE_PODCAST_SEARCH,
                CONFLICT_ABORT,
                getPodcastSearchContentValues(podcastSearch)
        );
        if (id == -1) {
            return podcastSearchIdentifierOptEmptyFactory.newOpt();
        } else {
            return podcastSearchIdentifierOptNonEmptyFactory.newOpt(
                    podcastSearchIdentifierFactory.newIdentifier(id)
            );
        }
    }

    private int updatePodcastSearchIdentified(
            PodcastSearchIdentifiedType podcastSearchIdentified) {
        return dimDatabase.update(
                TABLE_PODCAST_SEARCH,
                CONFLICT_ABORT,
                getPodcastSearchIdentifiedContentValues(podcastSearchIdentified),
                ID + " = ?",
                Long.toString(podcastSearchIdentified.getIdentifier().getId())
        );
    }

    int deletePodcastSearchById(PodcastSearchIdentifierType podcastSearchIdentifier) {
        return dimDatabase.delete(
                TABLE_PODCAST_SEARCH,
                ID + " = ?",
                Long.toString(podcastSearchIdentifier.getId())
        );
    }

    int deletePodcastSearchesByIds(Collection<PodcastSearchIdentifierType> podcastSearchIdentifiers) {
        final String[] idStrings = new String[podcastSearchIdentifiers.size()];
        int i = 0;
        for (PodcastSearchIdentifierType podcastSearchIdentifier : podcastSearchIdentifiers) {
            idStrings[i] = Long.toString(podcastSearchIdentifier.getId());
            i++;
        }
        return dimDatabase.delete(
                TABLE_PODCAST_SEARCH,
                ID + inPlaceholderClause(podcastSearchIdentifiers.size()),
                idStrings
        );
    }

    @Nullable
    Long sortForPodcastSearch(PodcastSearchIdentifierType podcastSearchIdentifier) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder2
                        .builder(TABLE_PODCAST_SEARCH)
                        .columns(COLUMNS_SORT)
                        .selection(
                                ID + " = ?",
                                new Object[]{podcastSearchIdentifier.getId()})
                        .create();
        try (final Cursor sortCursor = dimDatabase.query(query)) {
            if (sortCursor.moveToNext()) {
                return sortCursor.getLong(0);
            } else {
                return null;
            }
        }
    }

    Observable<PodcastSearchIdentifiedListType> observeQueryForAllPodcastSearchIdentifieds() {
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
                .mapToSpecificList(
                        this::getPodcastSearchIdentified,
                        podcastSearchIdentifiedListCapacityFactory::newCollection
                );
    }

    Observable<Optional<PodcastSearchIdentifiedType>> observeQueryForPodcastSearchIdentified(
            PodcastSearchIdentifierType podcastSearchIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST_SEARCH)
                        .columns(COLUMNS_ALL_BUT_SORT)
                        .selection(
                                ID + "= ?",
                                new Object[]{podcastSearchIdentifier.getId()}
                        ).create();

        return dimDatabase
                .createQuery(TABLE_PODCAST_SEARCH, query)
                .mapToOptional(this::getPodcastSearchIdentified);
    }

    PodcastSearchIdentifiedType getPodcastSearchIdentified(Cursor cursor) {
        return podcastSearchIdentifiedFactory.newIdentified(
                podcastSearchIdentifierFactory.newIdentifier(
                        getNonnullInt(
                                cursor,
                                ID
                        )
                ),
                podcastSearchFactory.newPodcastSearch(
                        getNonnullString(
                                cursor,
                                SEARCH
                        )
                )
        );
    }

    ContentValues getPodcastSearchContentValues(PodcastSearchType podcastSearch) {
        final ContentValues values = new ContentValues(3);
        values.put(
                SEARCH,
                podcastSearch.getSearch()
        );
        return values;
    }

    ContentValues getPodcastSearchIdentifiedContentValues(PodcastSearchIdentifiedType podcastSearchIdentified) {
        final ContentValues values = getPodcastSearchContentValues(podcastSearchIdentified.getModel());

        putIdentifier2(
                values,
                ID,
                podcastSearchIdentified
        );

        return values;
    }
}
