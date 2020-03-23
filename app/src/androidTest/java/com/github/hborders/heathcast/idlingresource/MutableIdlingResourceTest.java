package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MutableIdlingResourceTest {

    @Test
    public void testInitiallyIdle() {
        final MutableIdlingResource testObject = MutableIdlingResource.idle("test");

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testInitiallyBusy() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenIdleDoesNotCallResourceCallbackOnSetBusy() {
        final MutableIdlingResource testObject = MutableIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyDoesNotCallResourceCallbackOnSetBusy() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyCallsResourceCallbackOnSetIdle() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenIdleCallsResourceCallbackOnSetIdle() {
        final MutableIdlingResource testObject = MutableIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertNotCalled();
    }
}
