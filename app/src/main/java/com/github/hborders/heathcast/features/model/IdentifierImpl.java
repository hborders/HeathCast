package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.ClassUtil;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.models.Identifier;

import java.util.Collection;
import java.util.Objects;

public abstract class IdentifierImpl
        implements Identifier {
    public static abstract class IdentifierOptImpl<
            IdentifierType
            > extends OptImpl<IdentifierType> {
        public static abstract class IdentifierOptListImpl<
                IdentifierOptType extends Opt<IdentifierType>,
                IdentifierType extends Identifier
                > extends OptListImpl<
                IdentifierOptType,
                IdentifierType
                > {
            public IdentifierOptListImpl() {
            }

            public IdentifierOptListImpl(int initialCapacity) {
                super(initialCapacity);
            }

            public IdentifierOptListImpl(Collection<? extends IdentifierOptType> c) {
                super(c);
            }
        }

        protected IdentifierOptImpl() {
        }

        protected IdentifierOptImpl(IdentifierType identifier) {
            super(identifier);
        }
    }

    public final long id;

    protected IdentifierImpl(long id) {
        this.id = id;
    }

    // Object

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierImpl that = (IdentifierImpl) o;
        return id == that.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        final String simpleName = ClassUtil.getSpecificSimpleName(
                IdentifierImpl.class,
                getClass()
        );

        return simpleName + "{" +
                "id=" + id +
                '}';
    }

    // Identifier

    @Override
    public long getId() {
        return id;
    }
}
