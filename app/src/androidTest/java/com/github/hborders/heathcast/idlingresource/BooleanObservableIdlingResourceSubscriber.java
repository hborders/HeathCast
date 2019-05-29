package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

// can't just observe an optional. Need to actually observe whether a search is active
// thus, we have to actually expose the loading state, not just the effects
public final class BooleanObservableIdlingResourceSubscriber {
    public static SubscribedIdlingResource subscribe(
            String name,
            Observable<Boolean> idleObservable
    ) {
        final AtomicBoolean isIdleAtomicBoolean = new AtomicBoolean(false);
        final AtomicReference<IdlingResource.ResourceCallback> resourceCallbackAtomicReference =
                new AtomicReference<>();
        final Disposable disposable = idleObservable.subscribe(idle -> {
            isIdleAtomicBoolean.set(idle);
            if (idle) {
                @Nullable final IdlingResource.ResourceCallback resourceCallback = resourceCallbackAtomicReference.get();
                if (resourceCallback != null) {
                    resourceCallback.onTransitionToIdle();
                }
            }
        });

        return new SubscribedIdlingResource(
                name,
                isIdleAtomicBoolean,
                resourceCallbackAtomicReference,
                disposable
        );
    }

    public static class SubscribedIdlingResource implements IdlingResource, Disposable {
        private final String name;
        private final AtomicBoolean isIdleAtomicBoolean;
        private final AtomicReference<ResourceCallback> resourceCallbackAtomicReference;
        private final Disposable disposable;

        private SubscribedIdlingResource(
                String name,
                AtomicBoolean isIdleAtomicBoolean,
                AtomicReference<ResourceCallback> resourceCallbackAtomicReference,
                Disposable disposable
        ) {
            this.name = name;
            this.isIdleAtomicBoolean = isIdleAtomicBoolean;
            this.resourceCallbackAtomicReference = resourceCallbackAtomicReference;
            this.disposable = disposable;
        }

        @Override
        public String toString() {
            return "SubscribedIdlingResource{" +
                    "name='" + name + '\'' +
                    ", isIdleAtomicBoolean=" + isIdleAtomicBoolean +
                    ", resourceCallbackAtomicReference=" + resourceCallbackAtomicReference +
                    ", disposable=" + disposable +
                    '}';
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isIdleNow() {
            return isIdleAtomicBoolean.get();
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            resourceCallbackAtomicReference.set(resourceCallback);
        }

        @Override
        public void dispose() {
            disposable.dispose();
        }

        @Override
        public boolean isDisposed() {
            return disposable.isDisposed();
        }
    }
}
