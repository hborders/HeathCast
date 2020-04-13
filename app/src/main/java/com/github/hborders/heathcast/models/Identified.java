package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.ClassUtil;

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
        final String simpleName = ClassUtil.getSpecificSimpleName(
                Identified.class,
                getClass()
        );

        return simpleName + "{" +
                "identifier=" + identifier +
                ", model=" + model +
                '}';
    }
}
