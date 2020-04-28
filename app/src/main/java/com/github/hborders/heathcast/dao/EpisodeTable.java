package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;

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
        EpisodeIdentifiedSetType extends Episode.EpisodeIdentified.EpisodeIdentifiedSet2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode.EpisodeIdentifier,
        EpisodeListType extends Episode.EpisodeList2<EpisodeType>,
        PodcastIdentifierType extends Podcast.PodcastIdentifier
        > extends Table<MarkerType> {
    static final String TABLE_EPISODE = "episode";
    static final String FOREIGN_KEY_EPISODE = TABLE_EPISODE + "_id";

    private static final String COLUMN_ARTWORK_URL = "artwork_url";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PODCAST_EPISODE_ID = PodcastEpisodeTable.FOREIGN_KEY_PODCAST_EPISODE;
    private static final String COLUMN_PUBLISH_TIME_MILLIS = "publish_times_millis";
    private static final String COLUMN_SORT = "sort";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_URL = "url";

    private static final String[] COLUMNS_ALL_BUT_PODCAST_EPISODE_ID = new String[]{
            COLUMN_ARTWORK_URL,
            COLUMN_DURATION,
            COLUMN_ID,
            COLUMN_PUBLISH_TIME_MILLIS,
            COLUMN_SORT,
            COLUMN_SUMMARY,
            COLUMN_TITLE,
            COLUMN_URL,
    };

    static final String CREATE_FOREIGN_KEY_EPISODE =
            "FOREIGN KEY(" + FOREIGN_KEY_EPISODE + ") REFERENCES " + TABLE_EPISODE + "(" + COLUMN_ID + ")";

    static void createEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EPISODE + " ("
                + COLUMN_ARTWORK_URL + " TEXT, "
                + COLUMN_DURATION + " INTEGER, "
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PODCAST_EPISODE_ID + " INTEGER NOT NULL, "
                + COLUMN_PUBLISH_TIME_MILLIS + " INTEGER, "
                + COLUMN_SORT + " INTEGER NOT NULL UNIQUE DEFAULT 0, "
                + COLUMN_SUMMARY + " TEXT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_URL + " TEXT NOT NULL, "
                + "UNIQUE("
                + "  " + COLUMN_PODCAST_EPISODE_ID + ", "
                + "  " + COLUMN_URL + " "
                + "), "
                + PodcastEpisodeTable.CREATE_FOREIGN_KEY_PODCAST_EPISODE + " ON DELETE CASCADE "
                + ")"
        );
        db.execSQL(
                "CREATE TRIGGER " + TABLE_EPISODE + "_sort_after_insert_trigger"
                        + "  AFTER INSERT ON " + TABLE_EPISODE + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + TABLE_EPISODE
                        + "      SET " + COLUMN_SORT + " = ("
                        + "        SELECT"
                        + "          IFNULL(MAX(" + COLUMN_SORT + "), 0) + 1"
                        + "        FROM " + TABLE_EPISODE
                        + "      )"
                        + "      WHERE " + COLUMN_ID + " = NEW." + COLUMN_ID + ";"
                        + "    END"
        );
        // Don't need an insert trigger because all inserts trigger an update above
        db.execSQL(
                "CREATE TRIGGER " + TABLE_EPISODE + "_update_podcast_episode_version_after_update_trigger"
                        + "  AFTER UPDATE ON " + TABLE_EPISODE + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + PodcastEpisodeTable.TABLE_PODCAST_EPISODE
                        + "      SET " + PodcastEpisodeTable.COLUMN_VERSION + " = ("
                        + "        SELECT"
                        + "          IFNULL(MAX(" + PodcastEpisodeTable.COLUMN_VERSION + "), 0) + 1"
                        + "        FROM " + PodcastEpisodeTable.TABLE_PODCAST_EPISODE
                        + "      )"
                        + "      WHERE " + PodcastEpisodeTable.COLUMN_ID + " = NEW." + COLUMN_PODCAST_EPISODE_ID + ";"
                        + "    END"
        );
        db.execSQL(
                "CREATE TRIGGER " + TABLE_EPISODE + "_update_podcast_episode_version_after_delete_trigger"
                        + "  AFTER DELETE ON " + TABLE_EPISODE + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + PodcastEpisodeTable.TABLE_PODCAST_EPISODE
                        + "      SET " + PodcastEpisodeTable.COLUMN_VERSION + " = ("
                        + "        SELECT"
                        + "          IFNULL(MAX(" + PodcastEpisodeTable.COLUMN_VERSION + "), 0) + 1"
                        + "        FROM " + PodcastEpisodeTable.TABLE_PODCAST_EPISODE
                        + "      )"
                        + "      WHERE " + PodcastEpisodeTable.COLUMN_ID + " = OLD." + COLUMN_PODCAST_EPISODE_ID + ";"
                        + "    END"
        );
        db.execSQL(
                "CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_PODCAST_EPISODE_ID
                + " ON " + TABLE_EPISODE + "(" + COLUMN_PODCAST_EPISODE_ID + ")"
        );
        db.execSQL(
                "CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_PUBLISH_TIME_MILLIS
                + " ON " + TABLE_EPISODE + "(" + COLUMN_PUBLISH_TIME_MILLIS + ")"
        );
        db.execSQL(
                "CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_SORT
                + " ON " + TABLE_EPISODE + "(" + COLUMN_SORT + ")"
        );
        db.execSQL(
                "CREATE INDEX " + TABLE_EPISODE + "__" + COLUMN_URL
                + " ON " + TABLE_EPISODE + "(" + COLUMN_URL + ")"
        );
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
                            + " AND " + COLUMN_PODCAST_EPISODE_ID + " = ? ";
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
    private final CollectionFactory.Capacity<
            EpisodeIdentifiedListType,
            EpisodeIdentifiedType
            > episodeIdentifiedListCapacityFactory;
    private final CollectionFactory.Collection<
            EpisodeIdentifiedSetType,
            EpisodeIdentifiedType
            > episodeIdentifiedSetCollectionFactory;

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
            CollectionFactory.Capacity<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListCapacityFactory,
            CollectionFactory.Collection<
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedSetCollectionFactory
    ) {
        super(dimDatabase);

        this.episodeFactory2 = episodeFactory;
        this.episodeIdentifierFactory = episodeIdentifierFactory;
        this.episodeIdentifiedFactory = episodeIdentifiedFactory;
        this.episodeIdentifiedListCapacityFactory = episodeIdentifiedListCapacityFactory;
        this.episodeIdentifiedSetCollectionFactory = episodeIdentifiedSetCollectionFactory;
    }

    Optional<EpisodeIdentifierType> insertEpisode(
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
            return Optional.empty();
        } else {
            return Optional.of(
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

    List<Optional<EpisodeIdentifierType>> upsertEpisodes(
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
                ArrayList::new
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
                        .columns(COLUMNS_ALL_BUT_PODCAST_EPISODE_ID)
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
                                COLUMN_PODCAST_EPISODE_ID + " = ?",
                                new Object[]{podcastIdentifier.getId()}
                        )
                        .orderBy(COLUMN_SORT)
                        .columns(COLUMNS_ALL_BUT_PODCAST_EPISODE_ID)
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
                        .columns(COLUMNS_ALL_BUT_PODCAST_EPISODE_ID)
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
                COLUMN_PODCAST_EPISODE_ID,
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
