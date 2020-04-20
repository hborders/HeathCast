package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.ClassUtil;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public abstract class IdentifiedImpl<
        IdentifierType extends Identifier,
        ModelType
        > implements Identified<
        IdentifierType,
        ModelType
        > {
    public static abstract class IdentifiedListImpl<
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier,
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

    public static abstract class IdentifiedOptImpl<
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier,
            ModelType
            > extends OptImpl<IdentifiedType> {
        public static abstract class IdentifiedOptListImpl<
                IdentifiedOptType extends Opt<IdentifiedType>,
                IdentifiedType extends Identified<
                        IdentifierType,
                        ModelType
                        >,
                IdentifierType extends Identifier,
                ModelType
                > extends OptListImpl<
                IdentifiedOptType,
                IdentifiedType
                > {
            protected IdentifiedOptListImpl() {
            }

            protected IdentifiedOptListImpl(int initialCapacity) {
                super(initialCapacity);
            }

            protected IdentifiedOptListImpl(Collection<? extends IdentifiedOptType> c) {
                super(c);
            }
        }

        protected IdentifiedOptImpl() {
        }

        protected IdentifiedOptImpl(IdentifiedType value) {
            super(value);
        }
    }

    public static abstract class IdentifiedSetImpl<
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier,
            ModelType
            > extends HashSet<IdentifiedType> {
        protected IdentifiedSetImpl() {
        }

        protected IdentifiedSetImpl(int initialCapacity) {
            super(initialCapacity);
        }

        protected IdentifiedSetImpl(Collection<? extends IdentifiedType> c) {
            super(c);
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
