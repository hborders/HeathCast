package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;

public final class SubscriptionTable<N> extends Table<N> {
    static final String TABLE_SUBSCRIPTION = "subscription";

    private static final String ID = "_id";
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String SORT = "sort";

    private static final String[] COLUMNS_ID = new String[]{ID};

    SubscriptionTable(DimDatabase<N> dimDatabase) {
        super(dimDatabase);
    }

    Optional<Identifier<Subscription>> insertSubscription(Identifier<Podcast> podcastIdentifier) {
        final long id = dimDatabase.insert(
                TABLE_SUBSCRIPTION,
                CONFLICT_ABORT,
                getSubscriptionContentValues(podcastIdentifier)
        );

        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(new SubscriptionIdentifier(id));
        }
    }

    int moveSubscriptionIdentifiedBefore(
            Identifier<Subscription> movingSubscriptionIdentifier,
            Identifier<Subscription> referenceSubscriptionIdentifier
    ) {
        SupportSQLiteStatement statement =
                dimDatabase.getWritableDatabase().compileStatement(
                        "UPDATE " + TABLE_SUBSCRIPTION + " "
                                + " SET " + SORT + " = ("
                                + "   CASE WHEN ("
                                + "     SELECT ( "
                                + "       SELECT " + SORT + " "
                                + "       FROM " + TABLE_SUBSCRIPTION + " "
                                + "       WHERE " + SORT + " < ( "
                                + "         SELECT " + SORT + " "
                                + "         FROM " + TABLE_SUBSCRIPTION + " "
                                + "         WHERE " + ID + " = ? "
                                + "       ) LIMIT 1 "
                                + "     ) as max_" + SORT + " "
                                + "   ) IS NULL THEN ("
                                + "     SELECT IFNULL(MIN(" + SORT + "), 0) - 1 " +
                                "       FROM " + TABLE_SUBSCRIPTION + " "
                                + "   ) ELSE ("
                                + "       ("
                                + "         ("
                                + "           SELECT " + SORT + " "
                                + "           FROM " + TABLE_SUBSCRIPTION + " "
                                + "           WHERE " + SORT + " < ( "
                                + "             SELECT " + SORT + " "
                                + "             FROM " + TABLE_SUBSCRIPTION + " "
                                + "             WHERE " + ID + " = ? "
                                + "           ) LIMIT 1 "
                                + "         ) + ( "
                                + "             SELECT " + SORT + " "
                                + "             FROM " + TABLE_SUBSCRIPTION + " "
                                + "             WHERE " + ID + " = ? "
                                + "         )"
                                + "       ) / 2"
                                + "   ) END "
                                + " ) WHERE " + ID + " = ?"
                );
        statement.bindLong(1, referenceSubscriptionIdentifier.id);
        statement.bindLong(2, referenceSubscriptionIdentifier.id);
        statement.bindLong(3, referenceSubscriptionIdentifier.id);
        statement.bindLong(4, movingSubscriptionIdentifier.id);
        return dimDatabase.executeUpdateDelete(
                TABLE_SUBSCRIPTION,
                statement
        );
    }

    int moveSubscriptionIdentifiedAfter(
            Identifier<Subscription> movingSubscriptionIdentifier,
            Identifier<Subscription> referenceSubscriptionIdentifier
    ) {
        SupportSQLiteStatement statement =
                dimDatabase.getWritableDatabase().compileStatement(
                        "UPDATE " + TABLE_SUBSCRIPTION + " "
                                + " SET " + SORT + " = ("
                                + "   CASE WHEN ("
                                + "     SELECT ( "
                                + "       SELECT " + SORT + " "
                                + "       FROM " + TABLE_SUBSCRIPTION + " "
                                + "       WHERE " + SORT + " > ( "
                                + "         SELECT " + SORT + " "
                                + "         FROM " + TABLE_SUBSCRIPTION + " "
                                + "         WHERE " + ID + " = ? "
                                + "       ) LIMIT 1 "
                                + "     ) as max_" + SORT + " "
                                + "   ) IS NULL THEN ("
                                + "     SELECT IFNULL(MAX(" + SORT + "), 0) + 1 " +
                                "       FROM " + TABLE_SUBSCRIPTION + " "
                                + "   ) ELSE ("
                                + "       ("
                                + "         ("
                                + "           SELECT " + SORT + " "
                                + "           FROM " + TABLE_SUBSCRIPTION + " "
                                + "           WHERE " + SORT + " > ( "
                                + "             SELECT " + SORT + " "
                                + "             FROM " + TABLE_SUBSCRIPTION + " "
                                + "             WHERE " + ID + " = ? "
                                + "           ) LIMIT 1 "
                                + "         ) + ( "
                                + "             SELECT " + SORT + " "
                                + "             FROM " + TABLE_SUBSCRIPTION + " "
                                + "             WHERE " + ID + " = ? "
                                + "         )"
                                + "       ) / 2"
                                + "   ) END "
                                + " ) WHERE " + ID + " = ?"
                );
        statement.bindLong(1, referenceSubscriptionIdentifier.id);
        statement.bindLong(2, referenceSubscriptionIdentifier.id);
        statement.bindLong(3, referenceSubscriptionIdentifier.id);
        statement.bindLong(4, movingSubscriptionIdentifier.id);
        return dimDatabase.executeUpdateDelete(
                TABLE_SUBSCRIPTION,
                statement
        );
    }

    int deleteSubscription(Identifier<Subscription> subscriptionIdentifier) {
        return dimDatabase.delete(
                TABLE_SUBSCRIPTION,
                ID + " = ?",
                Long.toString(subscriptionIdentifier.id)
        );
    }

    Observable<List<Identified<Subscription>>> observeQueryForSubscriptions() {
        return dimDatabase.createQuery(
                Arrays.asList(
                        PodcastTable.TABLE_PODCAST,
                        SubscriptionTable.TABLE_SUBSCRIPTION
                ),
                "SELECT "
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ARTWORK_URL + " AS " + PodcastTable.ARTWORK_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.AUTHOR + " AS " + PodcastTable.AUTHOR + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.FEED_URL + " AS " + PodcastTable.FEED_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " AS " + PodcastTable.ID + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.NAME + " AS " + PodcastTable.NAME + " "
                        + "FROM " + PodcastTable.TABLE_PODCAST + " "
                        + "INNER JOIN " + TABLE_SUBSCRIPTION + " "
                        + "  ON " + TABLE_SUBSCRIPTION + "." + PODCAST_ID + " "
                        + "    = " + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " "
                        + "ORDER BY " + TABLE_SUBSCRIPTION + "." + SORT
        ).mapToList(SubscriptionTable::getSubscriptionIdentified);
    }

    Observable<Optional<Identifier<Subscription>>> observeQueryForSubscriptionIdentifier(Identifier<Podcast> podcastIdentifier) {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_SUBSCRIPTION)
                        .columns(COLUMNS_ID)
                        .selection(
                                PODCAST_ID + " = ?",
                                new Object[]{podcastIdentifier.id}
                        ).create();
        return dimDatabase.createQuery(
                Arrays.asList(
                        PodcastTable.TABLE_PODCAST,
                        SubscriptionTable.TABLE_SUBSCRIPTION
                ),
                query
        ).mapToOptional(SubscriptionTable::getSubscriptionIdentifier);
    }

    static void createSubscriptionTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SUBSCRIPTION + " ("
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + PODCAST_ID + " INTEGER NOT NULL UNIQUE, "
                + SORT + " REAL NOT NULL UNIQUE DEFAULT 0, "
                + CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE"
                + ")");
        db.execSQL(
                "CREATE TRIGGER " + TABLE_SUBSCRIPTION + "_sort_after_insert_trigger"
                        + "  AFTER INSERT ON " + TABLE_SUBSCRIPTION + " FOR EACH ROW "
                        + "    BEGIN"
                        + "      UPDATE " + TABLE_SUBSCRIPTION
                        + "      SET " + SORT + " = (" +
                        "          SELECT" +
                        "            IFNULL(MAX(" + SORT + "), 0) + 1 " +
                        "          FROM " + TABLE_SUBSCRIPTION
                        + "      )"
                        + "      WHERE " + ID + " = NEW." + ID + ";"
                        + "    END"
        );
        db.execSQL("CREATE INDEX " + TABLE_SUBSCRIPTION + "__" + PODCAST_ID
                + " ON " + TABLE_SUBSCRIPTION + "(" + PODCAST_ID + ")");
        db.execSQL("CREATE INDEX " + TABLE_SUBSCRIPTION + "__" + SORT
                + " ON " + TABLE_SUBSCRIPTION + "(" + SORT + ")");
    }

    static ContentValues getSubscriptionContentValues(Identifier<Podcast> podcastIdentifier) {
        final ContentValues values = new ContentValues(1);

        putIdentifier(
                values,
                PODCAST_ID,
                podcastIdentifier
        );

        return values;
    }

    static Identified<Subscription> getSubscriptionIdentified(Cursor cursor) {
        final Identified<Podcast> podcastIdentified = PodcastTable.getPodcastIdentified(cursor);
        final Identifier<Subscription> subscriptionIdentifier = new SubscriptionIdentifier(
                CursorUtil.getNonnullLong(
                        cursor,
                        ID
                )
        );
        return new Identified<>(
                subscriptionIdentifier,
                new Subscription(podcastIdentified)
        );
    }

    static Identifier<Subscription> getSubscriptionIdentifier(Cursor cursor) {
        return new SubscriptionIdentifier(
                CursorUtil.getNonnullLong(
                        cursor,
                        ID
                )
        );
    }
}
