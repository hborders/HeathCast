package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DelegatingIdlingResourceTest {
    @Test
    public void testWhenExpectingIsBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        assertFalse(testObject.isIdleNow());
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
    public void testWhenExpectingSetHasWithBusyDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();

        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenExpectingSetHasWithIdleCallsResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");

        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenExpectingSetNotExpectingCallsResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithBusyIsBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        testObject.setState(
                DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource)
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenHasWithBusySetExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithBusySetHasWithBusyDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner1");
        basicIdlingResource1.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource2 = new BasicIdlingResource("inner2");
        basicIdlingResource2.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource2));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithBusySetHasWithIdleCallsResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner1");
        basicIdlingResource1.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource2 = new BasicIdlingResource("inner2");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource2));
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithBusySetNotExpectingCallsResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner");
        basicIdlingResource1.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithBusyCallsResourceCallbackWhenBecomesIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicIdlingResource.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithIdleIsIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        testObject.setState(
                DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource)
        );

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenHasWithIdleSetExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleSetHasWithBusyDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner1");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource2 = new BasicIdlingResource("inner2");
        basicIdlingResource2.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource2));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleSetHasWithIdleDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner1");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource2 = new BasicIdlingResource("inner2");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource2));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleSetNotExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource1 = new BasicIdlingResource("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleDoesntCallResourceCallbackWhenBecomesBusy() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicIdlingResource.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingIsIdle() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenNotExpectingSetExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingSetHasWithBusyDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        basicIdlingResource.setBusy();
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingSetHasWithIdleDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingSetNotExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = new DelegatingIdlingResource("test");
        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicIdlingResource basicIdlingResource = new BasicIdlingResource("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicIdlingResource));
        testResourceCallback.assertNotCalled();
    }
}
