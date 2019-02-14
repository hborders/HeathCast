package com.github.hborders.heathcast.models;

import java.util.Objects;

public final class Identifier<T> {
    public final Class<T> reifiedClass;
    public final long id;

    public Identifier(
            Class<T> reifiedClass,
            long id
    ) {
        this.reifiedClass = reifiedClass;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier<?> that = (Identifier<?>) o;
        return id == that.id &&
                reifiedClass.equals(that.reifiedClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reifiedClass, id);
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "reifiedClass=" + reifiedClass +
                ", id=" + id +
                '}';
    }
}
