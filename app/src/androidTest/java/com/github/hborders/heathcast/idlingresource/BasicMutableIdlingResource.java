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
        IdlingResourceLog.v(
                this,
                "before registerIdleTransitionCallback"
        );
        this.resourceCallback = resourceCallback;
        if (resourceCallback == null) {
            IdlingResourceLog.v(
                    this,
                    "no registerIdleTransitionCallback, so no immediate transitionToIdle"
            );
        } else {
            if (idleNow) {
                IdlingResourceLog.v(
                        this,
                        "before immediate transitionToIdle"
                );
                resourceCallback.onTransitionToIdle();
                IdlingResourceLog.v(
                        this,
                        "after immediate transitionToIdle"
                );
            } else {
                IdlingResourceLog.v(
                        this,
                        "busy, so no immediate transitionToIdle"
                );
            }
        }
        IdlingResourceLog.v(
                this,
                "after registerIdleTransitionCallback"
        );
    }

    // Public API

    @Override
    public void setBusy() {
        IdlingResourceLog.v(
                this,
                "before busy"
        );
        idleNow = false;
        IdlingResourceLog.v(
                this,
                "after busy"
        );
    }

    @Override
    public void setIdle() {
        IdlingResourceLog.v(
                this,
                "before idle"
        );
        if (!idleNow) {
            idleNow = true;
            @Nullable final ResourceCallback resourceCallback = this.resourceCallback;
            if (resourceCallback == null) {
                IdlingResourceLog.v(
                        this,
                        "no resourceCallback"
                );
            } else {
                IdlingResourceLog.v(
                        this,
                        "before onTransitionToIdle"
                );
                resourceCallback.onTransitionToIdle();
                IdlingResourceLog.v(
                        this,
                        "after onTransitionToIdle"
                );
            }
        }
        IdlingResourceLog.v(
                this,
                "after idle"
        );
    }
}
