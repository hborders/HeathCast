package com.github.hborders.heathcast.reactivex;

import org.hamcrest.Matcher;

import io.reactivex.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.contains;
import static org.hamcrest.MatcherAssert.assertThat;

public final class MatcherTestObserver<T> extends TestObserver<T> {
    public final void assertValueThat(Matcher<? super T> valueMatcher) {
        assertThat(values, contains(valueMatcher));
    }

    public final void assertValueSequenceThat(Matcher<? super Iterable<T>> sequenceMatcher) {
        assertThat(values, sequenceMatcher);
    }
}
