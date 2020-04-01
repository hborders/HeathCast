package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MutableIdlingResourceTest {

    @Test
    public void test_InitiallyIdle() {
        final MutableIdlingResource testObject = MutableIdlingResource.idle("test");

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void test_InitiallyBusy() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void test_WhenIdleDoesNotCallResourceCallbackOnSetBusy_IsNotIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        testObject.setBusy();

        assertFalse(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenBusyDoesNotCallResourceCallbackOnSetBusy_IsNotIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setBusy();

        assertFalse(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenBusyCallsResourceCallbackOnSetIdle_IsIdle_CallbackCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setIdle();

        assertTrue(testObject.isIdleNow());
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void test_WhenIdleCallsResourceCallbackOnSetIdle_IsIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.idle("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        testObject.setIdle();

        assertTrue(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenBusyCallsDeclerate_IsNotIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.decelerate();

        assertFalse(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenDeceleratingCallsMaybeSetIdle_IsIdle_CallbackCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final MutableIdlingResource.Deceleration deceleration = testObject.decelerate();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        deceleration.maybeSetIdle();

        assertTrue(testObject.isIdleNow());
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void test_WhenBusyAfterDeceleratingCallsMaybeSetIdle_IsNotIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final MutableIdlingResource.Deceleration oldDeceleration = testObject.decelerate();
        testObject.setBusy();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        oldDeceleration.maybeSetIdle();

        assertFalse(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenDeceleratingCallsOldMaybeSetIdle_IsNotIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final MutableIdlingResource.Deceleration oldDeceleration = testObject.decelerate();
        testObject.decelerate();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        oldDeceleration.maybeSetIdle();

        assertFalse(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenIdleWithoutDeceleratingCallsOldDeceleration_IsIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final MutableIdlingResource.Deceleration oldDeceleration = testObject.decelerate();
        testObject.setIdle();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        oldDeceleration.maybeSetIdle();

        assertTrue(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void test_WhenIdleAfterDeceleratingCallsMaybeSetIdleAgain_IsIdle_CallbackNotCalled() {
        final MutableIdlingResource testObject = MutableIdlingResource.busy("test");
        final MutableIdlingResource.Deceleration deceleration = testObject.decelerate();
        deceleration.maybeSetIdle();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        deceleration.maybeSetIdle();

        assertTrue(testObject.isIdleNow());
        testResourceCallback.assertNotCalled();
    }
}
