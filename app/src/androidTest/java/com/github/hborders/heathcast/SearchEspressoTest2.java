package com.github.hborders.heathcast;

import android.view.KeyEvent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;
import com.github.hborders.heathcast.idlingresource.RxListFragmentModeOptionalIdlingResourceSubscriber;
import com.github.hborders.heathcast.reactivexandroid.RxListFragment;
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
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.github.hborders.heathcast.matcher.ViewMatchers.atPosition;
import static com.github.hborders.heathcast.viewaction.ClickSearchViewCloseButtonViewAction.clickSearchViewCloseButton;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEspressoTest2 extends AbstractMainActivityTest {
    private IdlingResource subscribePodcastSearchNotLoadingIdlingResource() {
        return subscribeIdlingResource(
                "PodcastSearchNotLoading",
                MainActivity::getPodcastSearchResultsPodcastListFragmentModeOptionalObservable,
                new RxListFragmentModeOptionalIdlingResourceSubscriber(
                        RxListFragment.Mode.COMPLETE_EMPTY,
                        RxListFragment.Mode.COMPLETE_NON_EMPTY,
                        RxListFragment.Mode.ERROR_EMPTY,
                        RxListFragment.Mode.ERROR_NON_EMPTY
                )
        );
    }

    private IdlingResource subscribePodcastSearchLoadingIdlingResource() {
        return subscribeIdlingResource(
                "PodcastSearchLoading",
                MainActivity::getPodcastSearchResultsPodcastListFragmentModeOptionalObservable,
                new RxListFragmentModeOptionalIdlingResourceSubscriber(
                        RxListFragment.Mode.LOADING_EMPTY,
                        RxListFragment.Mode.LOADING_NON_EMPTY
                )
        );
    }

    @Test
    public void initialSearchShowsNoResults() throws Exception {
        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
        onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                matches(
                        isDisplayed()
                )
        );
    }

    // This test is flaky because pressing enter doesn't immediately
    // affect the loading state. Need to update PodcastSearchFragment to force this
    @Test
    public void searchForRandomTextShowsLoadingThenNoResults() throws Exception {
        final IdlingResource podcastSearchLoadingIdlingResource = subscribePodcastSearchLoadingIdlingResource();
        final IdlingResource podcastSearchNotLoadingIdlingResource =
                subscribePodcastSearchNotLoadingIdlingResource();
        final NetworkPauser searchForPodcasts2NetworkPauser = new NetworkPauser();
        setNetworkPausers(
                MainActivity::setSearchForPodcasts2NetworkPausers,
                searchForPodcasts2NetworkPauser
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

        IdlingRegistry.getInstance().register(podcastSearchLoadingIdlingResource);
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
        IdlingRegistry.getInstance().unregister(podcastSearchLoadingIdlingResource);
        IdlingRegistry.getInstance().register(podcastSearchNotLoadingIdlingResource);
        searchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                matches(
                        isDisplayed()
                )
        );
    }

    @Test
    public void searchForPlanetMoneyThenForRandomTextShowsNoResults() throws Exception {
        final IdlingResource podcastSearchNotLoadingIdlingResource =
                subscribePodcastSearchNotLoadingIdlingResource();

        final String planetMoney = "Planet Money";
        final NetworkPauser planetMoneySearchForPodcasts2NetworkPauser = new NetworkPauser();
        final NetworkPauser randomTextSearchForPodcasts2NetworkPauser = new NetworkPauser();
        setNetworkPausers(
                MainActivity::setSearchForPodcasts2NetworkPausers,
                planetMoneySearchForPodcasts2NetworkPauser,
                randomTextSearchForPodcasts2NetworkPauser
        );

        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                typeText(planetMoney),
                pressKey(KeyEvent.KEYCODE_ENTER)
        );
        IdlingRegistry.getInstance().register(podcastSearchNotLoadingIdlingResource);
        planetMoneySearchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).check(
                selectedDescendantsMatch(
                        atPosition(0),
                        withChild(withText(planetMoney))
                )
        );

        IdlingRegistry.getInstance().unregister(podcastSearchNotLoadingIdlingResource);

        onView(withId(R.id.fragment_podcast_search_search_view)).perform(clickSearchViewCloseButton());

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

        IdlingRegistry.getInstance().register(podcastSearchNotLoadingIdlingResource);
        randomTextSearchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_empty_complete_text_view)).check(
                matches(
                        isDisplayed()
                )
        );
    }

    @Test
    public void searchForPlanetMoneyThenForModernLoveShowsModernLove() throws Exception {
        final String planetMoney = "Planet Money";
        final String modernLove = "Modern Love";
        final NetworkPauser planetMoneySearchForPodcasts2NetworkPauser = new NetworkPauser();
        final NetworkPauser modernLoveSearchForPodcasts2NetworkPauser = new NetworkPauser();
        setNetworkPausers(
                MainActivity::setSearchForPodcasts2NetworkPausers,
                planetMoneySearchForPodcasts2NetworkPauser,
                modernLoveSearchForPodcasts2NetworkPauser
        );

        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());

        final IdlingResource podcastSearchNotLoadingIdlingResource =
                subscribePodcastSearchNotLoadingIdlingResource();
        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                typeText(planetMoney),
                pressKey(KeyEvent.KEYCODE_ENTER)
        );
        IdlingRegistry.getInstance().register(podcastSearchNotLoadingIdlingResource);
        planetMoneySearchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).check(
                selectedDescendantsMatch(
                        atPosition(0),
                        withChild(withText("Planet Money"))
                )
        );

        IdlingRegistry.getInstance().unregister(podcastSearchNotLoadingIdlingResource);

        onView(withId(R.id.fragment_podcast_search_search_view)).perform(clickSearchViewCloseButton());

        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                typeText(modernLove),
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

        IdlingRegistry.getInstance().register(podcastSearchNotLoadingIdlingResource);
        modernLoveSearchForPodcasts2NetworkPauser.resume();

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).check(
                selectedDescendantsMatch(
                        atPosition(0),
                        withChild(withText("Modern Love"))
                )
        );
    }

    @Test
    public void searchForPlanetMoneyThenSelectPlanetMoneyThenSwitchBackDoesntReload() {
        final String planetMoney = "Planet Money";
        final NetworkPauser planetMoneySearchForPodcasts2NetworkPauser = new NetworkPauser();
        final NetworkPauser extraSearchForPodcasts2NetworkPauser = new NetworkPauser();
        setNetworkPausers(
                MainActivity::setSearchForPodcasts2NetworkPausers,
                planetMoneySearchForPodcasts2NetworkPauser,
                extraSearchForPodcasts2NetworkPauser
        );

        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                typeText(planetMoney),
                pressKey(KeyEvent.KEYCODE_ENTER)
        );
        final IdlingResource podcastSearchNotLoadingIdlingResource =
                subscribePodcastSearchNotLoadingIdlingResource();
        planetMoneySearchForPodcasts2NetworkPauser.resume();
        IdlingRegistry.getInstance().register(podcastSearchNotLoadingIdlingResource);

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).perform(
                actionOnItem(
                        withChild(withText(planetMoney)),
                        click()
                )
        );
    }
}