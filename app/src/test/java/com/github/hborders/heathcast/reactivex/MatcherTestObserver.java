package com.github.hborders.heathcast.reactivex;

import org.hamcrest.Matcher;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

public final class MatcherTestObserver<T> extends TestObserver<T> {
    public final void assertValueSequenceThat(Matcher<? super Iterable<T>> sequenceMatcher) {
        assertThat(values, sequenceMatcher);
    }
}
