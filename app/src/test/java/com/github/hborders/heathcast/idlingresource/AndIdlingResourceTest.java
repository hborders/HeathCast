package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public final class AndIdlingResourceTest {

    @Test
    public void testWhenLeftIdleAndRightIdleThenIdle() {
        final BasicIdlingResource left = BasicIdlingResource.idle("left");
        final BasicIdlingResource right = BasicIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftIdleAndRightIdleDoesntCallCallbackWhenRightBecomesBusy() {
        final BasicIdlingResource left = BasicIdlingResource.idle("left");
        final BasicIdlingResource right = BasicIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        right.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenLeftIdleAndRightIdleDoesntCallCallbackWhenLeftBecomesBusy() {
        final BasicIdlingResource left = BasicIdlingResource.idle("left");
        final BasicIdlingResource right = BasicIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        left.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenLeftIdleAndRightBusyThenBusy() {
        final BasicIdlingResource left = BasicIdlingResource.idle("left");
        final BasicIdlingResource right = BasicIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftIdleAndRightBusyCallsCallbackWhenRightBecomesIdle() {
        final BasicIdlingResource left = BasicIdlingResource.idle("left");
        final BasicIdlingResource right = BasicIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        right.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenLeftIdleAndRightBusyDoesntCallCallbackWhenLeftBecomesBusy() {
        final BasicIdlingResource left = BasicIdlingResource.idle("left");
        final BasicIdlingResource right = BasicIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        left.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenLeftBusyAndRightIdleThenBusy() {
        final BasicIdlingResource left = BasicIdlingResource.busy("left");
        final BasicIdlingResource right = BasicIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftBusyAndRightIdleDoesntCallCallbackWhenRightBecomesBusy() {
        final BasicIdlingResource left = BasicIdlingResource.busy("left");
        final BasicIdlingResource right = BasicIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        right.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenLeftBusyAndRightIdleCallsCallbackWhenLeftBecomesIdle() {
        final BasicIdlingResource left = BasicIdlingResource.busy("left");
        final BasicIdlingResource right = BasicIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        left.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenLeftBusyAndRightBusyThenBusy() {
        final BasicIdlingResource left = BasicIdlingResource.busy("left");
        final BasicIdlingResource right = BasicIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftBusyAndRightBusyDoesntCallCallbackWhenRightBecomesIdle() {
        final BasicIdlingResource left = BasicIdlingResource.busy("left");
        final BasicIdlingResource right = BasicIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        right.setIdle();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenLeftBusyAndRightBusyDoesntCallCallbackWhenLeftBecomesIdle() {
        final BasicIdlingResource left = BasicIdlingResource.busy("left");
        final BasicIdlingResource right = BasicIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        left.setIdle();
        testResourceCallback.assertNotCalled();
    }
}
