package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DelegatingIdlingResourceTest {
    @Test
    public void testInitiallyBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testIdleWhenNotExpecting() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenExpectingSetExpectingDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenExpectingSetExpectingDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testSetInnerIdlingResourceToNullDoesntCallResourceCallbackWhenInnerIdlingResourceIsNull() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setInnerIdlingResource(null);
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testSettingInnerDelegateCallsResourceCallbackWhenInnerDelegateIsIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setInnerIdlingResource(basicIdlingResource);
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testSettingInnerDelegateDoesntCallResourceCallbackWhenInnerDelegateIsBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setInnerIdlingResource(basicIdlingResource);
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testCallsResourceCallbackWhenInnerDelegateBecomesIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        testObject.setInnerIdlingResource(basicIdlingResource);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicIdlingResource.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testDoesntCallResourceCallbackWhenOldInnerDelegateBecomesIdleAndNewInnerDelegateIsNull() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        testObject.setInnerIdlingResource(basicIdlingResource);
        testObject.setInnerIdlingResource(null);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicIdlingResource.setIdle();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testDoesntCallResourceCallbackWhenOldInnerDelegateBecomesIdleAndNewInnerDelegateIsNotNull() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner1");
        basicIdlingResource1.setBusy();
        testObject.setInnerIdlingResource(basicIdlingResource1);
        final BasicIdlingResource basicIdlingResource2 = new BasicIdlingResource("inner2");
        basicIdlingResource2.setBusy();
        testObject.setInnerIdlingResource(basicIdlingResource2);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicIdlingResource1.setIdle();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testCallsResourceCallbackWhenNewInnerDelegateBecomesIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner1");
        basicIdlingResource1.setBusy();
        testObject.setInnerIdlingResource(basicIdlingResource1);
        final BasicIdlingResource basicIdlingResource2 = new BasicIdlingResource("inner2");
        basicIdlingResource2.setBusy();
        testObject.setInnerIdlingResource(basicIdlingResource2);
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicIdlingResource1.setIdle();
        basicIdlingResource2.setIdle();
        testResourceCallback.assertCalledOnce();
    }
}
