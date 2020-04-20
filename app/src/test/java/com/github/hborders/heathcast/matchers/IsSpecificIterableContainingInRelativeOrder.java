package com.github.hborders.heathcast.matchers;

import androidx.annotation.Nullable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;

public class IsSpecificIterableContainingInRelativeOrder<
        IterableType extends Iterable<? extends ItemType>,
        ItemType
        > extends TypeSafeDiagnosingMatcher<IterableType> {
    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a series of items, that contains items logically equal to the
     * corresponding item in the specified items, in the same relative order
     * For example:
     * <pre>assertThat(Arrays.asList("a", "b", "c", "d", "e"), containsInRelativeOrder("b", "d"))</pre>
     *
     * @param items the items that must be contained within items provided by an examined {@link Iterable} in the same relative order
     */
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInRelativeOrder(ItemType... items) {
        List<
                Matcher<? super ItemType>
                > matchers = new ArrayList<>();
        for (ItemType item : items) {
            matchers.add(equalTo(item));
        }

        return specificallyContainsInRelativeOrder(matchers);
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a series of items, that each satisfying the corresponding
     * matcher in the specified matchers, in the same relative order.
     * For example:
     * <pre>assertThat(Arrays.asList("a", "b", "c", "d", "e"), containsInRelativeOrder(equalTo("b"), equalTo("d")))</pre>
     *
     * @param itemMatchers the matchers that must be satisfied by the items provided by an examined {@link Iterable} in the same relative order
     */
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInRelativeOrder(
                    Matcher<? super ItemType>... itemMatchers
    ) {
        return specificallyContainsInRelativeOrder(asList(itemMatchers));
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when a single pass over the
     * examined {@link Iterable} yields a series of items, that contains items satisfying the corresponding
     * matcher in the specified list of matchers, in the same relative order.
     * For example:
     * <pre>assertThat(Arrays.asList("a", "b", "c", "d", "e"), contains(Arrays.asList(equalTo("b"), equalTo("d"))))</pre>
     *
     * @param itemMatchers a list of matchers, each of which must be satisfied by the items provided by
     *                     an examined {@link Iterable} in the same relative order
     */
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInRelativeOrder(
                    List<
                            Matcher<? super ItemType>
                            > itemMatchers
    ) {
        return new IsSpecificIterableContainingInRelativeOrder<>(itemMatchers);
    }

    private static final class MatchSeriesInRelativeOrder<ItemType> {
        public final List<
                Matcher<? super ItemType>
                > matchers;
        private final Description mismatchDescription;
        private int nextMatchIx = 0;
        @Nullable
        private ItemType lastMatchedItem = null;

        public MatchSeriesInRelativeOrder(
                List<
                        Matcher<? super ItemType>
                        > matchers,
                Description mismatchDescription
        ) {
            if (matchers.isEmpty()) {
                throw new IllegalArgumentException("Should specify at least one expected element");
            }
            this.matchers = matchers;
            this.mismatchDescription = mismatchDescription;
        }

        public void processItems(Iterable<? extends ItemType> iterable) {
            for (ItemType item : iterable) {
                if (nextMatchIx < matchers.size()) {
                    Matcher<? super ItemType> matcher = matchers.get(nextMatchIx);
                    if (matcher.matches(item)) {
                        lastMatchedItem = item;
                        nextMatchIx++;
                    }
                }
            }
        }

        public boolean isFinished() {
            if (nextMatchIx < matchers.size()) {
                mismatchDescription
                        .appendDescriptionOf(matchers.get(nextMatchIx))
                        .appendText(" was not found");
                if (lastMatchedItem != null) {
                    mismatchDescription
                            .appendText(" after ")
                            .appendValue(lastMatchedItem);
                }
                return false;
            }
            return true;
        }
    }

    private final List<
            Matcher<? super ItemType>
            > matchers;

    public IsSpecificIterableContainingInRelativeOrder(
            List<
                    Matcher<? super ItemType>
                    > matchers
    ) {
        this.matchers = matchers;
    }

    @Override
    protected boolean matchesSafely(
            IterableType iterable,
            Description mismatchDescription
    ) {
        MatchSeriesInRelativeOrder<ItemType> matchSeriesInRelativeOrder =
                new MatchSeriesInRelativeOrder<>(
                        matchers,
                        mismatchDescription
                );
        matchSeriesInRelativeOrder.processItems(iterable);
        return matchSeriesInRelativeOrder.isFinished();
    }

    public void describeTo(Description description) {
        description
                .appendText("iterable containing ")
                .appendList(
                        "[",
                        ", ",
                        "]",
                        matchers
                )
                .appendText(" in relative order");
    }
}
