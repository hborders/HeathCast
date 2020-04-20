package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collection;
import java.util.HashSet;

public final class CollectionMatchers {
    public static final class DistinctElementsMatcher<ItemType> extends TypeSafeMatcher<Collection<ItemType>> {
        @Override
        protected boolean matchesSafely(Collection<ItemType> item) {
            final HashSet<ItemType> distinctElements = new HashSet<>(item);
            return distinctElements.size() == item.size();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has distinct elements");
        }
    }

    public static <ItemType> Matcher<? extends Collection<ItemType>> collectionHasDistinctElements() {
        return new DistinctElementsMatcher<>();
    }
}
