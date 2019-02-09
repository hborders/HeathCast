package com.github.hborders.heathcast.models;

import java.util.Objects;

public final class Identifier<T> {
    public final Class<T> mClass;
    public final long mId;

    public Identifier(
            Class<T> mClass,
            long mId
    ) {
        this.mClass = mClass;
        this.mId = mId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier<?> that = (Identifier<?>) o;
        return mId == that.mId &&
                mClass.equals(that.mClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mClass, mId);
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "mClass=" + mClass +
                ", mId=" + mId +
                '}';
    }
}
