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
        if (url != null) {
            contentValues.put(key, url.toExternalForm());
        }
    }

    public static void putDurationAsLong(
            ContentValues contentValues,
            String key,
            @Nullable Duration duration
    ) {
        if (duration != null) {
            contentValues.put(key, duration.getSeconds());
        }
    }

    public static void putDateAsLong(
            ContentValues contentValues,
            String key,
            @Nullable Date date
    ) {
        if (date != null) {
            contentValues.put(key, date.getTime());
        }
    }
}
