package com.github.hborders.heathcast.features.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.ClassUtil;
import com.github.hborders.heathcast.core.Opt2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class OptImpl<ValueType> implements Opt2<ValueType> {
    public abstract static class OptListImpl<
            OptItemType extends Opt2<ValueType>,
            ValueType
            > extends ArrayList<OptItemType> {
        protected OptListImpl() {
        }

        protected OptListImpl(int initialCapacity) {
            super(initialCapacity);
        }

        protected OptListImpl(@NonNull Collection<? extends OptItemType> c) {
            super(c);
        }
    }

    @Nullable
    public final ValueType value;

    protected OptImpl() {
        value = null;
    }

    protected OptImpl(ValueType value) {
        this.value = value;
    }

    // Object

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptImpl<?> opt = (OptImpl<?>) o;
        return Objects.equals(value, opt.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        final String simpleName = ClassUtil.getSpecificSimpleName(
                OptImpl.class,
                getClass()
        );
        return simpleName + "{" +
                "value=" + value +
                '}';
    }

    // Opt

    @Override
    @Nullable
    public ValueType getValue() {
        return value;
    }
}
