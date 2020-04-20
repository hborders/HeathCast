package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsEmptySpecificIterable<
        IterableType extends Iterable<? extends ItemType>,
        ItemType
        > extends TypeSafeMatcher<IterableType> {
    /**
     * Creates a matcher for {@link java.lang.Iterable}s matching examined collections whose <code>isEmpty</code>
     * method returns <code>true</code>.
     * For example:
     * <pre>assertThat(new ArrayList&lt;String&gt;(), is(empty()))</pre>
     *
     */
    public static <
            IterableType extends Iterable<? extends ItemType>,
            ItemType
            > Matcher<IterableType> specificallyEmpty() {
        return new IsEmptySpecificIterable<>();
    }

    @Override
    public boolean matchesSafely(IterableType item) {
        return !item.iterator().hasNext();
    }

    @Override
    public void describeMismatchSafely(
            IterableType item,
            Description mismatchDescription
    ) {
        mismatchDescription.appendValue(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an empty iterable");
    }
}
