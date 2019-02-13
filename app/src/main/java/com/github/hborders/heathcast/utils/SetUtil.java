package com.github.hborders.heathcast.utils;

import java.util.HashSet;
import java.util.Set;

public final class SetUtil {
    private SetUtil() {
    }

    public static <E> Set<E> union(Set<E> set1, Set<E> set2) {
        final HashSet<E> union = new HashSet<>(
                set1.size() + set2.size(),
                1);
        union.addAll(set1);
        union.addAll(set2);
        return union;
    }
}
