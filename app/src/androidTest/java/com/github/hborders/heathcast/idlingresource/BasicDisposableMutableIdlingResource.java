package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;

import io.reactivex.disposables.Disposable;

public final class BasicDisposableMutableIdlingResource implements DisposableMutableIdlingResource {
    private final Disposable disposable;
    private final MutableIdlingResource mutableIdlingResource;

    public BasicDisposableMutableIdlingResource(
            Disposable disposable,
            MutableIdlingResource mutableIdlingResource
    ) {
        this.disposable = disposable;
        this.mutableIdlingResource = mutableIdlingResource;
    }

    @Override
    public String toString() {
        return "BasicDisposableMutableIdlingResource{" +
                "disposable=" + disposable +
                ", mutableIdlingResource=" + mutableIdlingResource +
                '}';
    }

    // Disposable

    @Override
    public void dispose() {
        disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return disposable.isDisposed();
    }

    // IdlingResource

    @Override
    public String getName() {
        return mutableIdlingResource.getName();
    }

    @Override
    public boolean isIdleNow() {
        return mutableIdlingResource.isIdleNow();
    }

    @Override
    public void registerIdleTransitionCallback(@Nullable ResourceCallback callback) {
        mutableIdlingResource.registerIdleTransitionCallback(callback);
    }

    // MutableIdlingResource

    @Override
    public void setBusy() {
        mutableIdlingResource.setBusy();
    }

    @Override
    public void setIdle() {
        mutableIdlingResource.setIdle();
    }
}
