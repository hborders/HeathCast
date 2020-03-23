package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

public interface NullabilitiedIdlingResource extends IdlingResource {
    void registerIdleTransitionCallback(@Nullable ResourceCallback callback);
}
