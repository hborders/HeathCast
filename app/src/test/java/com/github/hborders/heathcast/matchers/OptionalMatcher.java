package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsNull;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public final class OptionalMatcher<ValueType> extends TypeSafeMatcher<
        Optional<ValueType>
        > {
    public static <ValueType> Matcher<
            Optional<ValueType>
            > optionalIsNotPresent() {
        return optionalValue(new IsNull<>());
    }

    public static <ValueType> Matcher<
            Optional<ValueType>
            > optionalIsPresent() {
        return optionalValue(not(new IsNull<>()));
    }

    public static <ValueType> Matcher<
            Optional<ValueType>
            > optionalValue(ValueType value) {
        return optionalValue(equalTo(value));
    }

    public static <ValueType> Matcher<
            Optional<ValueType>
            > optionalValue(Matcher<ValueType> valueMatcher) {
        return new OptionalMatcher<ValueType>(valueMatcher);
    }

    private final Matcher<ValueType> valueMatcher;

    public OptionalMatcher(Matcher<ValueType> valueMatcher) {
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(Optional<ValueType> item) {
        return valueMatcher.matches(item.orElse(null));
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("Optional value matches ")
                .appendDescriptionOf(valueMatcher);
    }
}
