package com.github.hborders.heathcast.matchers;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Iterator;

import static org.hamcrest.core.IsEqual.equalTo;

public class IsSpecificIterableWithSize<
        IterableType extends Iterable<? extends ItemType>,
        ItemType
        > extends FeatureMatcher<
        IterableType,
        Integer
        > {
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
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsSpecificIterableWithSize<IterableType, ItemType>(sizeMatcher);
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
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificIterableWithSize(int size) {
        return specificIterableWithSize(equalTo(size));
    }

    public IsSpecificIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "an iterable with size", "iterable size");
    }


    @Override
    protected Integer featureValueOf(IterableType actual) {
        int size = 0;
        for (Iterator<? extends ItemType> iterator = actual.iterator(); iterator.hasNext(); iterator.next()) {
            size++;
        }
        return size;
    }
}
