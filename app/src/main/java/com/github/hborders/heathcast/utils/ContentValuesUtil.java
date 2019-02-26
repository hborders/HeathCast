package com.github.hborders.heathcast.utils;

import android.content.ContentValues;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

import javax.annotation.Nullable;

public final class ContentValuesUtil {
    private ContentValuesUtil() {
    }

    public static void putURLAsString(
            ContentValues contentValues,
            String key,
            @Nullable URL url
    ) {
        final @Nullable String urlString;
        if (url == null) {
            urlString = null;
        } else {
            urlString = url.toExternalForm();
        }
        contentValues.put(key, urlString);
    }

    public static void putDurationAsLong(
            ContentValues contentValues,
            String key,
            @Nullable Duration duration
    ) {
        final @Nullable Long seconds;
        if (duration == null) {
            seconds = null;
        } else {
            seconds = duration.getSeconds();
        }
        contentValues.put(key, seconds);
    }

    public static void putDateAsLong(
            ContentValues contentValues,
            String key,
            @Nullable Date date
    ) {
        final @Nullable Long time;
        if (date == null) {
            time = null;
        } else {
            time = date.getTime();
        }
        contentValues.put(key, time);
    }
}
