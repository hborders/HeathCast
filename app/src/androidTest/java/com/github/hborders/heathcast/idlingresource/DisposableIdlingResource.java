package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.io.Closeable;

import io.reactivex.disposables.Disposable;

public interface DisposableIdlingResource extends Disposable, IdlingResource, Closeable {
    void registerIdleTransitionCallback(@Nullable ResourceCallback callback);

    default void close() {
        dispose();
    }
}
