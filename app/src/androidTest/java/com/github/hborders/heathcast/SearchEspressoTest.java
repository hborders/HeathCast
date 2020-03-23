package com.github.hborders.heathcast;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEspressoTest {

//    @Rule
//    public final ActivityScenarioRule<MainActivity> activityScenarioRule =
//            new ActivityScenarioRule<>(MainActivity.class);
//
//    @Nullable
//    private DisposableIdlingResource podcastSearchSubscribedIdlingResource;
//
//    @Before
//    public void registerIdlingResource() throws Exception {
//        activityScenarioRule.getScenario().onActivity(mainActivity -> {
//            final DisposableIdlingResource podcastSearchSubscribedIdlingResource =
//                    BooleanObservableIdlingResourceSubscriber.subscribe(
//                            "podcastSearchResults",
//                            mainActivity.getPodcastSearchingObservable().map(searching -> !searching)
//                    );
//            this.podcastSearchSubscribedIdlingResource =
//                    podcastSearchSubscribedIdlingResource;
//            IdlingRegistry.getInstance().register(podcastSearchSubscribedIdlingResource);
//        });
//    }
//
//    @After
//    public void unregisterIdlingResource() throws Exception {
//        @Nullable final DisposableIdlingResource podcastSearchSubscribedIdlingResource =
//                this.podcastSearchSubscribedIdlingResource;
//        if (podcastSearchSubscribedIdlingResource != null) {
//            podcastSearchSubscribedIdlingResource.dispose();
//            IdlingRegistry.getInstance().unregister(podcastSearchSubscribedIdlingResource);
//        }
//    }
//
//    @Test
//    public void initialSearchShowsNoResults() throws Exception {
//
//    }
//
//    @Test
//    public void searchForPlanetMoneyAndSubscribe() throws Exception {
//        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
//        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
//                typeText("Planet Money"),
//                pressKey(KeyEvent.KEYCODE_ENTER)
//        );
//
//        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).perform(
//                actionOnItemAtPosition(
//                        0,
//                        click()
//                )
//        );
//
//        {
//            final ViewInteraction subscribedButtonViewInteration =
//                    onView(withId(R.id.fragment_podcast_subscribed_button));
//            subscribedButtonViewInteration.check(
//                    matches(
//                            withText(R.string.fragment_podcast_not_subscribed)
//                    )
//            );
//            subscribedButtonViewInteration.perform(
//                    click()
//            );
//            subscribedButtonViewInteration.check(
//                    matches(
//                            withText(R.string.fragment_podcast_subscribed)
//                    )
//            );
//        }
//
//        Espresso.pressBack();
//
//        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).perform(
//                actionOnItemAtPosition(
//                        0,
//                        click()
//                )
//        );
//
//        onView(withId(R.id.fragment_podcast_subscribed_button)).check(
//                matches(
//                        withText(R.string.fragment_podcast_subscribed)
//                )
//        );
//    }
//
//    @Test
//    public void searchForPlanetMoneyAndViewPlanetMoneyAndThenSearchForModernLove() throws Exception {
//        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
//        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
//                typeText("Planet Money"),
//                pressKey(KeyEvent.KEYCODE_ENTER)
//        );
//        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).check(
//                selectedDescendantsMatch(
//                        atPosition(0),
//                        withChild(withText("Planet Money"))
//                )
//        );
//        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).perform(
//                actionOnItemAtPosition(
//                        0,
//                        click()
//                )
//        );
//
//        Espresso.pressBack();
//
//        onView(
//                allOf(
//                        isAssignableFrom(EditText.class),
//                        withAncestor(withId(R.id.fragment_podcast_search_search_view))
//                )
//        ).perform(clearText());
//
//        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
//                typeText("Modern Love"),
//                pressKey(KeyEvent.KEYCODE_ENTER)
//        );
//
//        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view))
//                .check(selectedDescendantsMatch(
//                        atPosition(0),
//                        withChild(withText("Modern Love"))
//                        )
//                );
//    }
}
