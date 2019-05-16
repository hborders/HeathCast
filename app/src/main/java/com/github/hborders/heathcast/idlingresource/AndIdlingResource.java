package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import javax.annotation.Nullable;

public final class AndIdlingResource implements IdlingResource {

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

    private AndIdlingResource(
            String name, IdlingResource leftIdlingResource,
            IdlingResource rightIdlingResource
    ) {
        this.name = name;
        this.leftIdlingResource = leftIdlingResource;
        this.rightIdlingResource = rightIdlingResource;
    }

    private void registerLeftAndRightIdleTransitionCallbacks() {
        final ResourceCallback leftAndRightIdleTransitionCallback = () -> {
            if (isIdleNow()) {
                final ResourceCallback resourceCallback = this.resourceCallback;
                if (resourceCallback != null) {
                    resourceCallback.onTransitionToIdle();
                }
            }
        };
        leftIdlingResource.registerIdleTransitionCallback(leftAndRightIdleTransitionCallback);
        rightIdlingResource.registerIdleTransitionCallback(leftAndRightIdleTransitionCallback);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return leftIdlingResource.isIdleNow() && rightIdlingResource.isIdleNow();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
