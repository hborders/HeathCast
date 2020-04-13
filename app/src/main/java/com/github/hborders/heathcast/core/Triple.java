package com.github.hborders.heathcast.core;

import java.util.Objects;

import javax.annotation.Nullable;

public class Triple<A, B, C> {
    public final A first;
    public final B second;
    public final C third;

    public Triple(
            A first,
            B second,
            C third
    ) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return first.equals(triple.first) &&
                second.equals(triple.second) &&
                third.equals(triple.third);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public final String toString() {
        final String simpleName = ClassUtil.getSpecificSimpleName(
                Triple.class,
                getClass()
        );

        return simpleName + "{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }
}
