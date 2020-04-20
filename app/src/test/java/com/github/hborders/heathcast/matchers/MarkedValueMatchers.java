package com.github.hborders.heathcast.matchers;

import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collections;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.equalTo;

public final class MarkedValueMatchers {
    public static final class MarkedValueMatcher<
            MarkerType,
            ValueType
            > extends TypeSafeMatcher<
            MarkedValue<
                    MarkerType,
                    ValueType
                    >
            > {
        private final Matcher<
                Set<MarkerType>
                > markerMatcher;
        private final Matcher<ValueType> valueMatcher;

        public MarkedValueMatcher(
                Matcher<
                        Set<MarkerType>
                        > markerMatcher,
                Matcher<ValueType> valueMatcher
        ) {
            this.markerMatcher = markerMatcher;
            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(
                MarkedValue<
                        MarkerType,
                        ValueType
                        > item
        ) {
            return markerMatcher.matches(item.markers) && valueMatcher.matches(item.value);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("MarkedValue matches ")
                    .appendDescriptionOf(markerMatcher)
                    .appendText(", ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(V value) {
        return markedValue(
                emptySet(),
                value
        );
    }

    public static <
            MarkerType,
            ValueType
            > Matcher<
            MarkedValue<
                    MarkerType,
                    ValueType
                    >
            > markedValue(Matcher<ValueType> valueMatcher) {
        return markedValue(
                equalTo(emptySet()),
                valueMatcher
        );
    }

    public static <
            MarkerType,
            ValueType
            > Matcher<
            MarkedValue<
                    MarkerType,
                    ValueType
                    >
            > markedValue(
            MarkerType marker,
            ValueType value
    ) {
        return markedValue(
                Collections.singleton(marker),
                value
        );
    }

    public static <
            MarkerType,
            ValueType
            > Matcher<
            MarkedValue<
                    MarkerType,
                    ValueType
                    >
            > markedValue(
            MarkerType marker,
            Matcher<ValueType> valueMatcher) {
        return markedValue(
                equalTo(Collections.singleton(marker)),
                valueMatcher
        );
    }

    public static <
            MarkerType,
            ValueType
            > Matcher<
            MarkedValue<
                    MarkerType,
                    ValueType
                    >
            > markedValue(
            Set<MarkerType> markers,
            ValueType value
    ) {
        return markedValue(
                equalTo(markers),
                equalTo(value)
        );
    }

    public static <
            MarkerType,
            ValueType
            > Matcher<
            MarkedValue<
                    MarkerType,
                    ValueType
                    >
            > markedValue(
            Matcher<Set<MarkerType>> markerMatcher,
            Matcher<ValueType> valueMatcher
    ) {
        return new MarkedValueMatcher<>(
                markerMatcher,
                valueMatcher
        );
    }

    private MarkedValueMatchers() {
    }
}
