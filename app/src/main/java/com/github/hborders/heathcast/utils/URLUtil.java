package com.github.hborders.heathcast.utils;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nullable;

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
