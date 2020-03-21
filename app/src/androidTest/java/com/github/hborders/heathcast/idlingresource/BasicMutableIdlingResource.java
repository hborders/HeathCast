package com.github.hborders.heathcast.idlingresource;

import javax.annotation.Nullable;

public final class BasicMutableIdlingResource implements MutableIdlingResource {

    public static BasicMutableIdlingResource idle(String name) {
        return new BasicMutableIdlingResource(
                name,
                true
        );
    }

    public static BasicMutableIdlingResource busy(String name) {
        return new BasicMutableIdlingResource(
                name,
                false
        );
    }

    private final String name;

    private volatile boolean idleNow;

    @Nullable
    private volatile ResourceCallback resourceCallback;

    private BasicMutableIdlingResource(String name, boolean idleNow) {
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
    public void registerIdleTransitionCallback(@Nullable ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    // Public API

    @Override
    public void setBusy() {
        idleNow = false;
    }

    @Override
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
