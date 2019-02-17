package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.utils.CursorUtil;
import com.github.hborders.heathcast.utils.NonnullPair;
import com.github.hborders.heathcast.utils.SetUtil;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableURLFromString;
import static com.github.hborders.heathcast.utils.ListUtil.indexedStream;
import static com.github.hborders.heathcast.utils.SqlUtil.inPlaceholderClause;

final class PodcastTable extends Table {
    private static final String TABLE_PODCAST = "podcast";

    private static final String ARTWORK_URL = "artwork_url";
    private static final String AUTHOR = "author";
    private static final String FEED_URL = "feed_url";
    private static final String ID = "_id";
    private static final String NAME = "name";

    static final String FOREIGN_KEY_PODCAST = TABLE_PODCAST + "_id";
    static final String CREATE_FOREIGN_KEY_PODCAST =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST + ") REFERENCES " + TABLE_PODCAST + "(" + ID + ")";
    private static final String[] COLUMNS_ID_FEED_URL = new String[]{
            ID,
            FEED_URL,
    };
    private static final String[] COLUMNS_ID = new String[]{ID};
    private static final String[] COLUMNS_ALL = new String[]{
            ID,
            ARTWORK_URL,
            AUTHOR,
            FEED_URL,
            NAME,
    };

    PodcastTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
    }

    @Nullable
    Identifier<Podcast> insertPodcast(Podcast podcast) {
        final long id = briteDatabase.insert(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastContentValues(podcast)
        );
        if (id == -1) {
            return null;
        } else {
            return new Identifier<>(
                    Podcast.class,
                    id
            );
        }
    }

    int updatePodcastIdentified(Identified<Podcast> identifiedPodcast) {
        return briteDatabase.update(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastIdentifiedContentValues(identifiedPodcast),
                ID + " = ?",
                Long.toString(identifiedPodcast.identifier.id)
        );
    }

    @Nullable
    Identifier<Podcast> upsertPodcast(Podcast podcast) {
        try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
            final SupportSQLiteQuery idQuery =
                    SupportSQLiteQueryBuilder
                            .builder(TABLE_PODCAST)
                            .columns(COLUMNS_ID)
                            .selection(
                                    FEED_URL + " = ?",
                                    new Object[]{podcast.feedURL.toExternalForm()})
                            .create();
            try (final Cursor cursor = briteDatabase.query(idQuery)) {
                @Nullable final Identifier<Podcast> podcastIdentifier;
                if (cursor.moveToNext()) {
                    podcastIdentifier = new Identifier<>(
                            Podcast.class,
                            cursor.getLong(0)
                    );

                    updatePodcastIdentified(
                            new Identified<>(
                                    podcastIdentifier,
                                    podcast
                            )
                    );
                } else {
                    podcastIdentifier = insertPodcast(podcast);
                }

                transaction.markSuccessful();

                return podcastIdentifier;
            }
        }
    }

    List<Optional<Identifier<Podcast>>> upsertPodcasts(List<Podcast> podcasts) {
        if (podcasts.isEmpty()) {
            return Collections.emptyList();
        } else {
            try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
                final Map<String, Set<NonnullPair<Integer, Podcast>>> indexPodcastSetsByFeedURLString =
                        indexedStream(podcasts)
                                .collect(
                                        Collectors.toMap(
                                                indexedPodcast -> indexedPodcast.mSecond.feedURL.toExternalForm(),
                                                indexedPodcast -> Collections.singleton(
                                                        new NonnullPair<>(
                                                                indexedPodcast.mFirst,
                                                                indexedPodcast.mSecond
                                                        )
                                                ),
                                                SetUtil::union
                                        )
                                );
                final String selection =
                        FEED_URL + inPlaceholderClause(indexPodcastSetsByFeedURLString.keySet().size());
                final SupportSQLiteQuery idAndFeedUrlQuery =
                        SupportSQLiteQueryBuilder
                                .builder(TABLE_PODCAST)
                                .columns(COLUMNS_ID_FEED_URL)
                                .selection(
                                        selection,
                                        indexPodcastSetsByFeedURLString.keySet().toArray()
                                )
                                .create();
                try (final Cursor podcastIdAndFeedURLCursor = briteDatabase.query(idAndFeedUrlQuery)) {
                    final HashSet<String> insertingPodcastFeedURLStrings = new HashSet<>(indexPodcastSetsByFeedURLString.keySet());
                    final List<Identified<Podcast>> updatingIdentifiedPodcasts = new ArrayList<>(podcasts.size());
                    while (podcastIdAndFeedURLCursor.moveToNext()) {
                        final long id = CursorUtil.getNonnullLong(
                                podcastIdAndFeedURLCursor,
                                ID
                        );
                        final String feedURLString = CursorUtil.getNonnullString(
                                podcastIdAndFeedURLCursor,
                                FEED_URL
                        );
                        @Nullable final Set<NonnullPair<Integer, Podcast>> indexPodcastSet =
                                indexPodcastSetsByFeedURLString.get(feedURLString);
                        if (indexPodcastSet == null) {
                            throw new IllegalStateException("Found unexpected feedURL: " + feedURLString);
                        } else {
                            final Podcast podcast = indexPodcastSet.iterator().next().mSecond;
                            insertingPodcastFeedURLStrings.remove(podcast.feedURL.toExternalForm());
                            updatingIdentifiedPodcasts.add(
                                    new Identified<>(
                                            new Identifier<>(
                                                    Podcast.class,
                                                    id
                                            ),
                                            podcast
                                    )
                            );
                        }
                    }
                    podcastIdAndFeedURLCursor.close();

                    List<Optional<Identifier<Podcast>>> upsertedPodcastIdentifiers =
                            new ArrayList<>(
                                    Collections.nCopies(
                                            podcasts.size(),
                                            Optional.empty()
                                    )
                            );
                    for (final String feedURLString : insertingPodcastFeedURLStrings) {
                        @Nullable final Set<NonnullPair<Integer, Podcast>> indexedPodcastSet =
                                indexPodcastSetsByFeedURLString.get(feedURLString);
                        if (indexedPodcastSet == null) {
                            throw new IllegalStateException("Found unexpected feedURL: " + feedURLString);
                        } else {
                            final Podcast podcast = indexedPodcastSet.iterator().next().mSecond;
                            final @Nullable Identifier<Podcast> podcastIdentifier = insertPodcast(podcast);
                            if (podcastIdentifier != null) {
                                for (final NonnullPair<Integer, Podcast> indexedPodcast : indexedPodcastSet) {
                                    upsertedPodcastIdentifiers.set(
                                            indexedPodcast.mFirst,
                                            Optional.of(podcastIdentifier)
                                    );
                                }
                            }
                        }
                    }

                    for (final Identified<Podcast> updatingIdentifiedPodcast : updatingIdentifiedPodcasts) {
                        final int rowCount = updatePodcastIdentified(updatingIdentifiedPodcast);
                        if (rowCount == 1) {
                            final String feedURLString = updatingIdentifiedPodcast.model.feedURL.toExternalForm();
                            @Nullable final Set<NonnullPair<Integer, Podcast>> indexedPodcastSet =
                                    indexPodcastSetsByFeedURLString.get(feedURLString);
                            if (indexedPodcastSet == null) {
                                throw new IllegalStateException("Found unexpected feedURL: " + feedURLString);
                            } else {
                                for (final NonnullPair<Integer, Podcast> indexedPodcast : indexedPodcastSet) {
                                    upsertedPodcastIdentifiers.set(
                                            indexedPodcast.mFirst,
                                            Optional.of(updatingIdentifiedPodcast.identifier)
                                    );
                                }
                            }
                        }
                    }

                    transaction.markSuccessful();

                    return upsertedPodcastIdentifiers;
                }
            }
        }
    }

    int deletePodcast(Identifier<Podcast> podcastIdentifier) {
        return briteDatabase.delete(
                TABLE_PODCAST,
                ID + " = ?",
                Long.toString(podcastIdentifier.id)
        );
    }

    int deletePodcasts(Collection<Identifier<Podcast>> podcastIdentifiers) {
        final String[] idStrings = new String[podcastIdentifiers.size()];
        int i = 0;
        for (Identifier<Podcast> podcastIdentifier : podcastIdentifiers) {
            idStrings[i] = Long.toString(podcastIdentifier.id);
            i++;
        }
        return briteDatabase.delete(
                TABLE_PODCAST,
                ID + inPlaceholderClause(podcastIdentifiers.size()),
                idStrings
        );
    }

    Observable<Set<Identified<Podcast>>> observeQueryForAllPodcastIdentifieds() {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST)
                        .columns(COLUMNS_ALL).create();

        return briteDatabase
                .createQuery(TABLE_PODCAST, query)
                .mapToList(PodcastTable::getPodcastIdentified)
                .map(HashSet::new);
    }

    Observable<Optional<Identified<Podcast>>> observeQueryForPodcastIdentified(
            Identifier<Podcast> podcastIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST)
                        .columns(COLUMNS_ALL)
                        .selection(
                                ID + "= ?",
                                new Object[]{podcastIdentifier.id}
                        ).create();

        return briteDatabase
                .createQuery(TABLE_PODCAST, query)
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

    static Identified<Podcast> getPodcastIdentified(Cursor cursor) {
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

        putURLAsString(values, ARTWORK_URL, podcast.artworkURL);
        values.put(AUTHOR, podcast.author);
        putURLAsString(values, FEED_URL, podcast.feedURL);
        values.put(NAME, podcast.name);

        return values;
    }

    static ContentValues getPodcastIdentifiedContentValues(Identified<Podcast> identifiedPodcast) {
        final ContentValues values = getPodcastContentValues(identifiedPodcast.model);
        putIdentifier(values, ID, identifiedPodcast);
        return values;
    }
}
