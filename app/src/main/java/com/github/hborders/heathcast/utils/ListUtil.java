package com.github.hborders.heathcast.utils;

import androidx.core.util.Pair;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ListUtil {
    private ListUtil() {
    }

    public static <E> Stream<NonnullPair<Integer, E>> indexedStream(List<E> list) {
        return IntStream
                .range(0, list.size())
                .mapToObj(
                        index -> new NonnullPair<>(
                                index,
                                list.get(index)
                        )
                );
    }
}
