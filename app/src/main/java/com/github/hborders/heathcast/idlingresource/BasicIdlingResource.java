package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import javax.annotation.Nullable;

public final class BasicIdlingResource implements IdlingResource {

    private final String name;

    private volatile boolean idleNow = true;

    @Nullable
    private volatile ResourceCallback resourceCallback;

    public BasicIdlingResource(String name) {
        this.name = name;
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
