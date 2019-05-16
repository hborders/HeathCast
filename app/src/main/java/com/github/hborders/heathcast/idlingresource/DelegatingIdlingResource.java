package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.Function;

import java.util.Objects;

import javax.annotation.Nullable;

public final class DelegatingIdlingResource implements IdlingResource {

    public static DelegatingIdlingResource expectingInnerIdlingResource(String name) {
        return new DelegatingIdlingResource(
                name,
                State.expectingInnerIdlingResource()
        );
    }

    public static DelegatingIdlingResource notExpectingInnerIdlingResource(String name) {
        return new DelegatingIdlingResource(
                name,
                State.notExpectingInnerIdlingResource()
        );
    }

    private final String name;
    private volatile State state;

    @Nullable
    private volatile ResourceCallback resourceCallback;

    private DelegatingIdlingResource(
            String name,
            State state
    ) {
        this.name = name;
        this.state = state;
    }

    // IdlingResource

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return state.reduce(
                expectingInnerIdlingResource -> false,
                hasInnerIdlingResource -> hasInnerIdlingResource.innerIdlingResource.isIdleNow(),
                notExpectingInnerIdlingResource -> true
        );
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    // Public API

    public void setState(State state) {
        if (!this.state.equals(state)) {
            final boolean wasIdle = isIdleNow();
            this.state = state;
            final boolean isIdle = isIdleNow();
            if (!wasIdle && isIdle) {
                @Nullable final ResourceCallback resourceCallback = this.resourceCallback;
                if (resourceCallback != null) {
                    resourceCallback.onTransitionToIdle();
                }
            }

            @Nullable final IdlingResource innerIdlingResource = state.innerIdlingResource;
            if (innerIdlingResource != null) {
                innerIdlingResource.registerIdleTransitionCallback(() -> {
                    if (this.state.innerIdlingResource == innerIdlingResource) {
                        @Nullable final ResourceCallback resourceCallback = this.resourceCallback;
                        if (resourceCallback != null) {
                            resourceCallback.onTransitionToIdle();
                        }
                    }
                });
            }
        }
    }

    public static abstract class State {
        public static ExpectingInnerIdlingResource expectingInnerIdlingResource() {
            return new ExpectingInnerIdlingResource();
        }

        public static HasInnerIdlingResource hasInnerIdlingResource(IdlingResource innerIdlingResource) {
            return new HasInnerIdlingResource(innerIdlingResource);
        }

        public static NotExpectingInnerIdlingResource notExpectingInnerIdlingResource() {
            return new NotExpectingInnerIdlingResource();
        }

        @Nullable
        public final IdlingResource innerIdlingResource;

        private State(@Nullable IdlingResource innerIdlingResource) {
            this.innerIdlingResource = innerIdlingResource;
        }

        public abstract <R> R reduce(
                Function<ExpectingInnerIdlingResource, R> expectingInnerIdlingResourceFunction,
                Function<HasInnerIdlingResource, R> hasInnerIdlingResourceFunction,
                Function<NotExpectingInnerIdlingResource, R> notExpectingInnerIdlingResourceFunction
        );

        public static final class ExpectingInnerIdlingResource extends State {
            private ExpectingInnerIdlingResource() {
                super(null);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                return o != null && getClass() == o.getClass();
            }

            @Override
            public int hashCode() {
                return Objects.hash(getClass());
            }

            @Override
            public String toString() {
                return "ExpectingInnerIdlingResource{}";
            }

            public <R> R reduce(
                    Function<ExpectingInnerIdlingResource, R> expectingInnerIdlingResourceFunction,
                    Function<HasInnerIdlingResource, R> hasInnerIdlingResourceFunction,
                    Function<NotExpectingInnerIdlingResource, R> notExpectingInnerIdlingResourceFunction
            ) {
                return expectingInnerIdlingResourceFunction.apply(this);
            }
        }

        public static final class HasInnerIdlingResource extends State {
            public final IdlingResource innerIdlingResource;

            private HasInnerIdlingResource(IdlingResource innerIdlingResource) {
                super(innerIdlingResource);
                this.innerIdlingResource = innerIdlingResource;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                HasInnerIdlingResource that = (HasInnerIdlingResource) o;
                return innerIdlingResource.equals(that.innerIdlingResource);
            }

            @Override
            public int hashCode() {
                return Objects.hash(innerIdlingResource);
            }

            @Override
            public String toString() {
                return "HasInnerIdlingResource{" +
                        "innerIdlingResource=" + innerIdlingResource +
                        '}';
            }

            public <R> R reduce(
                    Function<ExpectingInnerIdlingResource, R> expectingInnerIdlingResourceFunction,
                    Function<HasInnerIdlingResource, R> hasInnerIdlingResourceFunction,
                    Function<NotExpectingInnerIdlingResource, R> notExpectingInnerIdlingResourceFunction
            ) {
                return hasInnerIdlingResourceFunction.apply(this);
            }
        }

        public static final class NotExpectingInnerIdlingResource extends State {
            private NotExpectingInnerIdlingResource() {
                super(null);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                return o != null && getClass() == o.getClass();
            }

            @Override
            public int hashCode() {
                return Objects.hash(getClass());
            }

            @Override
            public String toString() {
                return "NotExpectingInnerIdlingResource{}";
            }

            public <R> R reduce(
                    Function<ExpectingInnerIdlingResource, R> expectingInnerIdlingResourceFunction,
                    Function<HasInnerIdlingResource, R> hasInnerIdlingResourceFunction,
                    Function<NotExpectingInnerIdlingResource, R> notExpectingInnerIdlingResourceFunction
            ) {
                return notExpectingInnerIdlingResourceFunction.apply(this);
            }
        }
    }
}
