package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsNull;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public final class OptionalMatcher<T> extends TypeSafeMatcher<Optional<T>> {
    public static <T> Matcher<Optional<T>> optionalIsNotPresent() {
        return optionalValue(new IsNull<>());
    }

    public static <T> Matcher<Optional<T>> optionalIsPresent() {
        return optionalValue(not(new IsNull<>()));
    }

    public static <T> Matcher<Optional<T>> optionalValue(T value) {
        return optionalValue(equalTo(value));
    }

    public static <T> Matcher<Optional<T>> optionalValue(Matcher<T> valueMatcher) {
        return new OptionalMatcher<T>(valueMatcher);
    }

    private final Matcher<T> valueMatcher;

    public OptionalMatcher(Matcher<T> valueMatcher) {
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(Optional<T> item) {
        return valueMatcher.matches(item.orElse(null));
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("Optional value matches ")
                .appendDescriptionOf(valueMatcher);
    }
}
