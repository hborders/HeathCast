package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

public final class OptionalMatcher<T> extends TypeSafeMatcher<Optional<T>> {

    public static <T> OptionalMatcher<T> optional(Matcher<T> valueMatcher) {
        return new OptionalMatcher<>(valueMatcher);
    }

    private final Matcher<T> valueMatcher;

    public OptionalMatcher(Matcher<T> valueMatcher) {
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(Optional<T> item) {
        return item.map(valueMatcher::matches).orElse(false);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("Optional value matches ")
                .appendDescriptionOf(valueMatcher);
    }
}
