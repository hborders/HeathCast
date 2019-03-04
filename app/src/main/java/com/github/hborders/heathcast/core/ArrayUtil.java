package com.github.hborders.heathcast.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ArrayUtil {
    private ArrayUtil() {}


    @Nonnull
    public static <E> List<E> asList(@Nullable E[] array) {
        if (array == null) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(array);
        }
    }
}
