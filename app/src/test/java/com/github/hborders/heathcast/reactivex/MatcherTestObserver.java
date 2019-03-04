package com.github.hborders.heathcast.reactivex;

import com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil;

import org.hamcrest.Matcher;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

public final class MatcherTestObserver<T> extends TestObserver<T> {
    public final void assertValueThat(Matcher<? super T> valueMatcher) {
        assertThat(values, IsIterableContainingInOrderUtil.containsInOrder(valueMatcher));
    }

    public final void assertValueSequenceThat(Matcher<? super Iterable<T>> sequenceMatcher) {
        assertThat(
                "actual values: " + values,
                values,
                sequenceMatcher
        );
    }

    @Nullable
    public final T lastValue() {
        if (values.isEmpty()) {
            return null;
        } else {
            return values.get(valueCount() - 1);
        }
    }

    public final void assertLastValue(T expectedLastValue) {
        assertValueAt(
                valueCount() - 1,
                expectedLastValue
        );
    }
}
