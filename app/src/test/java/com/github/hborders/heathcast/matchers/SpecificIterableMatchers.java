package com.github.hborders.heathcast.matchers;

import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public final class SpecificIterableMatchers {
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> containsInAnyOrder(Matcher<? super ItemType>... elementMatchers) {
        return IsSpecificIterableContainingInAnyOrder.specificallyContainsInAnyOrder(elementMatchers);
    }

    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> containsInAnyOrder(ItemType... elements) {
        return IsSpecificIterableContainingInAnyOrder.specificallyContainsInAnyOrder(elements);
    }

    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> containsInAnyOrder(
            Collection<
                    Matcher<? super ItemType>
                    > elementMatchers
    ) {
        return IsSpecificIterableContainingInAnyOrder.specificallyContainsInAnyOrder(elementMatchers);
    }

    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> containsInRelativeOrder(ItemType... elements) {
        return IsSpecificIterableContainingInRelativeOrder.specificallyContainsInRelativeOrder(elements);
    }

    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> containsInRelativeOrder(Matcher<? super ItemType>... elementMatchers) {
        return IsSpecificIterableContainingInRelativeOrder.specificallyContainsInRelativeOrder(elementMatchers);
    }

    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> containsInRelativeOrder(
            List<
                    Matcher<? super ItemType>
                    > elementMatchers
    ) {
        return IsSpecificIterableContainingInRelativeOrder.specificallyContainsInRelativeOrder(elementMatchers);
    }

    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> iterableWithSize(int size) {
        return iterableWithSize(equalTo(size));
    }

    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> iterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return IsSpecificIterableWithSize.specificIterableWithSize(sizeMatcher);
    }

    private SpecificIterableMatchers() {
    }
}
