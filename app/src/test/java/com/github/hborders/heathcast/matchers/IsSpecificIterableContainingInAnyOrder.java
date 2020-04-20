package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;

public class IsSpecificIterableContainingInAnyOrder<
        IterableType extends Iterable<? extends ItemType>,
        ItemType
        > extends TypeSafeDiagnosingMatcher<IterableType> {
    /**
     * <p>
     * Creates an order agnostic matcher for {@link Iterable}s that matches when a single pass over
     * the examined {@link Iterable} yields a series of items, each satisfying one matcher anywhere
     * in the specified matchers.  For a positive match, the examined iterable must be of the same
     * length as the number of specified matchers.
     * </p>
     * <p>
     * N.B. each of the specified matchers will only be used once during a given examination, so be
     * careful when specifying matchers that may be satisfied by more than one entry in an examined
     * iterable.
     * </p>
     * <p>
     * For example:
     * </p>
     * <pre>assertThat(Arrays.asList("foo", "bar"), containsInAnyOrder(equalTo("bar"), equalTo("foo")))</pre>
     *
     * @param itemMatchers a list of matchers, each of which must be satisfied by an item provided by an examined {@link Iterable}
     */
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInAnyOrder(
                    Matcher<? super ItemType>... itemMatchers
    ) {
        return specificallyContainsInAnyOrder(Arrays.asList(itemMatchers));
    }

    /**
     * <p>
     * Creates an order agnostic matcher for {@link Iterable}s that matches when a single pass over
     * the examined {@link Iterable} yields a series of items, each logically equal to one item
     * anywhere in the specified items. For a positive match, the examined iterable
     * must be of the same length as the number of specified items.
     * </p>
     * <p>
     * N.B. each of the specified items will only be used once during a given examination, so be
     * careful when specifying items that may be equal to more than one entry in an examined
     * iterable.
     * </p>
     * <p>
     * For example:
     * </p>
     * <pre>assertThat(Arrays.asList("foo", "bar"), containsInAnyOrder("bar", "foo"))</pre>
     *
     * @param items the items that must equal the items provided by an examined {@link Iterable} in any order
     */
    @SafeVarargs
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInAnyOrder(ItemType... items) {
        final List<
                Matcher<? super ItemType>
                > matchers = new ArrayList<>();
        for (ItemType item : items) {
            matchers.add(equalTo(item));
        }

        return new IsSpecificIterableContainingInAnyOrder<>(matchers);
    }

    /**
     * <p>
     * Creates an order agnostic matcher for {@link Iterable}s that matches when a single pass over
     * the examined {@link Iterable} yields a series of items, each satisfying one matcher anywhere
     * in the specified collection of matchers.  For a positive match, the examined iterable
     * must be of the same length as the specified collection of matchers.
     * </p>
     * <p>
     * N.B. each matcher in the specified collection will only be used once during a given
     * examination, so be careful when specifying matchers that may be satisfied by more than
     * one entry in an examined iterable.
     * </p>
     * <p>For example:</p>
     * <pre>assertThat(Arrays.asList("foo", "bar"), containsInAnyOrder(Arrays.asList(equalTo("bar"), equalTo("foo"))))</pre>
     *
     * @param itemMatchers a list of matchers, each of which must be satisfied by an item provided by an examined {@link Iterable}
     */
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyContainsInAnyOrder(
                    Collection<
                            Matcher<? super ItemType>
                            > itemMatchers
    ) {
        return new IsSpecificIterableContainingInAnyOrder<>(itemMatchers);
    }

    private static final class Matching<ItemType> {
        private final Collection<
                Matcher<? super ItemType>
                > matchers;
        private final Description mismatchDescription;

        public Matching(
                Collection<
                        Matcher<? super ItemType>
                        > matchers,
                Description mismatchDescription
        ) {
            this.matchers = new ArrayList<>(matchers);
            this.mismatchDescription = mismatchDescription;
        }

        public boolean matches(ItemType item) {
            if (matchers.isEmpty()) {
                mismatchDescription
                        .appendText("no match for: ")
                        .appendValue(item);
                return false;
            }
            return isMatched(item);
        }

        public boolean isFinished(Iterable<? extends ItemType> items) {
            if (matchers.isEmpty()) {
                return true;
            }
            mismatchDescription
                    .appendText("no item matches: ")
                    .appendList(
                            "",
                            ", ",
                            "",
                            matchers
                    )
                    .appendText(" in ")
                    .appendValueList(
                            "[",
                            ", ",
                            "]",
                            items
                    );
            return false;
        }

        private boolean isMatched(ItemType item) {
            for (Matcher<? super ItemType> matcher : matchers) {
                if (matcher.matches(item)) {
                    matchers.remove(matcher);
                    return true;
                }
            }
            mismatchDescription
                    .appendText("not matched: ")
                    .appendValue(item);
            return false;
        }
    }

    private final Collection<
            Matcher<? super ItemType>
            > matchers;

    public IsSpecificIterableContainingInAnyOrder(
            Collection<
                    Matcher<? super ItemType>
                    > matchers
    ) {
        this.matchers = matchers;
    }

    @Override
    protected boolean matchesSafely(
            IterableType items,
            Description mismatchDescription
    ) {
        final Matching<ItemType> matching = new Matching<>(
                matchers,
                mismatchDescription
        );
        for (ItemType item : items) {
            if (!matching.matches(item)) {
                return false;
            }
        }

        return matching.isFinished(items);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("iterable with items ")
                .appendList(
                        "[",
                        ", ",
                        "]",
                        matchers
                )
                .appendText(" in any order");
    }
}
