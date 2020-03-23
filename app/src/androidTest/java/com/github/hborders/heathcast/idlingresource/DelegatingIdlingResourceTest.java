package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DelegatingIdlingResourceTest {
    @Test
    public void testWhenNoDelegateIsBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenNoDelegateSetNullDelegateDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setDelegateIdlingResource(null);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNullDelegateSetBusyDelegateDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.busy("inner");

        testObject.setDelegateIdlingResource(mutableIdlingResource);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNullDelegateSetWithDelegateIdleCallsResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.idle("inner");

        testObject.setDelegateIdlingResource(mutableIdlingResource);

        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenBusyDelegateIsBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.busy("inner");
        testObject.setDelegateIdlingResource(mutableIdlingResource);

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenBusyDelegateSetNullDelegateDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.busy("inner");
        testObject.setDelegateIdlingResource(mutableIdlingResource);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        testObject.setDelegateIdlingResource(null);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyDelegateSetBusyDelegateDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource1 = MutableIdlingResource.busy("inner1");
        testObject.setDelegateIdlingResource(mutableIdlingResource1);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        final MutableIdlingResource mutableIdlingResource2 = MutableIdlingResource.busy("inner2");
        testObject.setDelegateIdlingResource(mutableIdlingResource2);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenBusyDelegateSetIdleDelegateCallsResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource1 = MutableIdlingResource.busy("inner1");
        testObject.setDelegateIdlingResource(mutableIdlingResource1);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        final MutableIdlingResource mutableIdlingResource2 = MutableIdlingResource.idle("inner2");
        testObject.setDelegateIdlingResource(mutableIdlingResource2);

        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testBusyDelegateCallsResourceCallbackWhenItBecomesIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.busy("inner");
        testObject.setDelegateIdlingResource(mutableIdlingResource);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        mutableIdlingResource.setIdle();

        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenIdleDelegateIsIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.idle("inner");
        testObject.setDelegateIdlingResource(mutableIdlingResource);

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenIdleDelegateSetNullDelegateDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.idle("inner");
        testObject.setDelegateIdlingResource(mutableIdlingResource);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        testObject.setDelegateIdlingResource(null);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenIdleDelegateSetBusyDelegateDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource1 = MutableIdlingResource.idle("inner1");
        testObject.setDelegateIdlingResource(mutableIdlingResource1);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        final MutableIdlingResource mutableIdlingResource2 = MutableIdlingResource.busy("inner2");
        testObject.setDelegateIdlingResource(mutableIdlingResource2);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenIdleDelegateSetIdleDelegateDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource1 = MutableIdlingResource.idle("inner1");
        testObject.setDelegateIdlingResource(mutableIdlingResource1);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        final MutableIdlingResource mutableIdlingResource2 = MutableIdlingResource.idle("inner2");
        testObject.setDelegateIdlingResource(mutableIdlingResource2);

        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenIdleDelegateDoesntCallResourceCallbackWhenItBecomesBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final MutableIdlingResource mutableIdlingResource = MutableIdlingResource.idle("inner");
        testObject.setDelegateIdlingResource(mutableIdlingResource);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        testResourceCallback.reset();

        mutableIdlingResource.setBusy();

        testResourceCallback.assertNotCalled();
    }
}
