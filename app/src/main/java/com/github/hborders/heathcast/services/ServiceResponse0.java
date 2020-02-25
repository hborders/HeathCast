package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

public abstract class ServiceResponse0 {
    public static ServiceResponse0 loading() {
        return new ServiceResponse0.Loading();
    }

    public static ServiceResponse0 complete() {
        return new ServiceResponse0.Complete();
    }

    public static ServiceResponse0 failed() {
        return new ServiceResponse0.Failed();
    }

    private ServiceResponse0() {
    }

    public abstract void act(
            VoidFunction<Loading> loadingVoidFunction,
            VoidFunction<Complete> completeVoidFunction,
            VoidFunction<Failed> failedVoidFunction
    );

    public abstract <R> R reduce(
            Function<Loading, R> loadingFunction,
            Function<Complete, R> completeFunction,
            Function<Failed, R> failedFunction
    );

    public static final class Loading extends ServiceResponse0 {
        public Loading() {
        }

        @Override
        public void act(
                VoidFunction<Loading> loadingVoidFunction,
                VoidFunction<Complete> completeVoidFunction,
                VoidFunction<Failed> failedVoidFunction
        ) {
            loadingVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Complete, R> completeFunction,
                Function<Failed, R> failedFunction
        ) {
            return loadingFunction.apply(this);
        }

        @Override
        public int hashCode() {
            return Loading.class.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public String toString() {
            return "Loading{}";
        }
    }

    public static final class Complete extends ServiceResponse0 {
        @Override
        public void act(
                VoidFunction<Loading> loadingVoidFunction,
                VoidFunction<Complete> completeVoidFunction,
                VoidFunction<Failed> failedVoidFunction
        ) {
            completeVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Complete, R> completeFunction,
                Function<Failed, R> failedFunction
        ) {
            return completeFunction.apply(this);
        }

        @Override
        public int hashCode() {
            return Complete.class.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public String toString() {
            return "Complete{}";
        }
    }

    public static final class Failed extends ServiceResponse0 {
        @Override
        public void act(
                VoidFunction<Loading> loadingVoidFunction,
                VoidFunction<Complete> completeVoidFunction,
                VoidFunction<Failed> failedVoidFunction
        ) {
            failedVoidFunction.apply(this);
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Complete, R> completeFunction,
                Function<Failed, R> failedFunction
        ) {
            return failedFunction.apply(this);
        }

        @Override
        public int hashCode() {
            return Failed.class.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public String toString() {
            return "Failed{}";
        }
    }
}
