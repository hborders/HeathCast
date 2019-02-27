package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collection;
import java.util.HashSet;

public final class CollectionMatchers {
    public static <T> Matcher<? extends Collection<T>> collectionHasDistinctElements() {
        return new DistinctElementsMatcher<>();
    }

    public static final class DistinctElementsMatcher<T> extends TypeSafeMatcher<Collection<T>> {
        @Override
        protected boolean matchesSafely(Collection<T> item) {
            final HashSet<T> distinctElements = new HashSet<>(item);
            return distinctElements.size() == item.size();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has distinct elements");
        }
    }
}
