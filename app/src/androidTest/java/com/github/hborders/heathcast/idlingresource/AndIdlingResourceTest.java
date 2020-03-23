package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public final class AndIdlingResourceTest {

    @Test
    public void testWhenLeftIdleAndRightIdleThenIdle() {
        final MutableIdlingResource left = MutableIdlingResource.idle("left");
        final MutableIdlingResource right = MutableIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftIdleAndRightIdleDoesntCallCallbackWhenRightBecomesBusy() {
        final MutableIdlingResource left = MutableIdlingResource.idle("left");
        final MutableIdlingResource right = MutableIdlingResource.idle("right");
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
        final MutableIdlingResource left = MutableIdlingResource.idle("left");
        final MutableIdlingResource right = MutableIdlingResource.idle("right");
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
        final MutableIdlingResource left = MutableIdlingResource.idle("left");
        final MutableIdlingResource right = MutableIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftIdleAndRightBusyCallsCallbackWhenRightBecomesIdle() {
        final MutableIdlingResource left = MutableIdlingResource.idle("left");
        final MutableIdlingResource right = MutableIdlingResource.busy("right");
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
        final MutableIdlingResource left = MutableIdlingResource.idle("left");
        final MutableIdlingResource right = MutableIdlingResource.busy("right");
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
        final MutableIdlingResource left = MutableIdlingResource.busy("left");
        final MutableIdlingResource right = MutableIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftBusyAndRightIdleDoesntCallCallbackWhenRightBecomesBusy() {
        final MutableIdlingResource left = MutableIdlingResource.busy("left");
        final MutableIdlingResource right = MutableIdlingResource.idle("right");
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
        final MutableIdlingResource left = MutableIdlingResource.busy("left");
        final MutableIdlingResource right = MutableIdlingResource.idle("right");
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
        final MutableIdlingResource left = MutableIdlingResource.busy("left");
        final MutableIdlingResource right = MutableIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftBusyAndRightBusyDoesntCallCallbackWhenRightBecomesIdle() {
        final MutableIdlingResource left = MutableIdlingResource.busy("left");
        final MutableIdlingResource right = MutableIdlingResource.busy("right");
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
        final MutableIdlingResource left = MutableIdlingResource.busy("left");
        final MutableIdlingResource right = MutableIdlingResource.busy("right");
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
