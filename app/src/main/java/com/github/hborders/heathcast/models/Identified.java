package com.github.hborders.heathcast.models;

import java.util.Objects;

import javax.annotation.Nullable;

public final class Identified<M> {
    public final Identifier<M> identifier;
    public final M model;

    public Identified(
            Identifier<M> identifier,
            M model
    ) {
        this.identifier = identifier;
        this.model = model;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identified<?> that = (Identified<?>) o;
        return identifier.equals(that.identifier) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, model);
    }

    @Override
    public String toString() {
        return "Identified{" +
                "identifier=" + identifier +
                ", model=" + model +
                '}';
    }
}
