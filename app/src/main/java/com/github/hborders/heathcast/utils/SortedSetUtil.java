package com.github.hborders.heathcast.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public final class SortedSetUtil<E> {
    private final Comparator<E> comparator;
    public SortedSetUtil(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public SortedSet<E> singletonSortedSet(E element) {
        final TreeSet<E> singleton = new TreeSet<>(comparator);
        singleton.add(element);
        return Collections.unmodifiableSortedSet(singleton);
    }

    public SortedSet<E> union(
            SortedSet<E> set1,
            SortedSet<E> set2
    ) {
        final TreeSet<E> union = new TreeSet<>(comparator);
        union.addAll(set1);
        union.addAll(set2);
        return union;
    }
}
