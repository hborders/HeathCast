package com.github.hborders.heathcast.matchers;

import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public final class SpecificIterableMatchers {
    private SpecificIterableMatchers() {
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> containsInAnyOrder(Matcher<? super E>... elementMatchers) {
        return IsSpecificIterableContainingInAnyOrder.specificallyContainsInAnyOrder(elementMatchers);
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> containsInAnyOrder(E... elements) {
        return IsSpecificIterableContainingInAnyOrder.specificallyContainsInAnyOrder(elements);
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> containsInAnyOrder(Collection<Matcher<? super E>> elementMatchers) {
        return IsSpecificIterableContainingInAnyOrder.specificallyContainsInAnyOrder(elementMatchers);
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> containsInRelativeOrder(E... elements) {
        return IsSpecificIterableContainingInRelativeOrder.specificallyContainsInRelativeOrder(elements);
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> containsInRelativeOrder(Matcher<? super E>... elementMatchers) {
        return IsSpecificIterableContainingInRelativeOrder.specificallyContainsInRelativeOrder(elementMatchers);
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> containsInRelativeOrder(List<Matcher<? super E>> elementMatchers) {
        return IsSpecificIterableContainingInRelativeOrder.specificallyContainsInRelativeOrder(elementMatchers);
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> iterableWithSize(int size) {
        return iterableWithSize(equalTo(size));
    }

    public static <I extends Iterable<? extends E>, E> Matcher<I> iterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return IsSpecificIterableWithSize.specificIterableWithSize(sizeMatcher);
    }
}
