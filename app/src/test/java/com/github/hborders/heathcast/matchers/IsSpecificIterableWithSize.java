package com.github.hborders.heathcast.matchers;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Iterator;

import static org.hamcrest.core.IsEqual.equalTo;

public class IsSpecificIterableWithSize<I extends Iterable<? extends E>, E> extends FeatureMatcher<I, Integer> {

    public IsSpecificIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "an iterable with size", "iterable size");
    }


    @Override
    protected Integer featureValueOf(I actual) {
        int size = 0;
        for (Iterator<? extends E> iterator = actual.iterator(); iterator.hasNext(); iterator.next()) {
            size++;
        }
        return size;
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields an item count that satisfies the specified
     * matcher.
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), iterableWithSize(equalTo(2)))</pre>
     *
     * @param sizeMatcher
     *     a matcher for the number of items that should be yielded by an examined {@link Iterable}
     */
    public static <I extends Iterable<? extends E>, E> Matcher<I> specificIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsSpecificIterableWithSize<I, E>(sizeMatcher);
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields an item count that is equal to the specified
     * <code>size</code> argument.
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), iterableWithSize(2))</pre>
     *
     * @param size
     *     the number of items that should be yielded by an examined {@link Iterable}
     */
    public static <I extends Iterable<? extends E>, E> Matcher<I> specificIterableWithSize(int size) {
        return specificIterableWithSize(equalTo(size));
    }
}
