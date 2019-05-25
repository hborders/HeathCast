package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.Objects;

public abstract class AsyncValue<T> {
    public static <T> AsyncValue<T> loading(Class<T> valueClass) {
        return new Loading<>(valueClass);
    }

    public static <T> AsyncValue<T> loaded(T value) {
        return new Loaded<>(value);
    }

    private AsyncValue() {
    }

    public abstract void act(
            VoidFunction<Loading<T>> loadingVoidFunction,
            VoidFunction<Loaded<T>> loadedVoidFunction
    );

    public abstract <R> R reduce(
            Function<Loading<T>, R> loadingFunction,
            Function<Loaded<T>, R> loadedFunction
    );

    public static final class Loading<T> extends AsyncValue<T> {
        private final Class<T> valueClass;
        private Loading(Class<T> valueClass) {
            this.valueClass = valueClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Loading<?> loading = (Loading<?>) o;
            return valueClass.equals(loading.valueClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass);
        }

        @Override
        public String toString() {
            return "Loading{" +
                    "valueClass=" + valueClass +
                    '}';
        }

        public void act(
                VoidFunction<Loading<T>> loadingVoidFunction,
                VoidFunction<Loaded<T>> loadedVoidFunction
        ) {
            loadingVoidFunction.apply(this);
        }

        public <R> R reduce(
                Function<Loading<T>, R> loadingFunction,
                Function<Loaded<T>, R> loadedFunction
        ) {
            return loadingFunction.apply(this);
        }
    }

    public static final class Loaded<T> extends AsyncValue<T> {
        public final T value;

        private Loaded(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Loaded<?> loaded = (Loaded<?>) o;
            return value.equals(loaded.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Loaded{" +
                    "value=" + value +
                    '}';
        }

        public void act(
                VoidFunction<Loading<T>> loadingVoidFunction,
                VoidFunction<Loaded<T>> loadedVoidFunction
        ) {
            loadedVoidFunction.apply(this);
        }

        public <R> R reduce(
                Function<Loading<T>, R> loadingFunction,
                Function<Loaded<T>, R> loadedFunction
        ) {
            return loadedFunction.apply(this);
        }
    }
}
