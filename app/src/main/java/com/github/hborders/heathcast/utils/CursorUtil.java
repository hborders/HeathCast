package com.github.hborders.heathcast.utils;

import android.database.Cursor;

import java.net.URL;
import java.time.Duration;

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
        final long value = getNonnullLong(cursor, columnName);
        return Duration.ofSeconds(value);
    }

    public static Duration getNonnullDurationFromLong(Cursor cursor, String columnName) {
        @Nullable final Duration value = getNullableDurationFromLong(cursor, columnName);
        if (value == null) {
            throw new NullPointerException(columnName);
        }

        return value;
    }
}
