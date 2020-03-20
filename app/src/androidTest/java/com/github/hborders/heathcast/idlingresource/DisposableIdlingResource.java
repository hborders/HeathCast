package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import java.io.Closeable;

import io.reactivex.disposables.Disposable;

public interface DisposableIdlingResource extends Disposable, IdlingResource, Closeable {
    default void close() {
        dispose();
    }
}
