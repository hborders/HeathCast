package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public final class AndIdlingResourceTest {

    @Test
    public void testWhenLeftIdleAndRightIdleThenIdle() {
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.idle("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftIdleAndRightIdleDoesntCallCallbackWhenRightBecomesBusy() {
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.idle("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.idle("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.idle("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.idle("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.idle("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftIdleAndRightBusyCallsCallbackWhenRightBecomesIdle() {
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.idle("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.busy("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.idle("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.busy("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.busy("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.idle("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftBusyAndRightIdleDoesntCallCallbackWhenRightBecomesBusy() {
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.busy("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.idle("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.busy("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.idle("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.busy("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.busy("right");
        final AndIdlingResource testObject = AndIdlingResource.and(
                left,
                right
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenLeftBusyAndRightBusyDoesntCallCallbackWhenRightBecomesIdle() {
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.busy("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.busy("right");
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
        final BasicMutableIdlingResource left = BasicMutableIdlingResource.busy("left");
        final BasicMutableIdlingResource right = BasicMutableIdlingResource.busy("right");
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
