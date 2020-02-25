package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.Objects;

public abstract class ServiceResponse1<LCF> {
    public static final class Factory<LCF> {
        private final Class<LCF> valueClass;

        public Factory(Class<LCF> valueClass) {
            this.valueClass = valueClass;
        }

        public ServiceResponse1<LCF> loading(LCF value) {
            return new ServiceResponse1.Loading<>(
                    valueClass,
                    value
            );
        }

        public ServiceResponse1<LCF> complete(LCF value) {
            return new ServiceResponse1.Complete<>(
                    valueClass,
                    value
            );
        }

        public ServiceResponse1<LCF> failed(LCF value) {
            return new ServiceResponse1.Failed<>(
                    valueClass,
                    value
            );
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Factory<?> factory = (Factory<?>) o;
            return valueClass.equals(factory.valueClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueClass);
        }

        @Override
        public String toString() {
            return "Factory{" +
                    "valueClass=" + valueClass +
                    '}';
        }
    }

    public static <LCF> ServiceResponse1<LCF> loading(
            Class<LCF> valueClass,
            LCF value
    ) {
        return new ServiceResponse1.Loading<>(
                valueClass,
                value
        );
    }

    public static <LCF> ServiceResponse1<LCF> complete(
            Class<LCF> valueClass,
            LCF value
    ) {
        return new ServiceResponse1.Complete<>(
                valueClass,
                value
        );
    }

    public static <LCF> ServiceResponse1<LCF> failed(
            Class<LCF> valueClass,
            LCF value
    ) {
        return new ServiceResponse1.Failed<>(
                valueClass,
                value
        );
    }

    private ServiceResponse1() {
    }

    public abstract void act(
            VoidFunction<Loading<LCF>> loadingVoidFunction,
            VoidFunction<Complete<LCF>> completeVoidFunction,
            VoidFunction<Failed<LCF>> failedVoidFunction
    );

    public abstract <R> R reduce(
            Function<Loading<LCF>, R> loadingFunction,
            Function<Complete<LCF>, R> completeFunction,
            Function<Failed<LCF>, R> failedFunction
    );

    public final ServiceResponse2LC<LCF, LCF> toServiceResponse2() {
        return ServiceResponse2LC.fromServiceResponse1(this);
    }

    public static final class Loading<L> extends ServiceResponse1<L> {
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
                VoidFunction<Loading<L>> loadingVoidFunction,
                VoidFunction<Complete<L>> completeVoidFunction,
                VoidFunction<Failed<L>> failedVoidFunction
        ) {
            loadingVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<L>, R> loadingFunction,
                Function<Complete<L>, R> completeFunction,
                Function<Failed<L>, R> failedFunction
        ) {
            return loadingFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Loading<?> loading = (Loading<?>) o;
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

    public static final class Complete<C> extends ServiceResponse1<C> {
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
                VoidFunction<Loading<C>> loadingVoidFunction,
                VoidFunction<Complete<C>> completeVoidFunction,
                VoidFunction<Failed<C>> failedVoidFunction
        ) {
            completeVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<C>, R> loadingFunction,
                Function<Complete<C>, R> completeFunction,
                Function<Failed<C>, R> failedFunction
        ) {
            return completeFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Complete<?> complete = (Complete<?>) o;
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

    public static final class Failed<F> extends ServiceResponse1<F> {
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
                VoidFunction<Loading<F>> loadingVoidFunction,
                VoidFunction<Complete<F>> completeVoidFunction,
                VoidFunction<Failed<F>> failedVoidFunction
        ) {
            failedVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading<F>, R> loadingFunction,
                Function<Complete<F>, R> completeFunction,
                Function<Failed<F>, R> failedFunction
        ) {
            return failedFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Failed<?> failed = (Failed<?>) o;
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
