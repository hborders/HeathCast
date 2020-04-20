package com.github.hborders.heathcast.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;

public final class IsIterableContainingInOrderUtil {
    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsNothing() {
        return equalTo(Collections.emptyList());
    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(Matcher<? super ItemType> itemMatcher1) {
        return new IsIterableContainingInOrder<>(Collections.singletonList(itemMatcher1));
    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(
            Matcher<? super ItemType> itemMatcher1,
            Matcher<? super ItemType> itemMatcher2
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super ItemType>>asList(
                        itemMatcher1,
                        itemMatcher2
                )
        );
    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(
            Matcher<? super ItemType> itemMatcher1,
            Matcher<? super ItemType> itemMatcher2,
            Matcher<? super ItemType> itemMatcher3
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super ItemType>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3
                )
        );
    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(
            Matcher<? super ItemType> itemMatcher1,
            Matcher<? super ItemType> itemMatcher2,
            Matcher<? super ItemType> itemMatcher3,
            Matcher<? super ItemType> itemMatcher4
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super ItemType>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4
                )
        );
    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(
            Matcher<? super ItemType> itemMatcher1,
            Matcher<? super ItemType> itemMatcher2,
            Matcher<? super ItemType> itemMatcher3,
            Matcher<? super ItemType> itemMatcher4,
            Matcher<? super ItemType> itemMatcher5
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super ItemType>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5
                )
        );
    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(
            Matcher<? super ItemType> itemMatcher1,
            Matcher<? super ItemType> itemMatcher2,
            Matcher<? super ItemType> itemMatcher3,
            Matcher<? super ItemType> itemMatcher4,
            Matcher<? super ItemType> itemMatcher5,
            Matcher<? super ItemType> itemMatcher6
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super ItemType>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5,
                        itemMatcher6
                )
        );
    }

    private IsIterableContainingInOrderUtil() {
    }
}
