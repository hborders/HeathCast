package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

public final class URLUtil {
    private URLUtil() {
    }

    @Nullable
    public static URL fromString(@Nullable String spec) {
        if (spec == null) {
            return null;
        } else {
            try {
                return new URL(spec);
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }
}
