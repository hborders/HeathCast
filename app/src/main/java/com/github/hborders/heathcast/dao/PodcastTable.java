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
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ROLLBACK;
import static com.github.hborders.heathcast.utils.ContentValuesUtil.putURLAsString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullURLFromString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableString;
import static com.github.hborders.heathcast.utils.CursorUtil.getNullableURLFromString;
import static com.github.hborders.heathcast.utils.SqlUtil.inPlaceholderClause;

final class PodcastTable extends Table {
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

    PodcastTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
    }

    Optional<Identifier<Podcast>> insertPodcast(Podcast podcast) {
        final long id = briteDatabase.insert(
                TABLE_PODCAST,
                CONFLICT_ROLLBACK,
                getPodcastContentValues(podcast)
        );
        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(
                    new Identifier<>(
                            Podcast.class,
                            id
                    )
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
        return upsertModel(
                TABLE_PODCAST,
                ID,
                FEED_URL,
                Podcast.class,
                podcast,
                podcast_ -> podcast_.feedURL.toExternalForm(),
                cursor -> CursorUtil.getNonnullString(cursor, FEED_URL),
                this::insertPodcast,
                this::updatePodcastIdentified
        );
    }

    List<Optional<Identifier<Podcast>>> upsertPodcasts(List<Podcast> podcasts) {
        return upsertModels(
                TABLE_PODCAST,
                ID,
                FEED_URL,
                Podcast.class,
                podcasts,
                podcast -> podcast.feedURL.toExternalForm(),
                cursor -> CursorUtil.getNonnullString(cursor, FEED_URL),
                this::insertPodcast,
                this::updatePodcastIdentified
        );
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
                        .columns(COLUMNS_ALL)
                        .create();

        return briteDatabase
                .createQuery(
                        TABLE_PODCAST,
                        query
                )
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
