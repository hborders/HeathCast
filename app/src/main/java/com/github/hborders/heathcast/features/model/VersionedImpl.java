package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.ClassUtil;
import com.github.hborders.heathcast.models.Versioned;

import java.util.Objects;

public abstract class VersionedImpl<ValueType> implements Versioned<ValueType> {
    public final ValueType value;
    public final long version;

    protected VersionedImpl(
            ValueType value,
            long version
    ) {
        this.value = value;
        this.version = version;
    }

    // Object

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionedImpl<?> versioned = (VersionedImpl<?>) o;
        return version == versioned.version &&
                value.equals(versioned.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value, version);
    }

    @Override
    public final String toString() {
        final String simpleName = ClassUtil.getSpecificSimpleName(
                VersionedImpl.class,
                getClass()
        );

        return simpleName + "{" +
                "value=" + value +
                ", version=" + version +
                '}';
    }

    // Versioned

    @Override
    public final ValueType getValue() {
        return value;
    }

    @Override
    public final long getVersion() {
        return version;
    }
}
