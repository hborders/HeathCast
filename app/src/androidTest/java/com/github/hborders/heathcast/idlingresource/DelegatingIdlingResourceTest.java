package com.github.hborders.heathcast.idlingresource;

import org.junit.Test;

import static com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource.expectingInnerIdlingResource;
import static com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource.notExpectingInnerIdlingResource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DelegatingIdlingResourceTest {
    @Test
    public void testWhenExpectingIsBusy() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenExpectingSetExpectingDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenExpectingSetHasWithBusyDoesNotCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.busy("inner");

        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenExpectingSetHasWithIdleCallsResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.idle("inner");

        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenExpectingSetNotExpectingCallsResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithBusyIsBusy() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.busy("inner");
        testObject.setState(
                DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource)
        );

        assertFalse(testObject.isIdleNow());
    }

    @Test
    public void testWhenHasWithBusySetExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.busy("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithBusySetHasWithBusyDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource1 = BasicMutableIdlingResource.busy("inner1");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource2 = BasicMutableIdlingResource.busy("inner2");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource2));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithBusySetHasWithIdleCallsResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource1 = BasicMutableIdlingResource.busy("inner1");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource2 = BasicMutableIdlingResource.idle("inner2");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource2));
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithBusySetNotExpectingCallsResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource1 = BasicMutableIdlingResource.busy("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithBusyCallsResourceCallbackWhenBecomesIdle() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.busy("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicMutableIdlingResource.setIdle();
        testResourceCallback.assertCalledOnce();
    }

    @Test
    public void testWhenHasWithIdleIsIdle() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.idle("inner");
        testObject.setState(
                DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource)
        );

        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenHasWithIdleSetExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.idle("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleSetHasWithBusyDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource1 = BasicMutableIdlingResource.idle("inner1");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource2 = BasicMutableIdlingResource.busy("inner2");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource2));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleSetHasWithIdleDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource1 = BasicMutableIdlingResource.idle("inner1");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource2 = BasicMutableIdlingResource.idle("inner2");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource2));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleSetNotExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource1 = BasicMutableIdlingResource.idle("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource1));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.notExpectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenHasWithIdleDoesntCallResourceCallbackWhenBecomesBusy() {
        final DelegatingIdlingResource testObject = expectingInnerIdlingResource("test");
        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.idle("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        basicMutableIdlingResource.setBusy();
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingIsIdle() {
        final DelegatingIdlingResource testObject = notExpectingInnerIdlingResource("test");
        assertTrue(testObject.isIdleNow());
    }

    @Test
    public void testWhenNotExpectingSetExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = notExpectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        testObject.setState(DelegatingIdlingResource.State.expectingInnerIdlingResource());
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingSetHasWithBusyDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = notExpectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.busy("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingSetHasWithIdleDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = notExpectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.idle("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        testResourceCallback.assertNotCalled();
    }

    @Test
    public void testWhenNotExpectingSetNotExpectingDoesntCallResourceCallback() {
        final DelegatingIdlingResource testObject = notExpectingInnerIdlingResource("test");
        final TestResourceCallback testResourceCallback = new TestResourceCallback();
        testObject.registerIdleTransitionCallback(testResourceCallback);

        final BasicMutableIdlingResource basicMutableIdlingResource = BasicMutableIdlingResource.idle("inner");
        testObject.setState(DelegatingIdlingResource.State.hasInnerIdlingResource(basicMutableIdlingResource));
        testResourceCallback.assertNotCalled();
    }
}
