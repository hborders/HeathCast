package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class BasicIdlingResourceTest {

    @Test
    public void testInitiallyIdle() {
        final BasicIdlingResource testObject = BasicIdlingResource.idle("test");

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testInitiallyBusy() {
        final BasicIdlingResource testObject = BasicIdlingResource.busy("test");

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenIdleDoesNotCallResourceCallbackOnSetBusy() {
        final BasicIdlingResource testObject = BasicIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyDoesNotCallResourceCallbackOnSetBusy() {
        final BasicIdlingResource testObject = BasicIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyCallsResourceCallbackOnSetIdle() {
        final BasicIdlingResource testObject = BasicIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenIdleCallsResourceCallbackOnSetIdle() {
        final BasicIdlingResource testObject = BasicIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertNotCalled();
    }

}
