package com.github.hborders.heathcast.utils;

import java.util.Collections;

public final class SqlUtil {
    private SqlUtil() {
    }

    public static String inPlaceholderClause(int count) {
        return " IN (" + String.join(
                ",",
                Collections.nCopies(
                        count,
                        "?"
                )
        ) + ")";
    }
}
