package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.internal.NullSafety;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.ArrayMatching.asEqualMatchers;

public class IsSpecificIterableContainingInOrder<
        IterableType extends Iterable<? extends ItemType>,
        ItemType
        > extends TypeSafeDiagnosingMatcher<IterableType> {
    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a series of items, each logically equal to the
     * corresponding item in the specified items.  For a positive match, the examined iterable
     * must be of the same length as the number of specified items.
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), contains("foo", "bar"))</pre>
     *
     * @param items
     *     the items that must equal the items provided by an examined {@link Iterable}
     */
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType> Matcher<IterableType> specificallyContainsInOrder(ItemType... items) {
        return specificallyContainsInOrder(asEqualMatchers(items));
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a single item that satisfies the specified matcher.
     * For a positive match, the examined iterable must only yield one item.
     * For example:
     * <pre>assertThat(Arrays.asList("foo"), contains(equalTo("foo")))</pre>
     *
     * @param itemMatcher
     *     the matcher that must be satisfied by the single item provided by an
     *     examined {@link Iterable}
     */
    @SuppressWarnings("unchecked")
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContains2(final Matcher<ItemType> itemMatcher) {
        return specificallyContainsInOrder(new ArrayList<>(singletonList(itemMatcher)));
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a series of items, each satisfying the corresponding
     * matcher in the specified matchers.  For a positive match, the examined iterable
     * must be of the same length as the number of specified matchers.
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), contains(equalTo("foo"), equalTo("bar")))</pre>
     *
     * @param itemMatchers
     *     the matchers that must be satisfied by the items provided by an examined {@link Iterable}
     */
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInOrder(Matcher<? super ItemType>... itemMatchers) {
        // required for JDK 1.6
        //noinspection RedundantTypeArguments
        final List<Matcher<? super ItemType>> nullSafeWithExplicitTypeMatchers = NullSafety.<ItemType>nullSafe(itemMatchers);
        return specificallyContainsInOrder(nullSafeWithExplicitTypeMatchers);
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a series of items, each satisfying the corresponding
     * matcher in the specified list of matchers.  For a positive match, the examined iterable
     * must be of the same length as the specified list of matchers.
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), contains(Arrays.asList(equalTo("foo"), equalTo("bar"))))</pre>
     *
     * @param itemMatchers
     *     a list of matchers, each of which must be satisfied by the corresponding item provided by
     *     an examined {@link Iterable}
     */
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInOrder(List<Matcher<? super ItemType>> itemMatchers) {
        return new IsSpecificIterableContainingInOrder<>(itemMatchers);
    }

    private final static class MatchSeries<ItemType> {
        private final List<Matcher<? super ItemType>> matchers;
        private final Description mismatchDescription;
        private int nextMatchIx = 0;

        public MatchSeries(
                List<Matcher<? super ItemType>> matchers,
                Description mismatchDescription
        ) {
            if (matchers.isEmpty()) {
                throw new IllegalArgumentException("Should specify at least one expected element");
            }
            this.matchers = matchers;
            this.mismatchDescription = mismatchDescription;
        }

        public boolean matches(ItemType item) {
            if (matchers.size() <= nextMatchIx) {
                mismatchDescription
                        .appendText("not matched: ")
                        .appendValue(item);
                return false;
            }

            return isMatched(item);
        }

        public boolean isFinished() {
            if (nextMatchIx < matchers.size()) {
                mismatchDescription
                        .appendText("no item was ")
                        .appendDescriptionOf(matchers.get(nextMatchIx));
                return false;
            }
            return true;
        }

        private boolean isMatched(ItemType item) {
            final Matcher<? super ItemType> matcher = matchers.get(nextMatchIx);
            if (!matcher.matches(item)) {
                describeMismatch(
                        matcher,
                        item
                );
                return false;
            }
            nextMatchIx++;
            return true;
        }

        private void describeMismatch(Matcher<? super ItemType> matcher, ItemType item) {
            mismatchDescription.appendText("item " + nextMatchIx + ": ");
            matcher.describeMismatch(
                    item,
                    mismatchDescription
            );
        }
    }

    private final List<Matcher<? super ItemType>> matchers;

    public IsSpecificIterableContainingInOrder(List<Matcher<? super ItemType>> matchers) {
        this.matchers = matchers;
    }

    @Override
    protected boolean matchesSafely(
            IterableType iterable,
            Description mismatchDescription
    ) {
        final MatchSeries<ItemType> matchSeries = new MatchSeries<>(
                matchers,
                mismatchDescription
        );
        for (ItemType item : iterable) {
            if (!matchSeries.matches(item)) {
                return false;
            }
        }

        return matchSeries.isFinished();
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("iterable containing ")
                .appendList(
                        "[",
                        ", ",
                        "]",
                        matchers
                );
    }
}
