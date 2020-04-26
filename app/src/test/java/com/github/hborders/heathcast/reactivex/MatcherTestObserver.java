package com.github.hborders.heathcast.reactivex;

import androidx.annotation.Nullable;

import org.hamcrest.Matcher;

import io.reactivex.rxjava3.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static org.hamcrest.MatcherAssert.assertThat;

public final class MatcherTestObserver<T> extends TestObserver<T> {
    public final void assertValueThat(Matcher<? super T> valueMatcher) {
        assertNoErrors();
        assertThat(
                values,
                containsInOrder(valueMatcher)
        );
    }

    public final void assertValueSequenceThat(Matcher<? super Iterable<T>> sequenceMatcher) {
        assertNoErrors();
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
            return values.get(values.size() - 1);
        }
    }

    public final void assertLastValue(T expectedLastValue) {
        assertNoErrors();
        assertValueAt(
                values.size() - 1,
                expectedLastValue
        );
    }
}
