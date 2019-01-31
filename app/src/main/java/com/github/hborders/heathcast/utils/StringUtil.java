package com.github.hborders.heathcast.utils;

import javax.annotation.Nullable;

public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
