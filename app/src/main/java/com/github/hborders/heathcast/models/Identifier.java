package com.github.hborders.heathcast.models;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.ClassUtil;

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
        final String simpleName = ClassUtil.getSpecificSimpleName(
                Identifier.class,
                getClass()
        );

        return simpleName + "{" +
                "id=" + id +
                '}';
    }
}
