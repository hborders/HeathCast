package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.utils.ListUtil;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;

import java.util.Collections;

import static com.github.hborders.heathcast.utils.ListUtil.listOf;
import static org.hamcrest.CoreMatchers.is;

public final class IsIterableContainingInOrderUtil {
    private IsIterableContainingInOrderUtil() {
    }

    public static <E> Matcher<Iterable<? extends E>> containsNothing() {
        return is(Collections.emptyList());
    }

    public static <E> Matcher<Iterable<? extends E>> contains(Matcher<? super E> itemMatcher1) {
        return new IsIterableContainingInOrder<>(listOf(itemMatcher1));
    }

    public static <E> Matcher<Iterable<? extends E>> contains(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2
    ) {
        return new IsIterableContainingInOrder<>(
                ListUtil.<Matcher<? super E>>listOf(
                        itemMatcher1,
                        itemMatcher2
                )
        );
    }

    public static <E> Matcher<Iterable<? extends E>> contains(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2,
            Matcher<? super E> itemMatcher3
    ) {
        return new IsIterableContainingInOrder<>(
                ListUtil.<Matcher<? super E>>listOf(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3
                )
        );
    }

    public static <E> Matcher<Iterable<? extends E>> contains(
            Matcher<? super E> itemMatcher1,
            Matcher<? super E> itemMatcher2,
            Matcher<? super E> itemMatcher3,
            Matcher<? super E> itemMatcher4
    ) {
        return new IsIterableContainingInOrder<>(
                ListUtil.<Matcher<? super E>>listOf(
                        itemMatcher1,
                        itemMatcher2,
                        itemMatcher3,
                        itemMatcher4
                )
        );
    }
}
