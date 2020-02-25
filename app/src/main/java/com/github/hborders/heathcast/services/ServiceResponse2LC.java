package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.Objects;

public abstract class ServiceResponse2LC<LC, F> {
    public static final class Factory<LC, F> {
        private final Class<LC> valueClass;
        private final Class<F> failedValueClass;

        public Factory(
                Class<LC> valueClass,
                Class<F> failedValueClass
        ) {
            this.valueClass = valueClass;
            this.failedValueClass = failedValueClass;
        }

        public ServiceResponse2LC<LC, F> loading(LC value) {
            return new ServiceResponse2LC.Loading<>(
                    valueClass,
                    value
            );
        }

        public ServiceResponse2LC<LC, F> complete(LC value) {
            return new ServiceResponse2LC.Complete<>(
                    valueClass,
                    value
            );
        }

        public ServiceResponse2LC<LC, F> failed(F failedValue) {
            return new ServiceResponse2LC.Failed<>(
                    failedValueClass,
                    failedValue
            );
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Factory<?, ?> that = (Factory<?, ?>) o;
            return valueClass.equals(that.valueClass) &&
                    failedValueClass.equals(that.failedValueClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass, failedValueClass);
        }

        @Override
        public String toString() {
            return "ServiceResponse2Factory.Factory{" +
                    "valueClass=" + valueClass +
                    ", failedValueClass=" + failedValueClass +
                    '}';
        }
    }

    public static <LC, F> ServiceResponse2LC<LC, F> loading(
            Class<LC> valueClass,
            Class<F> failedValueClass, // unused, helps with type inference
            LC value
    ) {
        return new ServiceResponse2LC.Loading<>(
                valueClass,
                value
        );
    }

    public static <LC, F> ServiceResponse2LC<LC, F> complete(
            Class<LC> valueClass,
            Class<F> failedValueClass, // unused, helps with type inference
            LC value
    ) {
        return new ServiceResponse2LC.Complete<>(
                valueClass,
                value
        );
    }

    public static <LC, F> ServiceResponse2LC<LC, F> failed(
            Class<LC> loadingAndCompleteValueClass,  // unused, helps with type inference
            Class<F> valueClass,
            F failedValue
    ) {
        return new ServiceResponse2LC.Failed<>(
                valueClass,
                failedValue
        );
    }

    public static <LCF> ServiceResponse2LC<LCF, LCF> fromServiceResponse1(
            ServiceResponse1<LCF> serviceResponse1
    ) {
        return serviceResponse1.reduce(
                loading -> new ServiceResponse2LC.Loading<>(
                        loading.valueClass,
                        loading.value
                ),
                complete -> new ServiceResponse2LC.Complete<>(
                        complete.valueClass,
                        complete.value
                ),
                failed -> new ServiceResponse2LC.Failed<>(
                        failed.valueClass,
                        failed.value
                )
        );
    }

    private ServiceResponse2LC() {
    }

    public abstract void act(
            VoidFunction<ServiceResponse2LC.Loading<LC, F>> loadingVoidFunction,
            VoidFunction<ServiceResponse2LC.Complete<LC, F>> completeVoidFunction,
            VoidFunction<ServiceResponse2LC.Failed<LC, F>> failedVoidFunction
    );

    public abstract <R> R reduce(
            Function<ServiceResponse2LC.Loading<LC, F>, R> loadingFunction,
            Function<ServiceResponse2LC.Complete<LC, F>, R> completeFunction,
            Function<ServiceResponse2LC.Failed<LC, F>, R> failedFunction
    );

    public static final class Loading<L, F> extends ServiceResponse2LC<L, F> {
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
                VoidFunction<Loading<L, F>> loadingVoidFunction,
                VoidFunction<Complete<L, F>> completeVoidFunction,
                VoidFunction<Failed<L, F>> failedVoidFunction
        ) {
            loadingVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L, F>, R> loadingFunction,
                Function<Complete<L, F>, R> completeFunction,
                Function<Failed<L, F>, R> failedFunction
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

    public static final class Complete<C, F> extends ServiceResponse2LC<C, F> {
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
                VoidFunction<Loading<C, F>> loadingVoidFunction,
                VoidFunction<Complete<C, F>> completeVoidFunction,
                VoidFunction<Failed<C, F>> failedVoidFunction
        ) {
            completeVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<C, F>, R> loadingFunction,
                Function<Complete<C, F>, R> completeFunction,
                Function<Failed<C, F>, R> failedFunction
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

    public static final class Failed<LC, F> extends ServiceResponse2LC<LC, F> {
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
                VoidFunction<Loading<LC, F>> loadingVoidFunction,
                VoidFunction<Complete<LC, F>> completeVoidFunction,
                VoidFunction<Failed<LC, F>> failedVoidFunction
        ) {
            failedVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<LC, F>, R> loadingFunction,
                Function<Complete<LC, F>, R> completeFunction,
                Function<Failed<LC, F>, R> failedFunction
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
