package com.github.hborders.heathcast;

import android.view.KeyEvent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.services.NetworkPauser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.github.hborders.heathcast.matcher.ViewMatchers.atPosition;
import static com.github.hborders.heathcast.viewaction.ClickSearchViewCloseButtonViewAction.clickSearchViewCloseButton;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEspressoTest2 extends AbstractMainActivityTest {

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
    public void searchForRandomTextShowsLoadingThenNoResults() throws Exception {
        final NetworkPauser searchForPodcasts2NetworkPauser = new NetworkPauser();
        activityScenarioRule.getScenario().onActivity(
                mainActivity -> {
                    mainActivity.setSearchForPodcasts2NetworkPausers(searchForPodcasts2NetworkPauser);
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

        IdlingRegistry.getInstance().register(getPodcastSearchDisposableIdlingResource());
        searchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                matches(
                        isDisplayed()
                )
        );
    }

    @Test
    public void searchForPlanetMoneyThenForRandomTextShowsNoResults() throws Exception {
        final String planetMoney = "Planet Money";
        final NetworkPauser planetMoneySearchForPodcasts2NetworkPauser = new NetworkPauser();
        final NetworkPauser randomTextSearchForPodcasts2NetworkPauser = new NetworkPauser();
        activityScenarioRule.getScenario().onActivity(
                mainActivity -> {
                    mainActivity.setSearchForPodcasts2NetworkPausers(
                            planetMoneySearchForPodcasts2NetworkPauser,
                            randomTextSearchForPodcasts2NetworkPauser
                    );
                }
        );

        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                typeText(planetMoney),
                pressKey(KeyEvent.KEYCODE_ENTER)
        );
        planetMoneySearchForPodcasts2NetworkPauser.resume();
        IdlingRegistry.getInstance().register(getPodcastSearchDisposableIdlingResource());

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).check(
                selectedDescendantsMatch(
                        atPosition(0),
                        withChild(withText(planetMoney))
                )
        );

        IdlingRegistry.getInstance().unregister(getPodcastSearchDisposableIdlingResource());

        onView(withId(R.id.fragment_podcast_search_search_view)).perform(clickSearchViewCloseButton());

        final String unmatchingText = UUID
                .randomUUID()
                .toString()
                .replaceAll(
                        "-",
                        ""
                );
        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                pressKey(KeyEvent.KEYCODE_DEL),
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

        IdlingRegistry.getInstance().register(getPodcastSearchDisposableIdlingResource());
        randomTextSearchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                matches(
                        isDisplayed()
                )
        );
    }
}
