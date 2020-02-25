package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.Objects;

public abstract class ServiceResponse2CF<L, CF> {
    public static final class Factory<L, CF> {
        private final Class<L> loadingValueClass;
        private final Class<CF> valueClass;

        public Factory(
                Class<L> loadingValueClass,
                Class<CF> valueClass
        ) {
            this.loadingValueClass = loadingValueClass;
            this.valueClass = valueClass;
        }

        public ServiceResponse2CF<L, CF> loading(L loadingValue) {
            return new ServiceResponse2CF.Loading<>(
                    loadingValueClass,
                    loadingValue
            );
        }

        public ServiceResponse2CF<L, CF> complete(CF value) {
            return new ServiceResponse2CF.Complete<>(
                    valueClass,
                    value
            );
        }

        public ServiceResponse2CF<L, CF> failed(CF value) {
            return new ServiceResponse2CF.Failed<>(
                    valueClass,
                    value
            );
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Factory<?, ?> factory = (Factory<?, ?>) o;
            return loadingValueClass.equals(factory.loadingValueClass) &&
                    valueClass.equals(factory.valueClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(loadingValueClass, valueClass);
        }

        @Override
        public String toString() {
            return "Factory{" +
                    "loadingValueClass=" + loadingValueClass +
                    ", valueClass=" + valueClass +
                    '}';
        }
    }

    public static <L, CF> ServiceResponse2CF<L, CF> loading(
            Class<L> loadingValueClass,
            Class<CF> valueClass, // unused, helps with type inference
            L loadingValue
    ) {
        return new ServiceResponse2CF.Loading<>(
                loadingValueClass,
                loadingValue
        );
    }

    public static <L, CF> ServiceResponse2CF<L, CF> complete(
            Class<L> loadingValueClass,  // unused, helps with type inference
            Class<CF> valueClass,
            CF value
    ) {
        return new ServiceResponse2CF.Complete<>(
                valueClass,
                value
        );
    }

    public static <L, CF> ServiceResponse2CF<L, CF> failed(
            Class<L> loadingAndCompleteValueClass,  // unused, helps with type inference
            Class<CF> valueClass,
            CF failedValue
    ) {
        return new ServiceResponse2CF.Failed<>(
                valueClass,
                failedValue
        );
    }

    public static <LCF> ServiceResponse2CF<LCF, LCF> fromServiceResponse1(
            ServiceResponse1<LCF> serviceResponse1
    ) {
        return serviceResponse1.reduce(
                loading -> new ServiceResponse2CF.Loading<>(
                        loading.valueClass,
                        loading.value
                ),
                complete -> new ServiceResponse2CF.Complete<>(
                        complete.valueClass,
                        complete.value
                ),
                failed -> new ServiceResponse2CF.Failed<>(
                        failed.valueClass,
                        failed.value
                )
        );
    }

    private ServiceResponse2CF() {
    }

    public abstract void act(
            VoidFunction<ServiceResponse2CF.Loading<L, CF>> loadingVoidFunction,
            VoidFunction<ServiceResponse2CF.Complete<L, CF>> completeVoidFunction,
            VoidFunction<ServiceResponse2CF.Failed<L, CF>> failedVoidFunction
    );

    public abstract <R> R reduce(
            Function<ServiceResponse2CF.Loading<L, CF>, R> loadingFunction,
            Function<ServiceResponse2CF.Complete<L, CF>, R> completeFunction,
            Function<ServiceResponse2CF.Failed<L, CF>, R> failedFunction
    );

    public static final class Loading<L, CF> extends ServiceResponse2CF<L, CF> {
        public final Class<L> valueClass;
        public final L value;

        public Loading(
                Class<L> valueClass,
                L value
        ) {
            this.valueClass = valueClass;
            this.value = value;
        }

        @Override
        public void act(
                VoidFunction<Loading<L, CF>> loadingVoidFunction,
                VoidFunction<Complete<L, CF>> completeVoidFunction,
                VoidFunction<Failed<L, CF>> failedVoidFunction
        ) {
            loadingVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, CF>, R> loadingFunction,
                Function<Complete<L, CF>, R> completeFunction,
                Function<Failed<L, CF>, R> failedFunction
        ) {
            return loadingFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Loading<?, ?> loading = (Loading<?, ?>) o;
            return valueClass.equals(loading.valueClass) &&
                    value.equals(loading.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass, value);
        }

        @Override
        public String toString() {
            return "Loading{" +
                    "valueClass=" + valueClass +
                    ", value=" + value +
                    '}';
        }
    }

    public static final class Complete<L, CF> extends ServiceResponse2CF<L, CF> {
        public final Class<CF> valueClass;
        public final CF value;

        public Complete(
                Class<CF> valueClass,
                CF value
        ) {
            this.valueClass = valueClass;
            this.value = value;
        }

        @Override
        public void act(
                VoidFunction<Loading<L, CF>> loadingVoidFunction,
                VoidFunction<Complete<L, CF>> completeVoidFunction,
                VoidFunction<Failed<L, CF>> failedVoidFunction
        ) {
            completeVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, CF>, R> loadingFunction,
                Function<Complete<L, CF>, R> completeFunction,
                Function<Failed<L, CF>, R> failedFunction
        ) {
            return completeFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Complete<?, ?> complete = (Complete<?, ?>) o;
            return valueClass.equals(complete.valueClass) &&
                    value.equals(complete.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass, value);
        }

        @Override
        public String toString() {
            return "Complete{" +
                    "valueClass=" + valueClass +
                    ", value=" + value +
                    '}';
        }
    }

    public static final class Failed<L, CF> extends ServiceResponse2CF<L, CF> {
        public final Class<CF> valueClass;
        public final CF value;

        public Failed(
                Class<CF> valueClass,
                CF value
        ) {
            this.valueClass = valueClass;
            this.value = value;
        }

        @Override
        public void act(
                VoidFunction<Loading<L, CF>> loadingVoidFunction,
                VoidFunction<Complete<L, CF>> completeVoidFunction,
                VoidFunction<Failed<L, CF>> failedVoidFunction
        ) {
            failedVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, CF>, R> loadingFunction,
                Function<Complete<L, CF>, R> completeFunction,
                Function<Failed<L, CF>, R> failedFunction
        ) {
            return failedFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Failed<?, ?> failed = (Failed<?, ?>) o;
            return valueClass.equals(failed.valueClass) &&
                    value.equals(failed.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass, value);
        }

        @Override
        public String toString() {
            return "Failed{" +
                    "valueClass=" + valueClass +
                    ", value=" + value +
                    '}';
        }
    }
}
