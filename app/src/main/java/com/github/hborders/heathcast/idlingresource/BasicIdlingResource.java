package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import javax.annotation.Nullable;

public final class BasicIdlingResource implements IdlingResource {

    public static BasicIdlingResource idle(String name) {
        return new BasicIdlingResource(
                name,
                true
        );
    }

    public static BasicIdlingResource busy(String name) {
        return new BasicIdlingResource(
                name,
                false
        );
    }

    private final String name;

    private volatile boolean idleNow;

    @Nullable
    private volatile ResourceCallback resourceCallback;

    private BasicIdlingResource(String name, boolean idleNow) {
        this.name = name;
        this.idleNow = idleNow;
    }

    // IdlingResource

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return idleNow;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    // Public API

    public void setBusy() {
        idleNow = false;
    }

    public void setIdle() {
        if (!idleNow) {
            idleNow = true;
            @Nullable final ResourceCallback resourceCallback = this.resourceCallback;
            if (resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
        }
    }
}
