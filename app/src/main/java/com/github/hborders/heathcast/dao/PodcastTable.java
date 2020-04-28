package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableURLFromString;
import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;
import static com.github.hborders.heathcast.dao.EpisodeTable.TABLE_EPISODE;

final class PodcastTable<
        MarkerType,
        PodcastType extends Podcast,
        PodcastIdentifiedType extends Podcast.PodcastIdentified<
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedSetType extends Podcast.PodcastIdentified.PodcastIdentifiedSet2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast.PodcastIdentifier
        > extends Table<MarkerType> {
    static final String TABLE_PODCAST = "podcast";

    static final String ARTWORK_URL = "artwork_url";
    static final String AUTHOR = "author";
    static final String FEED_URL = "feed_url";
    static final String ID = "_id";
    static final String NAME = "name";

    static final String FOREIGN_KEY_PODCAST = TABLE_PODCAST + "_id";
    static final String CREATE_FOREIGN_KEY_PODCAST =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST + ") REFERENCES " + TABLE_PODCAST + "(" + ID + ")";
    private static final String[] COLUMNS_ALL = new String[]{
            ID,
            ARTWORK_URL,
            AUTHOR,
            FEED_URL,
            NAME,
    };
    private static final UpsertAdapter<String> upsertAdapter = new SingleColumnSecondaryKeyUpsertAdapter<>(
            TABLE_PODCAST,
            ID,
            FEED_URL,
            cursor -> CursorUtil.getNonnullString(cursor, FEED_URL)
    );

    static void createPodcastTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST + " ("
                + ARTWORK_URL + " TEXT, "
                + AUTHOR + " TEXT, "
                + FEED_URL + " TEXT NOT NULL UNIQUE, "
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL "
                + ")"
        );
        db.execSQL("CREATE INDEX " + TABLE_PODCAST + "__" + FEED_URL
                + " ON " + TABLE_PODCAST + "(" + FEED_URL + ")");
    }

    PodcastIdentifiedType getPodcastIdentified(Cursor cursor) {
        return getPodcastIdentified(
                podcastFactory,
                podcastIdentifierFactory,
                podcastIdentifiedFactory,
                cursor
        );
    }

    static <
            PodcastIdentifiedType extends Podcast.PodcastIdentified<
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifierType extends Podcast.PodcastIdentifier,
            PodcastType extends Podcast
            > PodcastIdentifiedType getPodcastIdentified(
            Podcast.PodcastFactory2<PodcastType> podcastFactory,
            Identifier.IdentifierFactory2<
                    PodcastIdentifierType
                    > podcastIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > podcastIdentifiedFactory,
            Cursor cursor
    ) {
        return podcastIdentifiedFactory.newIdentified(
                podcastIdentifierFactory.newIdentifier(
                        getNonnullInt(
                                cursor,
                                ID
                        )
                ),
                podcastFactory.newPodcast(
                        getNullableURLFromString(
                                cursor,
                                ARTWORK_URL
                        ),
                        getNullableString(
                                cursor,
                                AUTHOR
                        ),
                        getNonnullURLFromString(
                                cursor,
                                FEED_URL
                        ),
                        getNonnullString(
                                cursor,
                                NAME
                        )
                )
        );
    }

    private final Podcast.PodcastFactory2<PodcastType> podcastFactory;
    private final Identifier.IdentifierFactory2<
            PodcastIdentifierType
            > podcastIdentifierFactory;
    private final Identified.IdentifiedFactory2<
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > podcastIdentifiedFactory;
    private final CollectionFactory.Collection<
            PodcastIdentifiedSetType,
            PodcastIdentifiedType
            > podcastIdentifiedSetCollectionFactory;

    PodcastTable(
            DimDatabase<MarkerType> dimDatabase,
            Podcast.PodcastFactory2<PodcastType> podcastFactory,
            Identifier.IdentifierFactory2<
                    PodcastIdentifierType
                    > podcastIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > podcastIdentifiedFactory,
            CollectionFactory.Collection<
                    PodcastIdentifiedSetType,
                    PodcastIdentifiedType
                    > podcastIdentifiedSetCollectionFactory
    ) {
        super(dimDatabase);

        this.podcastFactory = podcastFactory;
        this.podcastIdentifierFactory = podcastIdentifierFactory;
        this.podcastIdentifiedFactory = podcastIdentifiedFactory;
        this.podcastIdentifiedSetCollectionFactory = podcastIdentifiedSetCollectionFactory;
    }

    Optional<PodcastIdentifierType> insertPodcast(PodcastType podcast) {
        final long id = dimDatabase.insert(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastContentValues(podcast)
        );
        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(
                    podcastIdentifierFactory.newIdentifier(id)
            );
        }
    }

    int updatePodcastIdentified(PodcastIdentifiedType identifiedPodcast) {
        return dimDatabase.update(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastIdentifiedContentValues(identifiedPodcast),
                ID + " = ?",
                Long.toString(identifiedPodcast.getIdentifier().getId())
        );
    }

    Optional<PodcastIdentifierType> upsertPodcast(PodcastType podcast) {
        return upsertModel2(
                upsertAdapter,
                String.class,
                podcast,
                podcast_ -> podcast_.getFeedURL().toExternalForm(),
                podcastIdentifierFactory,
                podcastIdentifiedFactory,
                this::insertPodcast,
                this::updatePodcastIdentified
        );
    }

    List<Optional<PodcastIdentifierType>> upsertPodcasts(List<PodcastType> podcasts) {
        return upsertModels2(
                upsertAdapter,
                String.class,
                podcasts,
                podcast -> podcast.getFeedURL().toExternalForm(),
                podcastIdentifierFactory,
                podcastIdentifiedFactory,
                this::insertPodcast,
                this::updatePodcastIdentified,
                ArrayList::new
        );
    }

    int deletePodcast(PodcastIdentifierType podcastIdentifier) {
        final SupportSQLiteStatement deleteStatement =
                dimDatabase.getWritableDatabase().compileStatement(
                        "DELETE FROM " + TABLE_PODCAST
                                + " WHERE " + ID + " = ?"
                );
        deleteStatement.bindLong(1, podcastIdentifier.getId());
        return dimDatabase.executeUpdateDelete(
                new HashSet<>(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_EPISODE
                        )
                ),
                deleteStatement
        );
    }

    int deletePodcasts(Collection<PodcastIdentifierType> podcastIdentifiers) {
        final String[] idStrings = idStrings2(podcastIdentifiers);
        return dimDatabase.delete(
                TABLE_PODCAST,
                ID + inPlaceholderClause(podcastIdentifiers.size()),
                idStrings
        );
    }

    void triggerMarked(MarkerType marker) {
        dimDatabase.triggerMarked(
                marker,
                TABLE_PODCAST
        );
    }

    Observable<PodcastIdentifiedSetType> observeQueryForAllPodcastIdentifieds() {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST)
                        .columns(COLUMNS_ALL)
                        .create();

        return dimDatabase
                .createQuery(
                        TABLE_PODCAST,
                        query
                )
                .mapToList(this::getPodcastIdentified)
                .map(podcastIdentifiedSetCollectionFactory::newCollection);
    }

    Observable<Optional<PodcastIdentifiedType>> observeQueryForPodcastIdentified(
            PodcastIdentifierType podcastIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST)
                        .columns(COLUMNS_ALL)
                        .selection(
                                ID + "= ?",
                                new Object[]{podcastIdentifier.getId()}
                        ).create();

        return dimDatabase
                .createQuery(
                        TABLE_PODCAST,
                        query
                )
                .mapToOptional(this::getPodcastIdentified);
    }

    ContentValues getPodcastContentValues(PodcastType podcast) {
        final ContentValues values = new ContentValues(5);

        putURLAsString(
                values,
                ARTWORK_URL,
                podcast.getArtworkURL()
        );
        values.put(
                AUTHOR,
                podcast.getAuthor()
        );
        putURLAsString(
                values,
                FEED_URL,
                podcast.getFeedURL()
        );
        values.put(
                NAME,
                podcast.getName()
        );

        return values;
    }

    ContentValues getPodcastIdentifiedContentValues(PodcastIdentifiedType identifiedPodcast) {
        final ContentValues values = getPodcastContentValues(identifiedPodcast.getModel());
        putIdentifier2(
                values,
                ID,
                identifiedPodcast
        );
        return values;
    }
}
