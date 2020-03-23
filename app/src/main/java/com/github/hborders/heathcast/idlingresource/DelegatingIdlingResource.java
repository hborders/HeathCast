package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import javax.annotation.Nullable;

public final class DelegatingIdlingResource implements NullabilitiedIdlingResource {
    private final String name;
    @Nullable
    private volatile ResourceCallback resourceCallback;
    @Nullable
    private volatile IdlingResource delegateIdlingResource;

    public DelegatingIdlingResource(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DelegatingIdlingResource{" +
                "name='" + name + '\'' +
                ", resourceCallback=" + resourceCallback +
                ", delegateIdlingResource=" + delegateIdlingResource +
                '}';
    }

    // IdlingResource

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        @Nullable final IdlingResource delegateIdlingResource = this.delegateIdlingResource;
        if (delegateIdlingResource == null) {
            return false;
        } else {
            return delegateIdlingResource.isIdleNow();
        }
    }

    @Override
    public void registerIdleTransitionCallback(@Nullable ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;

        @Nullable final IdlingResource delegateIdlingResource = this.delegateIdlingResource;
        if (delegateIdlingResource == null) {
            // No delegate means we're busy. Don't transition to idle.
        } else {
            // let the delegate try to transition to idle.
            delegateIdlingResource.registerIdleTransitionCallback(resourceCallback);
        }
    }

    // Public API

    public void setDelegateIdlingResource(@Nullable IdlingResource delegateIdlingResource) {
        @Nullable final IdlingResource oldDelegateIdlingResource = this.delegateIdlingResource;
        if (oldDelegateIdlingResource == null) {
            // no old delegate, so nothing to clean up
        } else {
            // clean up the old resource callback
            oldDelegateIdlingResource.registerIdleTransitionCallback(null);
        }

        this.delegateIdlingResource = delegateIdlingResource;

        if (delegateIdlingResource == null) {
            // No delegate, so we don't need to register the callback
        } else {
            // resourceCallback might be null here, but that's ok
            delegateIdlingResource.registerIdleTransitionCallback(resourceCallback);
        }
    }
}
