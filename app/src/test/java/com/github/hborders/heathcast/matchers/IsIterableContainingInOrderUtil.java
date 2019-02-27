package com.github.hborders.heathcast.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

public final class IsIterableContainingInOrderUtil {
    private IsIterableContainingInOrderUtil() {
    }

    public static <E> Matcher<Iterable<? extends E>> containsNothing() {
        return is(Collections.emptyList());
    }

    public static <E> Matcher<Iterable<? extends E>> containsInOrder(Matcher<? super E> itemMatcher1) {
        return new IsIterableContainingInOrder<>(Collections.singletonList(itemMatcher1));
    }

    public static <E> Matcher<Iterable<? extends E>> containsInOrder(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super E>>asList(
                        itemMatcher1,
                        itemMatcher2
                )
        );
    }

    public static <E> Matcher<Iterable<? extends E>> containsInOrder(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2,
            Matcher<? super E> itemMatcher3
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super E>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3
                )
        );
    }

    public static <E> Matcher<Iterable<? extends E>> containsInOrder(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2,
            Matcher<? super E> itemMatcher3,
            Matcher<? super E> itemMatcher4
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super E>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4
                )
        );
    }

    public static <E> Matcher<Iterable<? extends E>> containsInOrder(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2,
            Matcher<? super E> itemMatcher3,
            Matcher<? super E> itemMatcher4,
            Matcher<? super E> itemMatcher5
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super E>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5
                )
        );
    }

    public static <E> Matcher<Iterable<? extends E>> containsInOrder(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2,
            Matcher<? super E> itemMatcher3,
            Matcher<? super E> itemMatcher4,
            Matcher<? super E> itemMatcher5,
            Matcher<? super E> itemMatcher6
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.<Matcher<? super E>>asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5,
                        itemMatcher6
                )
        );
    }
}
