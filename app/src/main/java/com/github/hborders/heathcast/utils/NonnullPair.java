package com.github.hborders.heathcast.utils;

import java.util.Objects;

public final class NonnullPair<F, S> {
    public final F mFirst;
    public final S mSecond;

    public NonnullPair(
            F first,
            S second
    ) {
        this.mFirst = first;
        this.mSecond = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonnullPair<?, ?> that = (NonnullPair<?, ?>) o;
        return mFirst.equals(that.mFirst) &&
                mSecond.equals(that.mSecond);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mFirst, mSecond);
    }

    @Override
    public String toString() {
        return "NonnullPair{" +
                "mFirst=" + mFirst +
                ", mSecond=" + mSecond +
                '}';
    }

    public F getFirst() {
        return mFirst;
    }

    public S getSecond() {
        return mSecond;
    }
}
