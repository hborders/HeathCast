package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.core.NonnullPair;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.is;

public final class NonnullPairMatchers {
    public static <F, S> Matcher<NonnullPair<F, S>> nonnullPairFirst(F first) {
        return nonnullPairFirst(is(first));
    }

    public static <F, S> Matcher<NonnullPair<F, S>> nonnullPairSecond(S second) {
        return nonnullPairSecond(is(second));
    }

    public static <F, S> Matcher<NonnullPair<F, S>> nonnullPair(
            Matcher<? super F> firstMatcher,
            Matcher<? super S> secondMatcher
    ) {
        return new NonnullPairBothMatcher<>(
                firstMatcher,
                secondMatcher
        );
    }

    public static <F, S> Matcher<NonnullPair<F, S>> nonnullPairFirst(Matcher<? super F> firstMatcher) {
        return new NonnullPairFirstMatcher<>(firstMatcher);
    }

    public static <F, S> Matcher<NonnullPair<F, S>> nonnullPairSecond(Matcher<? super S> secondMatcher) {
        return new NonnullPairSecondMatcher<>(secondMatcher);
    }

    public static final class NonnullPairBothMatcher<F, S> extends TypeSafeMatcher<NonnullPair<F, S>> {
        private final Matcher<? super F> firstMatcher;
        private final Matcher<? super S> secondMatcher;

        public NonnullPairBothMatcher(
                Matcher<? super F> firstMatcher,
                Matcher<? super S> secondMatcher
        ) {
            this.firstMatcher = firstMatcher;
            this.secondMatcher = secondMatcher;
        }

        @Override
        protected boolean matchesSafely(NonnullPair<F, S> item) {
            return firstMatcher.matches(item.first) && secondMatcher.matches(item.second);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("NonnullPair.first matches ")
                    .appendDescriptionOf(firstMatcher)
                    .appendText(" and NonnullPair.second matches ")
                    .appendDescriptionOf(secondMatcher);
        }
    }

    public static final class NonnullPairFirstMatcher<F, S> extends TypeSafeMatcher<NonnullPair<F, S>> {
        private final Matcher<? super F> firstMatcher;

        public NonnullPairFirstMatcher(Matcher<? super F> firstMatcher) {
            this.firstMatcher = firstMatcher;
        }

        @Override
        protected boolean matchesSafely(NonnullPair<F, S> item) {
            return firstMatcher.matches(item.first);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("NonnullPair.first matches ")
                    .appendDescriptionOf(firstMatcher);
        }
    }

    public static final class NonnullPairSecondMatcher<F, S> extends TypeSafeMatcher<NonnullPair<F, S>> {
        private final Matcher<? super S> secondMatcher;

        public NonnullPairSecondMatcher(Matcher<? super S> secondMatcher) {
            this.secondMatcher = secondMatcher;
        }

        @Override
        protected boolean matchesSafely(NonnullPair<F, S> item) {
            return secondMatcher.matches(item.second);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("NonnullPair.second matches ")
                    .appendDescriptionOf(secondMatcher);
        }
    }
}
