package com.github.hborders.heathcast.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ListUtil {
    private ListUtil() {
    }

    public static <E> Stream<Tuple<Integer, E>> indexedStream(List<E> list) {
        return IntStream
                .range(0, list.size())
                .mapToObj(
                        index -> new Tuple<>(
                                index,
                                list.get(index)
                        )
                );
    }

    public static <E> List<E> listOf(E element1) {
        final ArrayList<E> list = new ArrayList<>(1);
        list.add(element1);
        return list;
    }

    public static <E> List<E> listOf(
            E element1,
            E element2
    ) {
        final ArrayList<E> list = new ArrayList<>(2);
        list.add(element1);
        list.add(element2);
        return list;
    }

    public static <E> List<E> listOf(
            E element1,
            E element2,
            E element3
    ) {
        final ArrayList<E> list = new ArrayList<>(3);
        list.add(element1);
        list.add(element2);
        list.add(element3);
        return list;
    }

    public static <E> List<E> listOf(
            E element1,
            E element2,
            E element3,
            E element4
    ) {
        final ArrayList<E> list = new ArrayList<>(4);
        list.add(element1);
        list.add(element2);
        list.add(element3);
        list.add(element4);
        return list;
    }

    public static <E> List<E> listOf(
            E element1,
            E element2,
            E element3,
            E element4,
            E element5
    ) {
        final ArrayList<E> list = new ArrayList<>(5);
        list.add(element1);
        list.add(element2);
        list.add(element3);
        list.add(element4);
        list.add(element5);
        return list;
    }
}
