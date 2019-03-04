package com.github.hborders.heathcast.android;

import android.database.Cursor;

import com.github.hborders.heathcast.core.URLUtil;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

import javax.annotation.Nullable;

public final class CursorUtil {
    private CursorUtil() {
    }

    @Nullable
    public static Integer getNullableInteger(Cursor cursor, String columnName) {
        final int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            return null;
        }

        return cursor.getInt(columnIndex);
    }

    public static int getNonnullInt(Cursor cursor, String columnName) {
        final int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            throw new NullPointerException(columnName);
        }
        return cursor.getInt(columnIndex);
    }

    @Nullable
    public static Long getNullableLong(Cursor cursor, String columnName) {
        final int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return cursor.getLong(columnIndex);
    }

    public static long getNonnullLong(Cursor cursor, String columnName) {
        final int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            throw new NullPointerException(columnName);
        }
        return cursor.getLong(columnIndex);
    }

    @Nullable
    public static String getNullableString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static String getNonnullString(Cursor cursor, String columnName) {
        @Nullable final String value = getNullableString(cursor, columnName);
        if (value == null) {
            throw new NullPointerException(columnName);
        }

        return value;
    }

    @Nullable
    public static URL getNullableURLFromString(Cursor cursor, String columnName) {
        @Nullable final String string = getNullableString(cursor, columnName);
        return URLUtil.fromString(string);
    }

    public static URL getNonnullURLFromString(Cursor cursor, String columnName) {
        @Nullable final URL value = getNullableURLFromString(cursor, columnName);
        if (value == null) {
            throw new NullPointerException(columnName);
        }

        return value;
    }

    @Nullable
    public static Duration getNullableDurationFromLong(Cursor cursor, String columnName) {
        final int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        final long value = cursor.getLong(columnIndex);
        return Duration.ofSeconds(value);
    }

    public static Duration getNonnullDurationFromLong(Cursor cursor, String columnName) {
        @Nullable final Duration value = getNullableDurationFromLong(cursor, columnName);
        if (value == null) {
            throw new NullPointerException(columnName);
        }

        return value;
    }

    @Nullable
    public static Date getNullableDateFromLong(Cursor cursor, String columnName) {
        final int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        final long time = cursor.getLong(columnIndex);
        return new Date(time);
    }

    public static Date getNonnullDateFromLong(Cursor cursor, String columnName) {
        @Nullable final Date date = getNullableDateFromLong(cursor, columnName);
        if (date == null) {
            throw new NullPointerException(columnName);
        }
        return date;
    }
}
