package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.Objects;

public final class ServiceResponse<T> {
    public final T value;
    public final RemoteStatus remoteStatus;

    public ServiceResponse(
            T value,
            RemoteStatus remoteStatus
    ) {
        this.value = value;
        this.remoteStatus = remoteStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceResponse<?> that = (ServiceResponse<?>) o;
        return value.equals(that.value) &&
                remoteStatus.equals(that.remoteStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, remoteStatus);
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "value=" + value +
                ", remoteStatus=" + remoteStatus +
                '}';
    }

    public static abstract class RemoteStatus {
        public static RemoteStatus loading() {
            return Loading.INSTANCE;
        }

        public static RemoteStatus failed(Throwable throwable) {
            return new Failed(throwable);
        }

        public static RemoteStatus complete() {
            return Complete.INSTANCE;
        }

        private RemoteStatus() {
        }

        public abstract void act(
                VoidFunction<Loading> loadingVoidFunction,
                VoidFunction<Failed> failedVoidFunction,
                VoidFunction<Complete> completeVoidFunction
        );

        public abstract <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Failed, R> failedFunction,
                Function<Complete, R> completeFunction
        );

        public static final class Loading extends RemoteStatus {
            private static final Loading INSTANCE = new Loading();

            private Loading() {
            }

            @Override
            public String toString() {
                return "Loading{}";
            }

            @Override
            public void act(
                    VoidFunction<Loading> loadingVoidFunction,
                    VoidFunction<Failed> failedVoidFunction,
                    VoidFunction<Complete> completeVoidFunction
            ) {
                loadingVoidFunction.apply(this);
            }

            @Override
            public <R> R reduce(
                    Function<Loading, R> loadingFunction,
                    Function<Failed, R> failedFunction,
                    Function<Complete, R> completeFunction
            ) {
                return loadingFunction.apply(this);
            }
        }

        public static final class Failed extends RemoteStatus {
            public final Throwable throwable;

            private Failed(Throwable throwable) {
                this.throwable = throwable;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Failed failed = (Failed) o;
                return throwable.equals(failed.throwable);
            }

            @Override
            public int hashCode() {
                return Objects.hash(throwable);
            }

            @Override
            public String toString() {
                return "Failed{" +
                        "throwable=" + throwable +
                        '}';
            }

            @Override
            public void act(
                    VoidFunction<Loading> loadingVoidFunction,
                    VoidFunction<Failed> failedVoidFunction,
                    VoidFunction<Complete> completeVoidFunction
            ) {
                failedVoidFunction.apply(this);
            }

            @Override
            public <R> R reduce(
                    Function<Loading, R> loadingFunction,
                    Function<Failed, R> failedFunction,
                    Function<Complete, R> completeFunction
            ) {
                return failedFunction.apply(this);
            }
        }

        public static final class Complete extends RemoteStatus {
            private static final Complete INSTANCE = new Complete();

            private Complete() {
            }

            @Override
            public String toString() {
                return "Complete{}";
            }

            @Override
            public void act(
                    VoidFunction<Loading> loadingVoidFunction,
                    VoidFunction<Failed> failedVoidFunction,
                    VoidFunction<Complete> completeVoidFunction
            ) {
                completeVoidFunction.apply(this);
            }

            @Override
            public <R> R reduce(
                    Function<Loading, R> loadingFunction,
                    Function<Failed, R> failedFunction,
                    Function<Complete, R> completeFunction
            ) {
                return completeFunction.apply(this);
            }
        }
    }
}
