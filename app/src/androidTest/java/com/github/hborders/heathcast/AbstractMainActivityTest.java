package com.github.hborders.heathcast;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;
import com.github.hborders.heathcast.idlingresource.BooleanObservableIdlingResourceSubscriber;
import com.github.hborders.heathcast.idlingresource.DisposableIdlingResource;
import com.github.hborders.heathcast.idlingresource.ItemRangeOptionalObserableIdlingResourceSubscriber;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class AbstractMainActivityTest {
    @Nullable
    private volatile DisposableIdlingResource podcastSearchDisposableIdlingResource;
    @Nullable
    private volatile DisposableIdlingResource podcastSearchResultPodcastsItemRangeDisposableIdlingResource;

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

    protected final DisposableIdlingResource getPodcastSearchResultPodcastsItemRangeDisposableIdlingResource() {
        @Nullable final DisposableIdlingResource podcastSearchResultPodcastsItemRangeDisposableIdlingResource =
                this.podcastSearchResultPodcastsItemRangeDisposableIdlingResource;
        if (podcastSearchResultPodcastsItemRangeDisposableIdlingResource == null) {
            throw new AssertionError();
        }
        return podcastSearchResultPodcastsItemRangeDisposableIdlingResource;
    }

    @Before
    public void setupDisposableIdlingResources() {
        activityScenarioRule.getScenario().onActivity(
                mainActivity -> {
                    podcastSearchDisposableIdlingResource =
                            BooleanObservableIdlingResourceSubscriber.subscribe(
                                    "podcastSearching",
                                    mainActivity.getPodcastSearchingObservable().map(searching -> !searching)
                            );
                    podcastSearchResultPodcastsItemRangeDisposableIdlingResource =
                            ItemRangeOptionalObserableIdlingResourceSubscriber.subscribe(
                                    "podcastSearchResultsPodcastsItemRange",
                                    mainActivity.getPodcastSearchResultPodcastsItemRangeOptionalObservable()
                            );
                }
        );
    }

    @After
    public void tearDisposableIdlingResource() {
        List<Optional<DisposableIdlingResource>> disposableIdlingResourceOptionals =
                Arrays.asList(
                        Optional.ofNullable(podcastSearchDisposableIdlingResource),
                        Optional.ofNullable(podcastSearchResultPodcastsItemRangeDisposableIdlingResource)
                );
        for (final Optional<DisposableIdlingResource> disposableIdlingResourceOptional : disposableIdlingResourceOptionals) {
            disposableIdlingResourceOptional.ifPresent(
                    disposableIdlingResource -> {
                        IdlingRegistry.getInstance().unregister(disposableIdlingResource);
                        disposableIdlingResource.dispose();
                    }
            );
        }
    }

    @After
    public final void removeNetworkPausers() {
        activityScenarioRule.getScenario().onActivity(MainActivity::clearNetworkPausers);
    }
}
