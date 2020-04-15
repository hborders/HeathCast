package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ArrayUtil {
    private ArrayUtil() {
    }

    public static <E> List<E> asList(@Nullable E[] array) {
        if (array == null) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(array);
        }
    }
}
