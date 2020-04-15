package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

public final class AndIdlingResource implements NullabilitiedIdlingResource {

    public static AndIdlingResource and(
            IdlingResource leftIdlingResource,
            IdlingResource rightIdlingResource
    ) {
        final AndIdlingResource andIdlingResource = new AndIdlingResource(
                leftIdlingResource.getName() + " && " + rightIdlingResource.getName(),
                leftIdlingResource,
                rightIdlingResource
        );
        andIdlingResource.registerLeftAndRightIdleTransitionCallbacks();
        return andIdlingResource;
    }

    private final String name;
    private final IdlingResource leftIdlingResource;
    private final IdlingResource rightIdlingResource;

    @Nullable
    private volatile ResourceCallback resourceCallback;

    public AndIdlingResource(
            String name,
            IdlingResource leftIdlingResource,
            IdlingResource rightIdlingResource
    ) {
        this.name = name;
        this.leftIdlingResource = leftIdlingResource;
        this.rightIdlingResource = rightIdlingResource;
    }

    private void registerLeftAndRightIdleTransitionCallbacks() {
        final ResourceCallback leftAndRightIdleTransitionCallback = () -> {
            if (isIdleNow()) {
                @Nullable final ResourceCallback resourceCallback = this.resourceCallback;
                if (resourceCallback != null) {
                    resourceCallback.onTransitionToIdle();
                }
            }
        };
        leftIdlingResource.registerIdleTransitionCallback(leftAndRightIdleTransitionCallback);
        rightIdlingResource.registerIdleTransitionCallback(leftAndRightIdleTransitionCallback);
    }

    @Override
    public String toString() {
        return "AndIdlingResource{" +
                "name='" + name + '\'' +
                ", leftIdlingResource=" + leftIdlingResource +
                ", rightIdlingResource=" + rightIdlingResource +
                ", resourceCallback=" + resourceCallback +
                '}';
    }

    // IdlingResource

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return leftIdlingResource.isIdleNow() && rightIdlingResource.isIdleNow();
    }

    @Override
    public void registerIdleTransitionCallback(@Nullable ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
