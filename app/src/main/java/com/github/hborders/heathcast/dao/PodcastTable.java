package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedSet;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.android.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.android.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.android.CursorUtil.getNullableURLFromString;
import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;
import static com.github.hborders.heathcast.dao.EpisodeTable.TABLE_EPISODE;

final class PodcastTable<N> extends Table<N> {
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

    PodcastTable(DimDatabase<N> dimDatabase) {
        super(dimDatabase);
    }

    Optional<PodcastIdentifier> insertPodcast(Podcast podcast) {
        final long id = dimDatabase.insert(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastContentValues(podcast)
        );
        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(new PodcastIdentifier(id));
        }
    }

    int updatePodcastIdentified(PodcastIdentified identifiedPodcast) {
        return dimDatabase.update(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastIdentifiedContentValues(identifiedPodcast),
                ID + " = ?",
                Long.toString(identifiedPodcast.identifier.id)
        );
    }

    Optional<PodcastIdentifier> upsertPodcast(Podcast podcast) {
        return upsertModel(
                upsertAdapter,
                String.class,
                podcast,
                podcast_ -> podcast_.feedURL.toExternalForm(),
                PodcastIdentifier::new,
                PodcastIdentified::new,
                this::insertPodcast,
                this::updatePodcastIdentified
        );
    }

    List<Optional<PodcastIdentifier>> upsertPodcasts(List<Podcast> podcasts) {
        return upsertModels(
                upsertAdapter,
                String.class,
                podcasts,
                podcast -> podcast.feedURL.toExternalForm(),
                PodcastIdentifier::new,
                PodcastIdentified::new,
                this::insertPodcast,
                this::updatePodcastIdentified
        );
    }

    int deletePodcast(PodcastIdentifier podcastIdentifier) {
        final SupportSQLiteStatement deleteStatement =
                dimDatabase.getWritableDatabase().compileStatement(
                        "DELETE FROM " + TABLE_PODCAST
                                + " WHERE " + ID + " = ?"
                );
        deleteStatement.bindLong(1, podcastIdentifier.id);
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

    int deletePodcasts(Collection<PodcastIdentifier> podcastIdentifiers) {
        final String[] idStrings = idStrings(podcastIdentifiers);
        return dimDatabase.delete(
                TABLE_PODCAST,
                ID + inPlaceholderClause(podcastIdentifiers.size()),
                idStrings
        );
    }

    void triggerMarked(N marker) {
        dimDatabase.triggerMarked(
                marker,
                TABLE_PODCAST
        );
    }

    Observable<PodcastIdentifiedSet> observeQueryForAllPodcastIdentifieds() {
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
                .mapToList(PodcastTable::getPodcastIdentified)
                .map(PodcastIdentifiedSet::new);
    }

    Observable<Optional<PodcastIdentified>> observeQueryForPodcastIdentified(
            PodcastIdentifier podcastIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST)
                        .columns(COLUMNS_ALL)
                        .selection(
                                ID + "= ?",
                                new Object[]{podcastIdentifier.id}
                        ).create();

        return dimDatabase
                .createQuery(
                        TABLE_PODCAST,
                        query
                )
                .mapToOptional(PodcastTable::getPodcastIdentified);
    }

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

    static PodcastIdentified getPodcastIdentified(Cursor cursor) {
        return new PodcastIdentified(
                new PodcastIdentifier(
                        getNonnullInt(
                                cursor,
                                ID
                        )
                ),
                new Podcast(
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

    static ContentValues getPodcastContentValues(Podcast podcast) {
        final ContentValues values = new ContentValues(5);

        putURLAsString(
                values,
                ARTWORK_URL
                , podcast.artworkURL
        );
        values.put(
                AUTHOR,
                podcast.author
        );
        putURLAsString(
                values,
                FEED_URL,
                podcast.feedURL
        );
        values.put(
                NAME,
                podcast.name
        );

        return values;
    }

    static ContentValues getPodcastIdentifiedContentValues(PodcastIdentified identifiedPodcast) {
        final ContentValues values = getPodcastContentValues(identifiedPodcast.model);
        putIdentifier(
                values,
                ID,
                identifiedPodcast
        );
        return values;
    }
}
