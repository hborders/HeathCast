package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public final class BasicIdlingResourceTest {

    @Test
    public void testInitiallyIdle() {
        final BasicIdlingResource testObject = new BasicIdlingResource("test");
        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testDoesNotCallResourceCallbackOnSetBusyWhenIdle() {
        final BasicIdlingResource testObject = new BasicIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testDoesNotCallResourceCallbackOnSetBusyWhenBusy() {
        final BasicIdlingResource testObject = new BasicIdlingResource("test");
        testObject.setBusy();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testCallsResourceCallbackOnSetIdleAfterWhenBusy() {
        final BasicIdlingResource testObject = new BasicIdlingResource("test");
        testObject.setBusy();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testCallsResourceCallbackOnSetIdleAfterWhenIdle() {
        final BasicIdlingResource testObject = new BasicIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();
        testResourceCallback.assertNotCalled();
    }

}
