package com.github.hborders.heathcast.dao;

import android.content.ContentValues;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.utils.CursorUtil;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.dao.PodcastTable.CREATE_FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.FOREIGN_KEY_PODCAST;
import static com.github.hborders.heathcast.dao.PodcastTable.TABLE_PODCAST;

public final class SubscriptionTable extends Table {
    static final String TABLE_SUBSCRIPTION = "subscription";

    private static final String ID = "_id";
    private static final String PODCAST_ID = FOREIGN_KEY_PODCAST;
    private static final String SORT = "sort";

    private static final String[] COLUMNS_ID = new String[]{ID};

    SubscriptionTable(BriteDatabase briteDatabase) {
        super(briteDatabase);
    }

    Optional<Identifier<Subscription>> insertSubscription(Subscription subscription) {
        final long id = briteDatabase.insert(
                TABLE_SUBSCRIPTION,
                CONFLICT_ABORT,
                getSubscriptionContentValues(subscription)
        );

        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(
                    new Identifier<>(
                            Subscription.class,
                            id
                    )
            );
        }
    }

    int moveSubscriptionIdentifiedBefore(
            Identifier<Subscription> movingSubscriptionIdentifier,
            Identifier<Subscription> referenceSubscriptionIdentifier
    ) {

        SupportSQLiteStatement statement =
                briteDatabase.getWritableDatabase().compileStatement(
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
        return briteDatabase.executeUpdateDelete(
                TABLE_SUBSCRIPTION,
                statement
        );
    }

    int moveSubscriptionIdentifiedAfter(
            Identifier<Subscription> movingSubscriptionIdentifier,
            Identifier<Subscription> referenceSubscriptionIdentifier
    ) {
        SupportSQLiteStatement statement =
                briteDatabase.getWritableDatabase().compileStatement(
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
        return briteDatabase.executeUpdateDelete(
                TABLE_SUBSCRIPTION,
                statement
        );
    }

    int deleteSubscriptionIdentified(Identifier<Subscription> subscriptionIdentifier) {
        return briteDatabase.delete(
                TABLE_SUBSCRIPTION,
                ID + " = ?",
                Long.toString(subscriptionIdentifier.id)
        );
    }

    Observable<List<Identifier<Subscription>>> observeQueryForAllSubscriptionIdentifiers() {
        final SupportSQLiteQuery query =
                SupportSQLiteQueryBuilder
                        .builder(TABLE_SUBSCRIPTION)
                        .columns(COLUMNS_ID)
                        .orderBy(SORT)
                        .create();

        return briteDatabase
                .createQuery(
                        Arrays.asList(
                                TABLE_PODCAST,
                                TABLE_SUBSCRIPTION
                        ),
                        query
                )
                .mapToList(
                        cursor -> new Identifier<>(
                                Subscription.class,
                                CursorUtil.getNonnullLong(
                                        cursor,
                                        ID
                                )
                        )
                );
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

    static ContentValues getSubscriptionContentValues(Subscription subscription) {
        final ContentValues values = new ContentValues(3);

        putIdentifier(values, PODCAST_ID, subscription.podcastIdentified);

        return values;
    }

    static ContentValues getSubscriptionIdentifiedContentValues(Identified<Subscription> subscriptionIdentified) {
        final ContentValues values = getSubscriptionContentValues(subscriptionIdentified.model);

        putIdentifier(values, ID, subscriptionIdentified);

        return values;
    }
}
