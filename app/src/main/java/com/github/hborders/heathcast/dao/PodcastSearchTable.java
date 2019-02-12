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

import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullInt;
import static com.github.hborders.heathcast.utils.CursorUtil.getNonnullString;

final class PodcastSearchTable {
    private static final String TABLE_PODCAST_SEARCH = "podcast_search";

    private static final String ID = "_id";
    private static final String SEARCH = "search";

    static final String FOREIGN_KEY_PODCAST_SEARCH = TABLE_PODCAST_SEARCH + "_id";
    static final String CREATE_FOREIGN_KEY_PODCAST_SEARCH =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST_SEARCH + ") REFERENCES " + TABLE_PODCAST_SEARCH + "(" + ID + ")";

    private final BriteDatabase mBriteDatabase;

    PodcastSearchTable(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    static void createPodcastSearchTable(SupportSQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_PODCAST_SEARCH + " ("
                        + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                        + SEARCH + " TEXT NOT NULL "
                        + ")"
        );
    }

    @Nullable
    public Identifier<PodcastSearch> insertPodcastSearch(PodcastSearch podcastSearch) {
        final long id = mBriteDatabase.insert(
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

    public Observable<Optional<Identified<PodcastSearch>>> observeQueryForPodcastSearch(
            Identifier<PodcastSearch> podcastSearchIdentifier
    ) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_PODCAST_SEARCH)
                        .columns(new String[]{
                                        ID,
                                        SEARCH
                                }
                        )
                        .selection(
                                ID + "= ?",
                                new Object[]{
                                        podcastSearchIdentifier.mId
                                }
                        ).create();

        return mBriteDatabase
                .createQuery(TABLE_PODCAST_SEARCH, query)
                .mapToOptional(PodcastSearchTable::getIdentifiedPodcastSearch);
    }

    static Identified<PodcastSearch> getIdentifiedPodcastSearch(Cursor cursor) {
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
        final ContentValues values = new ContentValues(2);
        values.put(SEARCH, podcastSearch.mSearch);
        return values;
    }

    static ContentValues getIdentifiedPodcastSearchContentValues(Identified<PodcastSearch> identifiedPodcastSearch) {
        final ContentValues values = getPodcastSearchContentValues(identifiedPodcastSearch.mModel);

        Database.putIdentifier(values, ID, identifiedPodcastSearch);

        return values;
    }
}
