package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.core.Tuple;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putDateAsLong;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putDurationAsLong;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullLong;
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
        EpisodeType extends Episode,
        EpisodeIdentifiedType extends Episode.EpisodeIdentified<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedListType extends Episode.EpisodeIdentified.EpisodeIdentifiedList2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedListVersionedType extends Episode.EpisodeIdentified.EpisodeIdentifiedList2.EpisodeIdentifiedListVersioned<
                EpisodeIdentifiedListType,
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedSetType extends Episode.EpisodeIdentified.EpisodeIdentifiedSet2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode.EpisodeIdentifier,
        EpisodeIdentifierOptType extends Episode.EpisodeIdentifier.EpisodeIdentifierOpt<
                EpisodeIdentifierType
                >,
        EpisodeIdentifierOptListType extends Episode.EpisodeIdentifier.EpisodeIdentifierOpt.EpisodeIdentifierOptList2<
                EpisodeIdentifierOptType,
                EpisodeIdentifierType
                >,
        EpisodeListType extends Episode.EpisodeList2<EpisodeType>,
        PodcastIdentifierType extends Podcast.PodcastIdentifier
        > extends Table<MarkerType> {
    static final String TABLE_EPISODE = "episode";

    private static final String COLUMN_ARTWORK_URL = "artwork_url";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String COLUMN_PUBLISH_TIME_MILLIS = "publish_times_millis";
    private static final String COLUMN_SORT = "sort";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_URL = "url";

    private static final String[] COLUMNS_ALL_BUT_PODCAST_ID = new String[]{
            COLUMN_ARTWORK_URL,
            COLUMN_DURATION,
            COLUMN_ID,
            COLUMN_PUBLISH_TIME_MILLIS,
            COLUMN_SORT,
            COLUMN_SUMMARY,
            COLUMN_TITLE,
            COLUMN_URL,
    };

    static final String FOREIGN_KEY_EPISODE = TABLE_EPISODE + "_id";
    static final String CREATE_FOREIGN_KEY_EPISODE =
            "FOREIGN KEY(" + FOREIGN_KEY_EPISODE + ") REFERENCES " + TABLE_EPISODE + "(" + COLUMN_ID + ")";

    static void createEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EPISODE + " ("
                + COLUMN_ARTWORK_URL + " TEXT, "
                + COLUMN_DURATION + " INTEGER, "
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PODCAST_ID + " INTEGER NOT NULL, "
                + COLUMN_PUBLISH_TIME_MILLIS + " INTEGER, "
                + COLUMN_SORT + " INTEGER NOT NULL UNIQUE DEFAULT 0, "
                + COLUMN_SUMMARY + " TEXT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_URL + " TEXT NOT NULL, "
                + "UNIQUE("
                + "  " + COLUMN_PODCAST_ID + ", "
                + "  " + COLUMN_URL + " "
                + "), "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                + ")"
        );
        db.execSQL(
                "CREATE TRIGGER " + TABLE_EPISODE + "_sort_after_insert_trigger"
                        + "  AFTER INSERT ON " + TABLE_EPISODE + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + TABLE_EPISODE
                        + "      SET " + COLUMN_SORT + " = (" +
                        "          SELECT" +
                        "            IFNULL(MAX(" + COLUMN_SORT + "), 0) + 1 " +
                        "          FROM " + TABLE_EPISODE
                        + "      )"
                        + "      WHERE " + COLUMN_ID + " = NEW." + COLUMN_ID + ";"
                        + "    END"
        );
        MetaTable.createUpdateVersionTriggers(
                db,
                TABLE_EPISODE,
                MetaTable.ID_EPISODE_TABLE
        );
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_PODCAST_ID
                + " ON " + TABLE_EPISODE + "(" + COLUMN_PODCAST_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_PUBLISH_TIME_MILLIS
                + " ON " + TABLE_EPISODE + "(" + COLUMN_PUBLISH_TIME_MILLIS + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_SORT
                + " ON " + TABLE_EPISODE + "(" + COLUMN_SORT + ")");
        db.execSQL("CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_URL
                + " ON " + TABLE_EPISODE + "(" + COLUMN_URL + ")");
    }

    private static final class EpisodeTableUpsertAdapter<
            PodcastIdentifierType extends Podcast.PodcastIdentifier
            > implements UpsertAdapter<String> {
        private final PodcastIdentifierType podcastIdentifier;

        public EpisodeTableUpsertAdapter(PodcastIdentifierType podcastIdentifier) {
            this.podcastIdentifier = podcastIdentifier;
        }

        @Override
        public SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<String> secondaryKeys) {
            final String selection =
                    COLUMN_URL + inPlaceholderClause(secondaryKeys.size())
                            + " AND " + COLUMN_PODCAST_ID + " = ? ";
            final Object[] bindArgs = new Object[secondaryKeys.size() + 1];
            secondaryKeys.toArray(bindArgs);
            bindArgs[secondaryKeys.size()] = podcastIdentifier.getId();
            return SupportSQLiteQueryBuilder
                    .builder(TABLE_EPISODE)
                    .columns(new String[]{
                            COLUMN_ID,
                            COLUMN_URL
                    })
                    .selection(
                            selection,
                            bindArgs
                    )
                    .create();
        }

        @Override
        public long getPrimaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return getNonnullLong(primaryAndSecondaryKeyCursor, COLUMN_ID);
        }

        @Override
        public String getSecondaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return CursorUtil.getNonnullString(primaryAndSecondaryKeyCursor, COLUMN_URL);
        }
    }

    private final Episode.EpisodeFactory2<EpisodeType> episodeFactory2;
    private final Identifier.IdentifierFactory2<
            EpisodeIdentifierType
            > episodeIdentifierFactory;
    private final Identified.IdentifiedFactory2<
            EpisodeIdentifiedType,
            EpisodeIdentifierType,
            EpisodeType
            > episodeIdentifiedFactory;
    private final Opt.OptEmptyFactory<
            EpisodeIdentifierOptType,
            EpisodeIdentifierType
            > episodeIdentifierOptEmptyFactory;
    private final Opt.OptNonEmptyFactory<
            EpisodeIdentifierOptType,
            EpisodeIdentifierType
            > episodeIdentifierOptNonEmptyFactory;
    private final CollectionFactory.Capacity<
            EpisodeIdentifiedListType,
            EpisodeIdentifiedType
            > episodeIdentifiedListCapacityFactory;
    private final BiFunction<
            EpisodeIdentifiedListType,
            Long,
            EpisodeIdentifiedListVersionedType
            > episodeIdentifiedListVersionedFactory;
    private final CollectionFactory.Collection<
            EpisodeIdentifiedSetType,
            EpisodeIdentifiedType
            > episodeIdentifiedSetCollectionFactory;
    private final CollectionFactory.Capacity<
            EpisodeIdentifierOptListType,
            EpisodeIdentifierOptType
            > episodeIdentifierOptListCapacityFactory;

    EpisodeTable(
            DimDatabase<MarkerType> dimDatabase,
            Episode.EpisodeFactory2<EpisodeType> episodeFactory,
            Identifier.IdentifierFactory2<
                    EpisodeIdentifierType
                    > episodeIdentifierFactory,
            Identified.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            Opt.OptEmptyFactory<
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierType
                    > episodeIdentifierOptEmptyFactory,
            Opt.OptNonEmptyFactory<
                    EpisodeIdentifierOptType,
                    EpisodeIdentifierType
                    > episodeIdentifierOptNonEmptyFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListCapacityFactory,
            BiFunction<
                    EpisodeIdentifiedListType,
                    Long,
                    EpisodeIdentifiedListVersionedType
                    > episodeIdentifiedListVersionedFactory,
            CollectionFactory.Collection<
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedSetCollectionFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifierOptListType,
                    EpisodeIdentifierOptType
                    > episodeIdentifierOptListCapacityFactory
    ) {
        super(dimDatabase);

        this.episodeFactory2 = episodeFactory;
        this.episodeIdentifierFactory = episodeIdentifierFactory;
        this.episodeIdentifiedFactory = episodeIdentifiedFactory;
        this.episodeIdentifierOptEmptyFactory = episodeIdentifierOptEmptyFactory;
        this.episodeIdentifierOptNonEmptyFactory = episodeIdentifierOptNonEmptyFactory;
        this.episodeIdentifiedListCapacityFactory = episodeIdentifiedListCapacityFactory;
        this.episodeIdentifiedListVersionedFactory = episodeIdentifiedListVersionedFactory;
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
                COLUMN_ID + " = ?",
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
                COLUMN_ID + " = ?",
                Long.toString(episodeIdentifier.getId())
        );
    }

    int deleteEpisodes(Collection<EpisodeIdentifierType> episodeIdentifiers) {
        final String[] idStrings = idStrings2(episodeIdentifiers);
        return dimDatabase.delete(
                TABLE_EPISODE,
                COLUMN_ID + inPlaceholderClause(episodeIdentifiers.size()),
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

    Observable<
            Optional<EpisodeIdentifiedListVersionedType>
            > observeQueryForEpisodeIdentifiedListVersionedOptionalForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        return dimDatabase.createQuery(
                Arrays.asList(
                        TABLE_PODCAST,
                        TABLE_EPISODE
                ),
                "SELECT "
                        + TABLE_EPISODE + "." + COLUMN_ARTWORK_URL + " AS " + COLUMN_ARTWORK_URL + ", "
                        + TABLE_EPISODE + "." + COLUMN_DURATION + " AS " + COLUMN_DURATION + ", "
                        + TABLE_EPISODE + "." + COLUMN_ID + " AS " + COLUMN_ID + ", "
                        + TABLE_EPISODE + "." + COLUMN_PUBLISH_TIME_MILLIS + " AS " + COLUMN_PUBLISH_TIME_MILLIS + ", "
                        + TABLE_EPISODE + "." + COLUMN_SORT + " AS " + COLUMN_SORT + ", "
                        + TABLE_EPISODE + "." + COLUMN_SUMMARY + " AS " + COLUMN_SUMMARY + ", "
                        + TABLE_EPISODE + "." + COLUMN_TITLE + " AS " + COLUMN_TITLE + ", "
                        + TABLE_EPISODE + "." + COLUMN_URL + " AS " + COLUMN_URL + ", "
                        + TABLE_EPISODE + "." + COLUMN_URL + " AS " + COLUMN_URL + ", "
                        + MetaTable.TABLE_META + "." + MetaTable.COLUMN_VERSION + " AS " + MetaTable.COLUMN_VERSION + " "
                        + "FROM " + TABLE_EPISODE + " "
                        + "INNER JOIN " + MetaTable.TABLE_META + " "
                        + "  ON " + MetaTable.TABLE_META + "." + MetaTable.COLUMN_ID + " "
                        + "    = " + MetaTable.ID_EPISODE_TABLE + " "
                        + "WHERE " + TABLE_EPISODE + "." + COLUMN_PODCAST_ID + " = ? "
                        + "ORDER BY " + TABLE_EPISODE + "." + COLUMN_SORT,
                podcastIdentifier.getId()
        ).lift(
                new MapToListVersionedOperator<>(
                        episodeIdentifiedListCapacityFactory,
                        this::getVersionAndEpisodeIdentified,
                        this.episodeIdentifiedListVersionedFactory
                )
        );
    }

    Observable<EpisodeIdentifiedListType> observeQueryForEpisodeIdentifiedsForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        return observeQueryForEpisodeIdentifiedListVersionedOptionalForPodcast(podcastIdentifier)
                .map(
                        episodeIdentifiedListVersionedOptional ->
                                episodeIdentifiedListVersionedOptional.map(
                                        EpisodeIdentifiedListVersionedType::getValue
                                ).orElse(
                                        episodeIdentifiedListCapacityFactory.newCollection(0)
                                )
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
                                COLUMN_ID + "= ?",
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

    private Tuple<
            Long,
            EpisodeIdentifiedType
            > getVersionAndEpisodeIdentified(Cursor cursor) {
        long version = getNonnullLong(
                cursor,
                MetaTable.COLUMN_VERSION
        );
        EpisodeIdentifiedType episodeIdentified = getEpisodeIdentified(cursor);
        return new Tuple<>(
                version,
                episodeIdentified
        );
    }

    EpisodeIdentifiedType getEpisodeIdentified(Cursor cursor) {
        return episodeIdentifiedFactory.newIdentified(
                episodeIdentifierFactory.newIdentifier(
                        getNonnullInt(
                                cursor,
                                COLUMN_ID
                        )
                ),
                episodeFactory2.newEpisode(
                        getNullableURLFromString(
                                cursor,
                                COLUMN_ARTWORK_URL
                        ),
                        getNullableDurationFromLong(
                                cursor,
                                COLUMN_DURATION
                        ),
                        getNullableDateFromLong(
                                cursor,
                                COLUMN_PUBLISH_TIME_MILLIS
                        ),
                        getNullableString(
                                cursor,
                                COLUMN_SUMMARY
                        ),
                        getNonnullString(
                                cursor,
                                COLUMN_TITLE
                        ),
                        getNonnullURLFromString(
                                cursor,
                                COLUMN_URL
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
                COLUMN_ARTWORK_URL,
                episode.getArtworkURL()
        );
        putDurationAsLong(
                values,
                COLUMN_DURATION,
                episode.getDuration()
        );
        putIdentifier2(
                values,
                COLUMN_PODCAST_ID,
                podcastIdentifier
        );
        putDateAsLong(
                values,
                COLUMN_PUBLISH_TIME_MILLIS,
                episode.getPublishDate()
        );
        values.put(
                COLUMN_SUMMARY,
                episode.getSummary()
        );
        values.put(
                COLUMN_TITLE,
                episode.getTitle()
        );
        putURLAsString(
                values,
                COLUMN_URL,
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
                COLUMN_ID,
                episodeIdentified
        );

        return values;
    }
}
