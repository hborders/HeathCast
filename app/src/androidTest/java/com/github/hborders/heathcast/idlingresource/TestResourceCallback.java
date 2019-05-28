package com.github.hborders.heathcast.idlingresource;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public final class TestResourceCallback implements IdlingResource.ResourceCallback {

    private final AtomicInteger callCount = new AtomicInteger();

    @Override
    public void onTransitionToIdle() {
        callCount.incrementAndGet();
    }

    public void assertNotCalled() {
        assertEquals(0, callCount.get());
    }

    public void assertCalledOnce() {
        assertEquals(1, callCount.get());
    }
}
