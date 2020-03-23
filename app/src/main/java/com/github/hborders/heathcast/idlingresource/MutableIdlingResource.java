package com.github.hborders.heathcast.idlingresource;

import javax.annotation.Nullable;

public final class MutableIdlingResource implements NullabilitiedIdlingResource {
    public static MutableIdlingResource idle(String name) {
        return new MutableIdlingResource(
                name,
                true
        );
    }

    public static MutableIdlingResource busy(String name) {
        return new MutableIdlingResource(
                name,
                false
        );
    }

    private final String name;

    private volatile boolean idleNow;

    @Nullable
    private volatile ResourceCallback resourceCallback;

    private MutableIdlingResource(String name, boolean idleNow) {
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
