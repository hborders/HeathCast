package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;

import java.util.Objects;

// this is specifically not generic because the service request has a separate state from
// the network request. We could have a failed remote request, but still have
// cached results.

// TODO break this apart into separate classes:
// Location: Local/Remote
// Load: Loading/Loaded/Failure
// This way, we can show our states as Location * Load:
// Local Loading, Remote Loading
// Local Loading, Remote Loaded
// Local Loading, Remote Failure
// Local Loaded, Remote Loading
// Local Loaded, Remote Loaded
// Local Loaded, Remote Failure
// Local Failure, Remote Loading
// Local Failure, Remote Loaded
// Local Failure, Remote Failure
public abstract class ServiceRequestState {
    public static ServiceRequestState loading() {
        return new Loading();
    }

    public static ServiceRequestState loaded() {
        return new Loaded();
    }

    public static ServiceRequestState localFailure(Throwable throwable) {
        return new LocalFailure(throwable);
    }

    public static ServiceRequestState remoteFailure(Throwable throwable) {
        return new RemoteFailure(throwable);
    }

    private ServiceRequestState() {
    }

    public abstract <R> R reduce(
            Function<Loading, R> loadingFunction,
            Function<Loaded, R> loadedFunction,
            Function<LocalFailure, R> localFailureFunction,
            Function<RemoteFailure, R> remoteFailureFunction
    );

    public static final class Loading extends ServiceRequestState {
        private Loading() {
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            return (obj != null && getClass() == obj.getClass());
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "Loading{}";
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Loaded, R> loadedFunction,
                Function<LocalFailure, R> localFailureFunction,
                Function<RemoteFailure, R> remoteFailureFunction
        ) {
            return loadingFunction.apply(this);
        }
    }

    public static final class Loaded extends ServiceRequestState {
        private Loaded() {
        }

        @Override
        public final boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            return (obj != null && getClass() == obj.getClass());
        }

        @Override
        public final int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "Loaded{}";
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Loaded, R> loadedFunction,
                Function<LocalFailure, R> localFailureFunction,
                Function<RemoteFailure, R> remoteFailureFunction
        ) {
            return loadedFunction.apply(this);
        }
    }

    public static final class LocalFailure extends ServiceRequestState {
        public final Throwable throwable;

        private LocalFailure(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LocalFailure that = (LocalFailure) o;
            return throwable.equals(that.throwable);
        }

        @Override
        public int hashCode() {
            return Objects.hash(throwable);
        }

        @Override
        public String toString() {
            return "LocalFailure{" +
                    "throwable=" + throwable +
                    '}';
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Loaded, R> loadedFunction,
                Function<LocalFailure, R> localFailureFunction,
                Function<RemoteFailure, R> remoteFailureFunction
        ) {
            return localFailureFunction.apply(this);
        }
    }

    public static final class RemoteFailure extends ServiceRequestState {
        public final Throwable throwable;

        private RemoteFailure(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RemoteFailure remoteFailure = (RemoteFailure) o;
            return throwable.equals(remoteFailure.throwable);
        }

        @Override
        public int hashCode() {
            return Objects.hash(throwable);
        }

        @Override
        public String toString() {
            return "RemoteFailure{" +
                    "throwable=" + throwable +
                    '}';
        }

        @Override
        public <R> R reduce(
                Function<Loading, R> loadingFunction,
                Function<Loaded, R> loadedFunction,
                Function<LocalFailure, R> localFailureFunction,
                Function<RemoteFailure, R> remoteFailureFunction
        ) {
            return remoteFailureFunction.apply(this);
        }
    }
}
