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

//    This should be enough, but there's a bug in Android Studio or JDK 8 that reports
//    compiler errors when using varargs like this.
//
//    @SafeVarargs
//    public static <ItemType> Matcher<
//            Iterable<? extends ItemType>
//            > containsInOrder(
//            Matcher<? super ItemType> itemMatcher1,
//            Matcher<? super ItemType>... otherItemMatchers
//    ) {
//        ArrayList<Matcher<? super ItemType>> itemMatchers = new ArrayList<>(otherItemMatchers.length + 1);
//        itemMatchers.add(itemMatcher1);
//        itemMatchers.addAll(Arrays.asList(otherItemMatchers));
//        return new IsIterableContainingInOrder<>(itemMatchers);
//    }

    public static <ItemType> Matcher<
            Iterable<? extends ItemType>
            > containsInOrder(
            Matcher<? super ItemType> itemMatcher1,
            Matcher<? super ItemType> itemMatcher2
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.asList(
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
                Arrays.asList(
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
                Arrays.asList(
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
                Arrays.asList(
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
                Arrays.asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5,
                        itemMatcher6
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
            Matcher<? super ItemType> itemMatcher6,
            Matcher<? super ItemType> itemMatcher7
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5,
                        itemMatcher6,
                        itemMatcher7
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
            Matcher<? super ItemType> itemMatcher6,
            Matcher<? super ItemType> itemMatcher7,
            Matcher<? super ItemType> itemMatcher8
    ) {
        return new IsIterableContainingInOrder<>(
                Arrays.asList(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4,
                        itemMatcher5,
                        itemMatcher6,
                        itemMatcher7,
                        itemMatcher8
                )
        );
    }

    private IsIterableContainingInOrderUtil() {
    }
}
