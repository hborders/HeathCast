package com.github.hborders.heathcast.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class SetUtil {
    public static <E> Set<E> union(Set<E> set1, Set<E> set2) {
        final HashSet<E> union = new HashSet<>(
                set1.size() + set2.size(),
                1);
        union.addAll(set2);
        union.addAll(set1);
        return union;
    }

    public static <E> Set<E> nonEmptySetOf(
            E requiredElement,
            E... elements
    ) {
        if (elements.length == 0) {
            return Collections.singleton(requiredElement);
        } else {
            final HashSet<E> set = new HashSet<>(elements.length + 1);
            set.add(requiredElement);
            Collections.addAll(set, elements);
            return set;
        }
    }

    public static <E> Set<E> setOf(E... elements) {
        if (elements.length == 0) {
            return Collections.emptySet();
        } else if (elements.length == 1) {
            return Collections.singleton(elements[0]);
        } else {
            final HashSet<E> set = new HashSet<>(elements.length);
            Collections.addAll(set, elements);
            return set;
        }
    }

    private SetUtil() {
    }
}
