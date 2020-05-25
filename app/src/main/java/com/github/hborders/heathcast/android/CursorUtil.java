package com.github.hborders.heathcast.android;

import android.database.Cursor;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.URLUtil;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

public final class CursorUtil {
    private CursorUtil() {
    }

    @Nullable
    public static Integer getNullableInteger(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }

        return cursor.getInt(columnIndex);
    }

    public static int getNonnullInt(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }
        return cursor.getInt(columnIndex);
    }

    @Nullable
    public static Long getNullableLong(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return cursor.getLong(columnIndex);
    }

    public static long getNonnullLong(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }
        return cursor.getLong(columnIndex);
    }

    @Nullable
    public static String getNullableString(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return cursor.getString(columnIndex);
    }

    public static String getNonnullString(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }
        @Nullable final String value = cursor.getString(columnIndex);
        if (value == null) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }

        return value;
    }

    @Nullable
    public static URL getNullableURLFromString(
            Cursor cursor,
            int columnIndex
    ) {
        @Nullable final String string = getNullableString(
                cursor,
                columnIndex
        );
        return URLUtil.fromString(string);
    }

    public static URL getNonnullURLFromString(
            Cursor cursor,
            int columnIndex
    ) {
        @Nullable final URL value = getNullableURLFromString(
                cursor,
                columnIndex
        );
        if (value == null) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }

        return value;
    }

    @Nullable
    public static Duration getNullableDurationFromLong(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        final long value = cursor.getLong(columnIndex);
        return Duration.ofSeconds(value);
    }

    public static Duration getNonnullDurationFromLong(
            Cursor cursor,
            int columnIndex
    ) {
        @Nullable final Duration value = getNullableDurationFromLong(
                cursor,
                columnIndex
        );
        if (value == null) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }

        return value;
    }

    @Nullable
    public static Date getNullableDateFromLong(
            Cursor cursor,
            int columnIndex
    ) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        final long time = cursor.getLong(columnIndex);
        return new Date(time);
    }

    public static Date getNonnullDateFromLong(
            Cursor cursor,
            int columnIndex
    ) {
        @Nullable final Date date = getNullableDateFromLong(
                cursor,
                columnIndex
        );
        if (date == null) {
            throw new NullPointerException("Null value at column: " + columnIndex);
        }
        return date;
    }
}
