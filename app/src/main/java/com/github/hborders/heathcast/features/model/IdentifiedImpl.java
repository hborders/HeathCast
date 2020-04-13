package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.ClassUtil;
import com.github.hborders.heathcast.dao.Identified2;
import com.github.hborders.heathcast.dao.Identifier2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public abstract class IdentifiedImpl<
        IdentifierType extends Identifier2,
        ModelType
        > implements Identified2<
        IdentifierType,
        ModelType
        > {
    public static abstract class IdentifiedListImpl<
            IdentifiedType extends Identified2<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier2,
            ModelType
            > extends ArrayList<IdentifiedType> {
        protected IdentifiedListImpl() {
        }

        protected IdentifiedListImpl(int capacity) {
            super(capacity);
        }

        protected IdentifiedListImpl(Collection<? extends IdentifiedType> identifieds) {
            super(identifieds);
        }
    }

    public static abstract class IdentifiedSetImpl<
            IdentifiedType extends Identified2<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier2,
            ModelType
            > extends HashSet<IdentifiedType> {
        protected IdentifiedSetImpl() {
        }

        protected IdentifiedSetImpl(Collection<? extends IdentifiedType> c) {
            super(c);
        }

        protected IdentifiedSetImpl(int initialCapacity) {
            super(initialCapacity);
        }
    }

    public IdentifierType identifier;
    public ModelType model;

    protected IdentifiedImpl(
            IdentifierType identifier,
            ModelType model
    ) {
        this.identifier = identifier;
        this.model = model;
    }

    // Object

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifiedImpl<?, ?> that = (IdentifiedImpl<?, ?>) o;
        return identifier.equals(that.identifier) &&
                model.equals(that.model);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(
                identifier,
                model
        );
    }

    @Override
    public final String toString() {
        final String simpleName = ClassUtil.getSpecificSimpleName(
                IdentifiedImpl.class,
                getClass()
        );

        return simpleName + "{" +
                "identifier=" + identifier +
                ", model=" + model +
                '}';
    }

    // Identified

    public IdentifierType getIdentifier() {
        return identifier;
    }

    public ModelType getModel() {
        return model;
    }
}
