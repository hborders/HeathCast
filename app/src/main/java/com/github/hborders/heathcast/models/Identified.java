package com.github.hborders.heathcast.models;

import java.util.Objects;

public final class Identified<M> {
    public final Identifier<M> mIdentifier;
    public final M mModel;

    public Identified(
            Identifier<M> mIdentifier,
            M mModel
    ) {
        this.mIdentifier = mIdentifier;
        this.mModel = mModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identified<?> that = (Identified<?>) o;
        return mIdentifier.equals(that.mIdentifier) &&
                mModel.equals(that.mModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mIdentifier, mModel);
    }

    @Override
    public String toString() {
        return "Identified{" +
                "mIdentifier=" + mIdentifier +
                ", mModel=" + mModel +
                '}';
    }
}
