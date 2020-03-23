package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

public final class RenamedIdlingResource implements NullabilitiedIdlingResource {
    public static RenamedIdlingResource prepend(
            String prefix,
            IdlingResource idlingResource
    ) {
        return new RenamedIdlingResource(
                prefix + idlingResource.getName(),
                idlingResource
        );
    }

    private final String name;
    private final IdlingResource idlingResource;

    public RenamedIdlingResource(
            String name,
            IdlingResource idlingResource
    ) {
        this.name = name;
        this.idlingResource = idlingResource;
    }

    @Override
    public String toString() {
        return "RenamedIdlingResource{" +
                "name='" + name + '\'' +
                ", idlingResource=" + idlingResource +
                '}';
    }

    // IdlingResource

    @Override
    public boolean isIdleNow() {
        return idlingResource.isIdleNow();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void registerIdleTransitionCallback(@Nullable ResourceCallback callback) {
        idlingResource.registerIdleTransitionCallback(callback);
    }
}
