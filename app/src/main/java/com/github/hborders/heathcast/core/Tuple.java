package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

// Not abstract to allow reification
public class Tuple<F, S> {
    public final F first;
    public final S second;

    public Tuple(
            F first,
            S second
    ) {
        this.first = first;
        this.second = second;
    }

    public final F getFirst() {
        return first;
    }

    public final S getSecond() {
        return second;
    }

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return first.equals(tuple.first) &&
                second.equals(tuple.second);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
