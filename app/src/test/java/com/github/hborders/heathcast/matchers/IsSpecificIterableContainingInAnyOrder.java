package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;

public class IsSpecificIterableContainingInAnyOrder<I extends Iterable<? extends E>, E> extends TypeSafeDiagnosingMatcher<I> {
    private final Collection<Matcher<? super E>> matchers;

    public IsSpecificIterableContainingInAnyOrder(Collection<Matcher<? super E>> matchers) {
        this.matchers = matchers;
    }

    @Override
    protected boolean matchesSafely(I items, Description mismatchDescription) {
        final Matching<E> matching = new Matching<>(matchers, mismatchDescription);
        for (E item : items) {
            if (! matching.matches(item)) {
                return false;
            }
        }

        return matching.isFinished(items);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("iterable with items ")
                .appendList("[", ", ", "]", matchers)
                .appendText(" in any order");
    }

    private static final class Matching<S> {
        private final Collection<Matcher<? super S>> matchers;
        private final Description mismatchDescription;

        public Matching(Collection<Matcher<? super S>> matchers, Description mismatchDescription) {
            this.matchers = new ArrayList<>(matchers);
            this.mismatchDescription = mismatchDescription;
        }

        public boolean matches(S item) {
            if (matchers.isEmpty()) {
                mismatchDescription.appendText("no match for: ").appendValue(item);
                return false;
            }
            return isMatched(item);
        }

        public boolean isFinished(Iterable<? extends S> items) {
            if (matchers.isEmpty()) {
                return true;
            }
            mismatchDescription
                    .appendText("no item matches: ").appendList("", ", ", "", matchers)
                    .appendText(" in ").appendValueList("[", ", ", "]", items);
            return false;
        }

        private boolean isMatched(S item) {
            for (Matcher<? super S>  matcher : matchers) {
                if (matcher.matches(item)) {
                    matchers.remove(matcher);
                    return true;
                }
            }
            mismatchDescription.appendText("not matched: ").appendValue(item);
            return false;
        }
    }

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
     * @param itemMatchers
     *     a list of matchers, each of which must be satisfied by an item provided by an examined {@link Iterable}
     */
    @SafeVarargs
    public static <I extends Iterable<? extends E>, E> Matcher<I> specificallyContainsInAnyOrder(Matcher<? super E>... itemMatchers) {
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
     * @param items
     *     the items that must equal the items provided by an examined {@link Iterable} in any order
     */
    @SafeVarargs
    public static <I extends Iterable<? extends E>, E> Matcher<I> specificallyContainsInAnyOrder(E... items) {
        final List<Matcher<? super E>> matchers = new ArrayList<>();
        for (E item : items) {
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
     * @param itemMatchers
     *     a list of matchers, each of which must be satisfied by an item provided by an examined {@link Iterable}
     */
    public static <I extends Iterable<? extends E>, E> Matcher<I> specificallyContainsInAnyOrder(Collection<Matcher<? super E>> itemMatchers) {
        return new IsSpecificIterableContainingInAnyOrder<>(itemMatchers);
    }
}
