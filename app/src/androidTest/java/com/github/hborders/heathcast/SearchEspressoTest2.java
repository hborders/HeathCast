package com.github.hborders.heathcast;

import android.view.KeyEvent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;
import com.github.hborders.heathcast.idlingresource.BooleanObservableIdlingResourceSubscriber;
import com.github.hborders.heathcast.services.NetworkPauser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEspressoTest2 {

    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void initialSearchShowsNoResults() throws Exception {
        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
        onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                matches(
                        isDisplayed()
                )
        );
    }

    @Test
    public void searchForUnmatchingTextShowsLoadingThenNoResults() throws Exception {
        final AtomicReference<BooleanObservableIdlingResourceSubscriber.SubscribedIdlingResource> podcastSearchSubscribedIdlingResourceAtomicReference =
                new AtomicReference<>();
        try {
            final NetworkPauser searchForPodcasts2NetworkPauser = new NetworkPauser();
            activityScenarioRule.getScenario().onActivity(
                    mainActivity -> {
                        mainActivity.searchForPodcasts2NetworkPauser = searchForPodcasts2NetworkPauser;

                        podcastSearchSubscribedIdlingResourceAtomicReference.set(
                                BooleanObservableIdlingResourceSubscriber.subscribe(
                                        "podcastSearchResults",
                                        mainActivity.getPodcastSearchingObservable().map(searching -> !searching)
                                )
                        );
                    }
            );
            onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
            final String unmatchingText = UUID
                    .randomUUID()
                    .toString()
                    .replaceAll(
                            "-",
                            ""
                    );
            onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                    typeText(unmatchingText),
                    pressKey(KeyEvent.KEYCODE_ENTER)
            );

            onView(withId(R.id.fragment_podcast_list_empty_loading_progress_bar)).check(
                    matches(
                            isDisplayed()
                    )
            );
            onView(withId(R.id.fragment_podcast_list_empty_loading_text_view)).check(
                    matches(
                            isDisplayed()
                    )
            );

            @Nullable final BooleanObservableIdlingResourceSubscriber.SubscribedIdlingResource podcastSearchSubscribedIdlingResource =
                    podcastSearchSubscribedIdlingResourceAtomicReference.get();
            if (podcastSearchSubscribedIdlingResource == null) {
                fail();
            }
            IdlingRegistry.getInstance().register(podcastSearchSubscribedIdlingResource);
            searchForPodcasts2NetworkPauser.resume();

            onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                    matches(
                            isDisplayed()
                    )
            );
        } finally {
            @Nullable final BooleanObservableIdlingResourceSubscriber.SubscribedIdlingResource podcastSearchSubscribedIdlingResource =
                    podcastSearchSubscribedIdlingResourceAtomicReference.get();
            if (podcastSearchSubscribedIdlingResource != null) {
                podcastSearchSubscribedIdlingResource.dispose();
                IdlingRegistry.getInstance().unregister(podcastSearchSubscribedIdlingResource);
            }
        }
    }
}
