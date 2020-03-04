package com.github.hborders.heathcast.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public abstract class Identifier<T> {
    public final long id;

    public Identifier(long id) {
        this.id = id;
    }

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier<?> that = (Identifier<?>) o;
        return id == that.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        @SuppressWarnings("rawtypes") final Class<? extends Identifier> clazz = getClass();
        @NonNull final String simpleName;
        if (clazz.isAnonymousClass()) {
            simpleName = "Identifier$";
        } else {
            simpleName = clazz.getSimpleName();
        }

        return simpleName + "{" +
                "id=" + id +
                '}';
    }
}
