package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A thread-safe IdlingResource that supports mutation and staged idling.
 *
 * Staged idling: Busy -> Decelerating -> Idle
 * If a #setBusy call happens while Decelerating, that Deceleration is invalid and no-ops.
 * If a #setIdle call happens while Decelerating, that Deceleration is invalid and no-ops.
 * After Deceleration#maybeSetIdle, that Deceleration is invalid and no-ops.
 */
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

    public final class Deceleration {
        private Deceleration() {
        }

        public void maybeSetIdle() {
            if (
                    decelerationAtomicReference.compareAndSet(
                            this,
                            null
                    )
            ) {
                setIdle();
            }
        }
    }

    private final String name;
    private final AtomicBoolean idleNow = new AtomicBoolean();

    private final AtomicReference<ResourceCallback> resourceCallbackAtomicReference = new AtomicReference<>();
    private final AtomicReference<Deceleration> decelerationAtomicReference = new AtomicReference<>();

    private MutableIdlingResource(String name, boolean idleNow) {
        this.name = name;
        this.idleNow.set(idleNow);
    }

    // IdlingResource

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return idleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(@Nullable ResourceCallback resourceCallback) {
        IdlingResourceLog.v(
                this,
                "before registerIdleTransitionCallback"
        );
        this.resourceCallbackAtomicReference.set(resourceCallback);
        if (resourceCallback == null) {
            IdlingResourceLog.v(
                    this,
                    "no registerIdleTransitionCallback, so no immediate transitionToIdle"
            );
        } else {
            if (idleNow.get()) {
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
        decelerationAtomicReference.set(null);
        idleNow.set(false);
        IdlingResourceLog.v(
                this,
                "after busy"
        );
    }

    @CheckResult
    public Deceleration decelerate() {
        final Deceleration deceleration = new Deceleration();
        decelerationAtomicReference.set(deceleration);
        return deceleration;
    }

    public void setIdle() {
        IdlingResourceLog.v(
                this,
                "before idle"
        );
        if (
                idleNow.compareAndSet(
                        false,
                        true)
        ) {
            // technically this isn't necessary since leaving the old deceleration hanging around
            // will just trigger a redundant setIdle() call, which is harmless, but I
            // feel safer keeping state consistent.
            decelerationAtomicReference.set(null);
            @Nullable final ResourceCallback resourceCallback =
                    this.resourceCallbackAtomicReference.get();
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
