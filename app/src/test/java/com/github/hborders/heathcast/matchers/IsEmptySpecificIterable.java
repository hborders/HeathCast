package com.github.hborders.heathcast.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsEmptySpecificIterable<I extends Iterable<? extends E>, E> extends TypeSafeMatcher<I> {

    @Override
    public boolean matchesSafely(I item) {
        return !item.iterator().hasNext();
    }

    @Override
    public void describeMismatchSafely(I item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an empty iterable");
    }

    /**
     * Creates a matcher for {@link java.lang.Iterable}s matching examined collections whose <code>isEmpty</code>
     * method returns <code>true</code>.
     * For example:
     * <pre>assertThat(new ArrayList&lt;String&gt;(), is(empty()))</pre>
     *
     */
    public static <I extends Iterable<? extends E>, E> Matcher<I> specificallyEmpty() {
        return new IsEmptySpecificIterable<>();
    }
}
