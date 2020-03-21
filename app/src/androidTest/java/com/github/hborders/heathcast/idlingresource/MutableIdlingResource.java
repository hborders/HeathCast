package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

public interface MutableIdlingResource extends IdlingResource {
    void registerIdleTransitionCallback(@Nullable ResourceCallback callback);

    void setBusy();

    void setIdle();

    default void setIdleOrBusy(boolean idleTrueBusyFalse) {
        if (idleTrueBusyFalse) {
            setIdle();
        } else {
            setBusy();
        }
    }
}
