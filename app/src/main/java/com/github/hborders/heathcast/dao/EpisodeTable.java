package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Opt2;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
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

final class EpisodeTable<
        MarkerType,
        EpisodeIdentifiedType extends Episode2.EpisodeIdentified2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode2.EpisodeIdentifier2,
        EpisodeType extends Episode2,
        EpisodeIdentifiedListType extends Episode2.EpisodeIdentified2.EpisodeIdentifiedList2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedSetType extends Episode2.EpisodeIdentified2.EpisodeIdentifiedSet2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierOptListType extends Episode2.EpisodeIdentifier2.EpisodeIdentifierOpt2.EpisodeIdentifierOptList2<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierOptType extends Episode2.EpisodeIdentifier2.EpisodeIdentifierOpt2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeListType extends EpisodeList2<EpisodeType>,
        PodcastIdentifierType extends Podcast2.PodcastIdentifier2
        > extends Table<MarkerType> {
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

    private final Episode2.EpisodeFactory2<EpisodeType> episodeFactory2;
    private final Identifier2.IdentifierFactory2<
            EpisodeIdentifierType
            > episodeIdentifierFactory;
    private final Identified2.IdentifiedFactory2<
            EpisodeIdentifiedType,
            EpisodeIdentifierType,
            EpisodeType
            > episodeIdentifiedFactory;
    private final Opt2.OptEmptyFactory<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType
                > episodeIdentifierOptEmptyFactory;
    private final Opt2.OptNonEmptyFactory<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType
                > episodeIdentifierOptNonEmptyFactory;
    private final CollectionFactory.Capacity<
            EpisodeIdentifiedListType,
            EpisodeIdentifiedType
            > episodeIdentifiedListCapacityFactory;
    private final CollectionFactory.Collection<
            EpisodeIdentifiedSetType,
            EpisodeIdentifiedType
            > episodeIdentifiedSetCollectionFactory;
    private final CollectionFactory.Capacity<
            EpisodeIdentifierOptListType,
            EpisodeIdentifierOptType
            > episodeIdentifierOptListCapacityFactory;

    EpisodeTable(
            Episode2.EpisodeFactory2<EpisodeType> episodeFactory2,
            Identifier2.IdentifierFactory2<
                    EpisodeIdentifierType
                    > episodeIdentifierFactory,
            Identified2.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            Opt2.OptEmptyFactory<
                                EpisodeIdentifierOptType,
                                EpisodeIdentifierType
                                > episodeIdentifierOptEmptyFactory,
            Opt2.OptNonEmptyFactory<
                                EpisodeIdentifierOptType,
                                EpisodeIdentifierType
                                > episodeIdentifierOptNonEmptyFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListCapacityFactory,
            CollectionFactory.Collection<
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedSetCollectionFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifierOptListType,
                    EpisodeIdentifierOptType
                    > episodeIdentifierOptListCapacityFactory,
            DimDatabase<MarkerType> dimDatabase) {
        super(dimDatabase);

        this.episodeFactory2 = episodeFactory2;
        this.episodeIdentifierFactory = episodeIdentifierFactory;
        this.episodeIdentifiedFactory = episodeIdentifiedFactory;
        this.episodeIdentifierOptEmptyFactory = episodeIdentifierOptEmptyFactory;
        this.episodeIdentifierOptNonEmptyFactory = episodeIdentifierOptNonEmptyFactory;
        this.episodeIdentifiedListCapacityFactory = episodeIdentifiedListCapacityFactory;
        this.episodeIdentifiedSetCollectionFactory = episodeIdentifiedSetCollectionFactory;
        this.episodeIdentifierOptListCapacityFactory = episodeIdentifierOptListCapacityFactory;
    }

    EpisodeIdentifierOptType insertEpisode(
            PodcastIdentifierType podcastIdentifier,
            EpisodeType episode
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
            return episodeIdentifierOptEmptyFactory.newOpt();
        } else {
            return episodeIdentifierOptNonEmptyFactory.newOpt(
                    episodeIdentifierFactory.newIdentifier(id)
            );
        }
    }

    int updateEpisodeIdentified(
            PodcastIdentifierType podcastIdentifier,
            EpisodeIdentifiedType episodeIdentified) {
        return dimDatabase.update(
                TABLE_EPISODE,
                CONFLICT_ROLLBACK,
                getEpisodeIdentifiedContentValues(
                        podcastIdentifier,
                        episodeIdentified
                ),
                ID + " = ?",
                Long.toString(episodeIdentified.getIdentifier().getId())
        );
    }

    EpisodeIdentifierOptListType upsertEpisodes(
            PodcastIdentifierType podcastIdentifier,
            EpisodeListType episodes
    ) {
        final UpsertAdapter<String> upsertAdapter = new EpisodeTableUpsertAdapter<>(podcastIdentifier);

        return super.upsertModels2(
                upsertAdapter,
                String.class,
                episodes,
                modelType ->
                        modelType.getURL().toExternalForm(),
                episodeIdentifierFactory,
                episodeIdentifiedFactory,
                episode ->
                        insertEpisode(
                                podcastIdentifier,
                                episode

                        ),
                episodeIdentified ->
                        updateEpisodeIdentified(
                                podcastIdentifier,
                                episodeIdentified
                        ),
                episodeIdentifierOptEmptyFactory,
                episodeIdentifierOptNonEmptyFactory,
                episodeIdentifierOptListCapacityFactory
        );
    }

    int deleteEpisode(EpisodeIdentifierType episodeIdentifier) {
        return dimDatabase.delete(
                TABLE_EPISODE,
                ID + " = ?",
                Long.toString(episodeIdentifier.getId())
        );
    }

    int deleteEpisodes(Collection<EpisodeIdentifierType> episodeIdentifiers) {
        final String[] idStrings = idStrings2(episodeIdentifiers);
        return dimDatabase.delete(
                TABLE_EPISODE,
                ID + inPlaceholderClause(episodeIdentifiers.size()),
                idStrings
        );
    }

    Observable<EpisodeIdentifiedSetType> observeQueryForAllEpisodeIdentifieds() {
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
                .mapToList(this::getEpisodeIdentified)
                .map(episodeIdentifiedSetCollectionFactory::newCollection);
    }

    Observable<EpisodeIdentifiedListType> observeQueryForEpisodeIdentifiedsForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder2
                        .builder(TABLE_EPISODE)
                        .selection(
                                PODCAST_ID + " = ?",
                                new Object[]{podcastIdentifier.getId()}
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
                        this::getEpisodeIdentified,
                        episodeIdentifiedListCapacityFactory::newCollection
                );
    }

    Observable<Optional<EpisodeIdentifiedType>> observeQueryForEpisodeIdentified(
            EpisodeIdentifierType episodeIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_EPISODE)
                        .columns(COLUMNS_ALL_BUT_PODCAST_ID)
                        .selection(
                                ID + "= ?",
                                new Object[]{episodeIdentifier.getId()}
                        ).create();

        return dimDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        ),
                        query
                )
                .mapToOptional(this::getEpisodeIdentified);
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

    EpisodeIdentifiedType getEpisodeIdentified(Cursor cursor) {
        return episodeIdentifiedFactory.newIdentified(
                episodeIdentifierFactory.newIdentifier(
                        getNonnullInt(
                                cursor,
                                ID
                        )
                ),
                episodeFactory2.newEpisode(
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

    ContentValues getEpisodeContentValues(
            PodcastIdentifierType podcastIdentifier,
            EpisodeType episode
    ) {
        final ContentValues values = new ContentValues(8);

        putURLAsString(
                values,
                ARTWORK_URL,
                episode.getArtworkURL()
        );
        putDurationAsLong(
                values,
                DURATION,
                episode.getDuration()
        );
        putIdentifier2(
                values,
                PODCAST_ID,
                podcastIdentifier
        );
        putDateAsLong(
                values,
                PUBLISH_TIME_MILLIS,
                episode.getPublishDate()
        );
        values.put(
                SUMMARY,
                episode.getSummary()
        );
        values.put(
                TITLE,
                episode.getTitle()
        );
        putURLAsString(
                values,
                URL,
                episode.getURL()
        );

        return values;
    }

    ContentValues getEpisodeIdentifiedContentValues(
            PodcastIdentifierType podcastIdentifier,
            EpisodeIdentifiedType episodeIdentified
    ) {
        final ContentValues values = getEpisodeContentValues(
                podcastIdentifier,
                episodeIdentified.getModel()
        );

        putIdentifier2(
                values,
                ID,
                episodeIdentified
        );

        return values;
    }

    private static final class EpisodeTableUpsertAdapter<
            PodcastIdentifierType extends Podcast2.PodcastIdentifier2
            > implements UpsertAdapter<String> {
        private final PodcastIdentifierType podcastIdentifier;

        public EpisodeTableUpsertAdapter(PodcastIdentifierType podcastIdentifier) {
            this.podcastIdentifier = podcastIdentifier;
        }

        @Override
        public SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<String> secondaryKeys) {
            final String selection =
                    URL + inPlaceholderClause(secondaryKeys.size())
                            + " AND " + PODCAST_ID + " = ? ";
            final Object[] bindArgs = new Object[secondaryKeys.size() + 1];
            secondaryKeys.toArray(bindArgs);
            bindArgs[secondaryKeys.size()] = podcastIdentifier.getId();
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
