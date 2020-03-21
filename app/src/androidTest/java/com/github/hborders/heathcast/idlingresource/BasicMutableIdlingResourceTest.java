package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class BasicMutableIdlingResourceTest {

    @Test
    public void testInitiallyIdle() {
        final BasicMutableIdlingResource testObject = BasicMutableIdlingResource.idle("test");

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testInitiallyBusy() {
        final BasicMutableIdlingResource testObject = BasicMutableIdlingResource.busy("test");

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenIdleDoesNotCallResourceCallbackOnSetBusy() {
        final BasicMutableIdlingResource testObject = BasicMutableIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyDoesNotCallResourceCallbackOnSetBusy() {
        final BasicMutableIdlingResource testObject = BasicMutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyCallsResourceCallbackOnSetIdle() {
        final BasicMutableIdlingResource testObject = BasicMutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenIdleCallsResourceCallbackOnSetIdle() {
        final BasicMutableIdlingResource testObject = BasicMutableIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertNotCalled();
    }

}
