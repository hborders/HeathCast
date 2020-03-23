package com.github.hborders.heathcast;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;
import com.github.hborders.heathcast.services.NetworkPauser;

import org.junit.After;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class AbstractMainActivityTest {
    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    public interface GetIdlingResource {
        IdlingResource getIdlingResource(MainActivity mainActivity);
    }

    private final ArrayList<IdlingResource> idlingResources = new ArrayList<>();

    public final IdlingResource getIdlingResource(GetIdlingResource getIdlingResource) {
        final AtomicReference<IdlingResource> idlingResourceAtomicReference = new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(
                mainActivity ->
                        idlingResourceAtomicReference.set(
                                getIdlingResource.getIdlingResource(mainActivity)
                        )
        );
        @Nullable final IdlingResource idlingResource = idlingResourceAtomicReference.get();
        if (idlingResource == null) {
            throw new AssertionError();
        }
        idlingResources.add(idlingResource);
        return idlingResource;
    }

    public interface SetNetworkPausers {
        void setNetworkPausers(
                MainActivity mainActivity,
                NetworkPauser... networkPausers
        );
    }

    public final void setNetworkPausers(
            SetNetworkPausers setNetworkPausers,
            NetworkPauser... networkPausers
    ) {
        activityScenarioRule.getScenario().onActivity(
                mainActivity ->
                        setNetworkPausers.setNetworkPausers(
                                mainActivity,
                                networkPausers
                        )
        );
    }

    @After
    public final void unregisterIdlingResources() {
        idlingResources.forEach(IdlingRegistry.getInstance()::unregister);
    }

    @After
    public final void removeNetworkPausers() {
        activityScenarioRule.getScenario().onActivity(MainActivity::clearNetworkPausers);
    }
}
