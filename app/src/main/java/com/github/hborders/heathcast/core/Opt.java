package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class Opt<V> {
    public interface OptFactory<P extends Opt<V>, V> {
        P empty();

        P of(V v);

        default P ofNullable(@Nullable V v) {
            final P p;
            if (v == null) {
                p = empty();
            } else {
                p = of(v);
            }
            return p;
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        default P fromOptional(Optional<V> optional) {
            return optional
                    .map(this::of)
                    .orElseGet(this::empty);
        }
    }

    @Nullable
    public final V value;

    public Opt() {
        this.value = null;
    }

    public Opt(V value) {
        this.value = value;
    }

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opt<?> opt = (Opt<?>) o;
        return Objects.equals(value, opt.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public final String toString() {
        @SuppressWarnings("rawtypes") final Class<? extends Opt> clazz = getClass();
        final String simpleName;
        if (clazz.isAnonymousClass()) {
            simpleName = "Opt$";
        } else {
            simpleName = clazz.getSimpleName();
        }
        return simpleName + "{" +
                "value=" + value +
                '}';
    }

    public boolean isPresent() {
        return value != null;
    }

    @Nullable
    public V orNull() {
        return value;
    }

    public V orElse(V other) {
        final V v;
        if (value == null) {
            v = other;
        } else {
            v = value;
        }
        return v;
    }

    public V orElseGet(Supplier<? extends V> otherSupplier) {
        final V v;
        if (value == null) {
            v = otherSupplier.get();
        } else {
            v = value;
        }
        return v;
    }

    public <T extends Throwable> V orElseThrow(Supplier<? extends T> exceptionSupplier) throws T {
        final V v;
        if (value == null) {
            final T throwable = exceptionSupplier.get();
            throw throwable;
        } else {
            v = value;
        }
        return v;
    }

    public <G extends OptFactory<Q, W>, Q extends Opt<W>, W> Q map(
            G optFactory,
            Function<? super V, W> mapper
    ) {
        final Q q;
        if (value == null) {
            q = optFactory.empty();
        } else {
            final W w = mapper.apply(value);
            q = optFactory.of(w);
        }
        return q;
    }

    public <G extends OptFactory<Q, W>, Q extends Opt<W>, W> Q flatMap(
            G optFactory,
            Function<? super V, Q> mapper
    ) {
        final Q q;
        if (value == null) {
            q = optFactory.empty();
        } else {
            q = mapper.apply(value);
        }
        return q;
    }

    public void act(VoidFunction<V> action) {
        if (value != null) {
            action.apply(value);
        }
    }

    public Optional<V> toOptional() {
        return Optional.ofNullable(value);
    }
}
