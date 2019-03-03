package com.github.hborders.heathcast.core;

import java.util.Objects;

public final class NonnullPair<F, S> {
    public final F first;
    public final S second;

    public NonnullPair(
            F first,
            S second
    ) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonnullPair<?, ?> that = (NonnullPair<?, ?>) o;
        return first.equals(that.first) &&
                second.equals(that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "NonnullPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
