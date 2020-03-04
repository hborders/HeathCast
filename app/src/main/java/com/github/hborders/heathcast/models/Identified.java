package com.github.hborders.heathcast.models;

import androidx.annotation.NonNull;

import java.util.Objects;

import javax.annotation.Nullable;

public abstract class Identified<I extends Identifier<M>, M> {
    public final I identifier;
    public final M model;

    public Identified(
            I identifier,
            M model
    ) {
        this.identifier = identifier;
        this.model = model;
    }

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identified<?, ?> that = (Identified<?, ?>) o;
        return identifier.equals(that.identifier) &&
                model.equals(that.model);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(identifier, model);
    }

    @Override
    public final String toString() {
        @SuppressWarnings("rawtypes") final Class<? extends Identified> clazz = getClass();
        @NonNull final String simpleName;
        if (clazz.isAnonymousClass()) {
            simpleName = "Identified$";
        } else {
            simpleName = clazz.getSimpleName();
        }

        return simpleName + "{" +
                "identifier=" + identifier +
                ", model=" + model +
                '}';
    }
}
