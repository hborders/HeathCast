package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.core.Tuple;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.is;

public final class TupleMatchers {
    public static <F, S> Matcher<Tuple<F, S>> tupleFirst(F first) {
        return tupleFirst(is(first));
    }

    public static <F, S> Matcher<Tuple<F, S>> tupleSecond(S second) {
        return tupleSecond(is(second));
    }

    public static <F, S> Matcher<Tuple<F, S>> tuple(
            Matcher<? super F> firstMatcher,
            Matcher<? super S> secondMatcher
    ) {
        return new TupleBothMatcher<>(
                firstMatcher,
                secondMatcher
        );
    }

    public static <F, S> Matcher<Tuple<F, S>> tupleFirst(Matcher<? super F> firstMatcher) {
        return new TupleFirstMatcher<>(firstMatcher);
    }

    public static <F, S> Matcher<Tuple<F, S>> tupleSecond(Matcher<? super S> secondMatcher) {
        return new TupleSecondMatcher<>(secondMatcher);
    }

    public static final class TupleBothMatcher<F, S> extends TypeSafeMatcher<Tuple<F, S>> {
        private final Matcher<? super F> firstMatcher;
        private final Matcher<? super S> secondMatcher;

        public TupleBothMatcher(
                Matcher<? super F> firstMatcher,
                Matcher<? super S> secondMatcher
        ) {
            this.firstMatcher = firstMatcher;
            this.secondMatcher = secondMatcher;
        }

        @Override
        protected boolean matchesSafely(Tuple<F, S> item) {
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

    public static final class TupleFirstMatcher<F, S> extends TypeSafeMatcher<Tuple<F, S>> {
        private final Matcher<? super F> firstMatcher;

        public TupleFirstMatcher(Matcher<? super F> firstMatcher) {
            this.firstMatcher = firstMatcher;
        }

        @Override
        protected boolean matchesSafely(Tuple<F, S> item) {
            return firstMatcher.matches(item.first);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("NonnullPair.first matches ")
                    .appendDescriptionOf(firstMatcher);
        }
    }

    public static final class TupleSecondMatcher<F, S> extends TypeSafeMatcher<Tuple<F, S>> {
        private final Matcher<? super S> secondMatcher;

        public TupleSecondMatcher(Matcher<? super S> secondMatcher) {
            this.secondMatcher = secondMatcher;
        }

        @Override
        protected boolean matchesSafely(Tuple<F, S> item) {
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
