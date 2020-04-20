package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.core.Tuple;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class TupleMatchers {
    public static final class TupleBothMatcher<
            FirstType,
            SecondType
            > extends TypeSafeMatcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > {
        private final Matcher<? super FirstType> firstMatcher;
        private final Matcher<? super SecondType> secondMatcher;

        public TupleBothMatcher(
                Matcher<? super FirstType> firstMatcher,
                Matcher<? super SecondType> secondMatcher
        ) {
            this.firstMatcher = firstMatcher;
            this.secondMatcher = secondMatcher;
        }

        @Override
        protected boolean matchesSafely(
                Tuple<
                        FirstType,
                        SecondType
                        > item
        ) {
            return firstMatcher.matches(item.first) &&
                    secondMatcher.matches(item.second);
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

    public static final class TupleFirstMatcher<
            FirstType,
            SecondType
            > extends TypeSafeMatcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > {
        private final Matcher<? super FirstType> firstMatcher;

        public TupleFirstMatcher(Matcher<? super FirstType> firstMatcher) {
            this.firstMatcher = firstMatcher;
        }

        @Override
        protected boolean matchesSafely(
                Tuple<
                        FirstType,
                        SecondType
                        > item
        ) {
            return firstMatcher.matches(item.first);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("NonnullPair.first matches ")
                    .appendDescriptionOf(firstMatcher);
        }
    }

    public static final class TupleSecondMatcher<
            FirstType,
            SecondType
            > extends TypeSafeMatcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > {
        private final Matcher<? super SecondType> secondMatcher;

        public TupleSecondMatcher(Matcher<? super SecondType> secondMatcher) {
            this.secondMatcher = secondMatcher;
        }

        @Override
        protected boolean matchesSafely(Tuple<FirstType, SecondType> item) {
            return secondMatcher.matches(item.second);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("NonnullPair.second matches ")
                    .appendDescriptionOf(secondMatcher);
        }
    }

    public static <
            FirstType,
            SecondType
            > Matcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > tupleFirst(FirstType first) {
        return tupleFirst(equalTo(first));
    }

    public static <
            FirstType,
            SecondType
            > Matcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > tupleSecond(SecondType second) {
        return tupleSecond(equalTo(second));
    }

    public static <
            FirstType,
            SecondType
            > Matcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > tuple(
            Matcher<? super FirstType> firstMatcher,
            Matcher<? super SecondType> secondMatcher
    ) {
        return new TupleBothMatcher<>(
                firstMatcher,
                secondMatcher
        );
    }

    public static <
            FirstType,
            SecondType
            > Matcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > tupleFirst(Matcher<? super FirstType> firstMatcher) {
        return new TupleFirstMatcher<>(firstMatcher);
    }

    public static <
            FirstType,
            SecondType
            > Matcher<
            Tuple<
                    FirstType,
                    SecondType
                    >
            > tupleSecond(Matcher<? super SecondType> secondMatcher) {
        return new TupleSecondMatcher<>(secondMatcher);
    }
}
