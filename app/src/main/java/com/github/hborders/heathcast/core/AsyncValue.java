package com.github.hborders.heathcast.core;

import java.util.Objects;

public abstract class AsyncValue<T> {
    public static <T> AsyncValue<T> loading(Class<T> valueClass) {
        return new Loading<>(valueClass);
    }

    public static <T> AsyncValue<T> failed(
            Class<T> valueClass,
            Throwable throwable
    ) {
        return new Failed<>(
                valueClass,
                throwable
        );
    }

    public static <T> AsyncValue<T> loaded(T value) {
        return new Loaded<>(value);
    }

    private AsyncValue() {
    }

    public abstract void act(
            VoidFunction<Loading<T>> loadingFunction,
            VoidFunction<Failed<T>> failedFunction,
            VoidFunction<Loaded<T>> loadedFunction
    );

    public abstract <R> R reduce(
            Function<Loading<T>, R> loadingFunction,
            Function<Failed<T>, R> failedFunction,
            Function<Loaded<T>, R> loadedFunction
    );

    public static final class Loading<T> extends AsyncValue<T> {
        private final Class<T> valueClass;

        public Loading(Class<T> valueClass) {
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

        @Override
        public void act(
                VoidFunction<Loading<T>> loadingFunction,
                VoidFunction<Failed<T>> failedFunction,
                VoidFunction<Loaded<T>> loadedFunction
        ) {
            loadingFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<T>, R> loadingFunction,
                Function<Failed<T>, R> failedFunction,
                Function<Loaded<T>, R> loadedFunction
        ) {
            return loadingFunction.apply(this);
        }
    }

    public static final class Failed<T> extends AsyncValue<T> {
        private final Class<T> valueClass;
        private final Throwable throwable;

        public Failed(
                Class<T> valueClass,
                Throwable throwable
        ) {
            this.valueClass = valueClass;
            this.throwable = throwable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Failed<?> failed = (Failed<?>) o;
            return valueClass.equals(failed.valueClass) &&
                    throwable.equals(failed.throwable);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass, throwable);
        }

        @Override
        public String toString() {
            return "Failed{" +
                    "valueClass=" + valueClass +
                    ", throwable=" + throwable +
                    '}';
        }

        @Override
        public void act(
                VoidFunction<Loading<T>> loadingFunction,
                VoidFunction<Failed<T>> failedFunction,
                VoidFunction<Loaded<T>> loadedFunction
        ) {
            failedFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<T>, R> loadingFunction,
                Function<Failed<T>, R> failedFunction,
                Function<Loaded<T>, R> loadedFunction
        ) {
            return failedFunction.apply(this);
        }
    }

    public static final class Loaded<T> extends AsyncValue<T> {
        public final T value;

        public Loaded(T value) {
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

        @Override
        public void act(
                VoidFunction<Loading<T>> loadingFunction,
                VoidFunction<Failed<T>> failedFunction,
                VoidFunction<Loaded<T>> loadedFunction
        ) {
            loadedFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<T>, R> loadingFunction,
                Function<Failed<T>, R> failedFunction,
                Function<Loaded<T>, R> loadedFunction
        ) {
            return loadedFunction.apply(this);
        }
    }
}
