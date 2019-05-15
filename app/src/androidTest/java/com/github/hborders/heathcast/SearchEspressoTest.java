package com.github.hborders.heathcast;

import android.view.KeyEvent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEspressoTest {

    private final ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() throws Exception {
        activityScenarioRule.getScenario().onActivity(mainActivity -> {
//            IdlingRegistry.getInstance().
//            mIdlingResource = mainActivity.getIdlingResource();
//            // To prove that the test fails, omit this call:
//            IdlingRegistry.getInstance().register(mIdlingResource);
        });
    }

    @Test
    public void searchForPlanetMoneyAndSubscribe() throws Exception {
        onView(withId(R.id.fragment_main_add_podcast_fab)).perform(click());
        onView(withId(R.id.fragment_podcast_search_search_view)).perform(
                typeText("Planet Money"),
                pressKey(KeyEvent.KEYCODE_ENTER)
        );

        // Need to replace this with an idling resource
        Thread.sleep(5 * 1000);

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).perform(
                actionOnItemAtPosition(0, click())
        );

        {
            final ViewInteraction subscribedButtonViewInteration =
                    onView(withId(R.id.fragment_podcast_subscribed_button));
            subscribedButtonViewInteration.check(
                    matches(
                            withText(R.string.fragment_podcast_not_subscribed)
                    )
            );
            subscribedButtonViewInteration.perform(
                    click()
            );
            subscribedButtonViewInteration.check(
                    matches(
                            withText(R.string.fragment_podcast_subscribed)
                    )
            );
        }

        Espresso.pressBack();

        // Need to replace this with an idling resource
        Thread.sleep(1000);

        onView(withId(R.id.fragment_podcast_list_podcasts_recycler_view)).perform(
                actionOnItemAtPosition(0, click())
        );

        onView(withId(R.id.fragment_podcast_subscribed_button)).check(
                matches(
                        withText(R.string.fragment_podcast_subscribed)
                )
        );
    }
}
