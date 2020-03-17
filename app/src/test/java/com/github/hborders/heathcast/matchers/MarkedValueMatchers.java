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
    private MarkedValueMatchers() {
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(V value) {
        return markedValue(
                emptySet(),
                value
        );
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(Matcher<V> valueMatcher) {
        return markedValue(
                equalTo(emptySet()),
                valueMatcher
        );
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(
            M marker,
            V value
    ) {
        return markedValue(
                Collections.singleton(marker),
                value
        );
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(
            M marker,
            Matcher<V> valueMatcher) {
        return markedValue(
                equalTo(Collections.singleton(marker)),
                valueMatcher
        );
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(
            Set<M> markers,
            V value
    ) {
        return markedValue(
                equalTo(markers),
                equalTo(value)
        );
    }

    public static <M, V> Matcher<MarkedValue<M, V>> markedValue(
            Matcher<Set<M>> markerMatcher,
            Matcher<V> valueMatcher
    ) {
        return new MarkedValueMatcher<>(
                markerMatcher,
                valueMatcher
        );
    }

    public static final class MarkedValueMatcher<M, V> extends TypeSafeMatcher<MarkedValue<M, V>> {
        private final Matcher<Set<M>> markerMatcher;
        private final Matcher<V> valueMatcher;

        public MarkedValueMatcher(
                Matcher<Set<M>> markerMatcher,
                Matcher<V> valueMatcher
        ) {
            this.markerMatcher = markerMatcher;
            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(MarkedValue<M, V> item) {
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
}
