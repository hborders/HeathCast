package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.Objects;

import javax.annotation.Nullable;

public abstract class ServiceResponse3<L, C, F> {
    public static final class Factory<L, C, F> {
        private final Class<L> loadingValueClass;
        private final Class<C> completeValueClass;
        private final Class<F> failedValueClass;

        public Factory(
                Class<L> loadingValueClass,
                Class<C> completeValueClass,
                Class<F> failedValueClass
        ) {
            this.loadingValueClass = loadingValueClass;
            this.completeValueClass = completeValueClass;
            this.failedValueClass = failedValueClass;
        }

        public ServiceResponse3<L, C, F> loading(L loadingValue) {
            return new ServiceResponse3.Loading<>(
                    loadingValueClass,
                    loadingValue
            );
        }

        public ServiceResponse3<L, C, F> complete(C completeValue) {
            return new ServiceResponse3.Complete<>(
                    completeValueClass,
                    completeValue
            );
        }

        public ServiceResponse3<L, C, F> failed(F failedValue) {
            return new ServiceResponse3.Failed<>(
                    failedValueClass,
                    failedValue
            );
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Factory<?, ?, ?> factory = (Factory<?, ?, ?>) o;
            return loadingValueClass.equals(factory.loadingValueClass) &&
                    completeValueClass.equals(factory.completeValueClass) &&
                    failedValueClass.equals(factory.failedValueClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(loadingValueClass, completeValueClass, failedValueClass);
        }

        @Override
        public String toString() {
            return "Factory{" +
                    "loadingValueClass=" + loadingValueClass +
                    ", completeValueClass=" + completeValueClass +
                    ", failedValueClass=" + failedValueClass +
                    '}';
        }
    }

    public static <L, C, F> ServiceResponse3<L, C, F> loading(
            Class<L> loadingValueClass,
            Class<C> completeValueClass, // unused, helps with type inference
            Class<F> failedValueClass, // unused, helps with type inference
            L loadingValue
    ) {
        return new ServiceResponse3.Loading<>(
                loadingValueClass,
                loadingValue
        );
    }

    public static <L, C, F> ServiceResponse3<L, C, F> complete(
            Class<L> loadingValueClass, // unused, helps with type inference
            Class<C> completeValueClass,
            Class<F> failedValueClass, // unused, helps with type inference
            C completeValue
    ) {
        return new ServiceResponse3.Complete<>(
                completeValueClass,
                completeValue
        );
    }

    public static <L, C, F> ServiceResponse3<L, C, F> failed(
            Class<L> loadingValueClass, // unused, helps with type inference
            Class<C> completeValueClass, // unused, helps with type inference
            Class<F> failedValueClass,
            F failedValue
    ) {
        return new ServiceResponse3.Failed<>(
                failedValueClass,
                failedValue
        );
    }

    public static <LCF> ServiceResponse3<LCF, LCF, LCF> fromServiceResponse1(
            ServiceResponse1<LCF> serviceResponse1
    ) {
        return serviceResponse1.reduce(
                loading -> new ServiceResponse3.Loading<>(
                        loading.valueClass,
                        loading.value
                ),
                complete -> new ServiceResponse3.Complete<>(
                        complete.valueClass,
                        complete.value
                ),
                failed -> new ServiceResponse3.Failed<>(
                        failed.valueClass,
                        failed.value
                )
        );
    }

    public static <LC, F> ServiceResponse3<LC, LC, F> fromServiceResponse2(
            ServiceResponse2LC<LC, F> serviceResponse2
    ) {
        return serviceResponse2.reduce(
                loading -> new ServiceResponse3.Loading<>(
                        loading.valueClass,
                        loading.value
                ),
                complete -> new ServiceResponse3.Complete<>(
                        complete.valueClass,
                        complete.value
                ),
                failed -> new ServiceResponse3.Failed<>(
                        failed.valueClass,
                        failed.value
                )
        );
    }

    private ServiceResponse3() {
    }

    public abstract void act(
            VoidFunction<Loading<L, C, F>> loadingVoidFunction,
            VoidFunction<Complete<L, C, F>> completeVoidFunction,
            VoidFunction<Failed<L, C, F>> failedVoidFunction
    );

    public abstract <R> R reduce(
            Function<Loading<L, C, F>, R> loadingFunction,
            Function<Complete<L, C, F>, R> completeFunction,
            Function<Failed<L, C, F>, R> failedFunction
    );

    public static final class Loading<L, C, F> extends ServiceResponse3<L, C, F> {
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
                VoidFunction<Loading<L, C, F>> loadingVoidFunction,
                VoidFunction<Complete<L, C, F>> completeVoidFunction,
                VoidFunction<Failed<L, C, F>> failedVoidFunction
        ) {
            loadingVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, C, F>, R> loadingFunction,
                Function<Complete<L, C, F>, R> completeFunction,
                Function<Failed<L, C, F>, R> failedFunction
        ) {
            return loadingFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Loading<?, ?, ?> loading = (Loading<?, ?, ?>) o;
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

    public static final class Complete<L, C, F> extends ServiceResponse3<L ,C, F> {
        public final Class<C> valueClass;
        public final C value;

        public Complete(
                Class<C> valueClass,
                C value
        ) {
            this.valueClass = valueClass;
            this.value = value;
        }

        @Override
        public void act(
                VoidFunction<Loading<L, C, F>> loadingVoidFunction,
                VoidFunction<Complete<L, C, F>> completeVoidFunction,
                VoidFunction<Failed<L, C, F>> failedVoidFunction
        ) {
            completeVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, C, F>, R> loadingFunction,
                Function<Complete<L, C, F>, R> completeFunction,
                Function<Failed<L, C, F>, R> failedFunction
        ) {
            return completeFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Complete<?, ?, ?> complete = (Complete<?, ?, ?>) o;
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

    public static final class Failed<L, C, F> extends ServiceResponse3<L, C, F> {
        public final Class<F> valueClass;
        public final F value;

        public Failed(
                Class<F> valueClass,
                F value
        ) {
            this.valueClass = valueClass;
            this.value = value;
        }

        @Override
        public void act(
                VoidFunction<Loading<L, C, F>> loadingVoidFunction,
                VoidFunction<Complete<L, C, F>> completeVoidFunction,
                VoidFunction<Failed<L, C, F>> failedVoidFunction
        ) {
            failedVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, C, F>, R> loadingFunction,
                Function<Complete<L, C, F>, R> completeFunction,
                Function<Failed<L, C, F>, R> failedFunction
        ) {
            return failedFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Failed<?, ?, ?> failed = (Failed<?, ?, ?>) o;
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
