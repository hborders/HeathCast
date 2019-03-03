package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.Subscription;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.is;

public final class SubscriptionMatchers {
    private SubscriptionMatchers() {
    }

    public static Matcher<Subscription> subscriptionPodcastIdentified(Matcher<Identified<Podcast>> podcastIdentifiedMatcher) {
        return new SubscriptionPodcastIdentifiedMatcher(podcastIdentifiedMatcher);
    }

    public static Matcher<Subscription> subscriptionPodcastIdentified(Identified<Podcast> podcastIdentified) {
        return subscriptionPodcastIdentified(is(podcastIdentified));
    }

    public static final class SubscriptionPodcastIdentifiedMatcher extends TypeSafeMatcher<Subscription> {
        private final Matcher<Identified<Podcast>> podIdentifiedMatcher;

        public SubscriptionPodcastIdentifiedMatcher(Matcher<Identified<Podcast>> podIdentifiedMatcher) {
            this.podIdentifiedMatcher = podIdentifiedMatcher;
        }

        @Override
        protected boolean matchesSafely(Subscription item) {
            return podIdentifiedMatcher.matches(item.podcastIdentified);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Subscription podcastIdentified matches ")
                    .appendDescriptionOf(podIdentifiedMatcher);
        }
    }
}
