package com.github.hborders.heathcast;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;
import com.github.hborders.heathcast.idlingresource.BooleanObservableIdlingResourceSubscriber;
import com.github.hborders.heathcast.idlingresource.DisposableIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import javax.annotation.Nullable;

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class AbstractMainActivityTest {
    @Nullable
    private volatile DisposableIdlingResource podcastSearchDisposableIdlingResource;

    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    protected final DisposableIdlingResource getPodcastSearchDisposableIdlingResource() {
        @Nullable final DisposableIdlingResource podcastSearchDisposableIdlingResource =
                this.podcastSearchDisposableIdlingResource;
        if (podcastSearchDisposableIdlingResource == null) {
            throw new AssertionError();
        }
        return podcastSearchDisposableIdlingResource;
    }

    @Before
    public void setupPodcastSearchDisposableIdlingResource() {
        activityScenarioRule.getScenario().onActivity(
                mainActivity -> {
                    podcastSearchDisposableIdlingResource =
                            BooleanObservableIdlingResourceSubscriber.subscribe(
                                    "podcastSearchResults",
                                    mainActivity.getPodcastSearchingObservable().map(searching -> !searching)
                            );
                }
        );
    }

    @After
    public void tearDownPodcastSearchDisposableIdlingResource() {
        @Nullable final DisposableIdlingResource podcastSearchDisposableIdlingResource =
                this.podcastSearchDisposableIdlingResource;
        if (podcastSearchDisposableIdlingResource != null) {
            podcastSearchDisposableIdlingResource.dispose();
            IdlingRegistry.getInstance().unregister(podcastSearchDisposableIdlingResource);
        }
    }

    @After
    public final void removeNetworkPausers() {
        activityScenarioRule.getScenario().onActivity(MainActivity::clearNetworkPausers);
    }
}
