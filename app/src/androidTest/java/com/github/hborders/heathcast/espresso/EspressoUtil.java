package com.github.hborders.heathcast.espresso;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.github.hborders.heathcast.core.Function;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class EspressoUtil {
    public static <ValueType> ValueType requireValue(
            Matcher<View> viewMatcher,
            Function<View, ValueType> valueFunction
    ) {
        return requireValue(
                viewMatcher,
                View.class,
                valueFunction
        );
    }

    public static <ViewType extends View, ValueType> ValueType requireValue(
            Matcher<View> viewMatcher,
            Class<ViewType> viewClass,
            Function<ViewType, ValueType> valueFunction
    ) {
        AtomicReference<ValueType> valueAtomicReference = new AtomicReference<>();
        Espresso.onView(viewMatcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(viewClass);
            }

            @Override
            public String getDescription() {
                StringDescription stringDescription = new StringDescription();
                stringDescription
                        .appendText("Getting value from ")
                        .appendDescriptionOf(viewMatcher)
                        .appendText(" with type ")
                        .appendValue(viewClass);
                return stringDescription.toString();
            }

            @Override
            public void perform(UiController uiController, View view) {
                final ValueType value = valueFunction.apply(Objects.requireNonNull(viewClass.cast(view)));
                valueAtomicReference.set(value);
            }
        });
        return Objects.requireNonNull(valueAtomicReference.get());
    }

    public interface NullableFunction<T, R> {
        @Nullable
        R apply(T t);
    }

    @Nullable
    public static <ValueType> ValueType findValue(
            Matcher<View> viewMatcher,
            NullableFunction<View, ValueType> valueFunction
    ) {
        return findValue(
                viewMatcher,
                View.class,
                valueFunction
        );
    }

    @Nullable
    public static <ViewType extends View, ValueType> ValueType findValue(
            Matcher<View> viewMatcher,
            Class<ViewType> viewClass,
            NullableFunction<ViewType, ValueType> valueFunction
    ) {
        // use an Optional to guarantee that we actually ran the perform method.
        AtomicReference<Optional<ValueType>> valueAtomicReference = new AtomicReference<>();
        Espresso.onView(viewMatcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(viewClass);
            }

            @Override
            public String getDescription() {
                StringDescription stringDescription = new StringDescription();
                stringDescription
                        .appendText("Getting value from ")
                        .appendDescriptionOf(viewMatcher)
                        .appendText(" with type ")
                        .appendValue(viewClass);
                return stringDescription.toString();
            }

            @Override
            public void perform(UiController uiController, View view) {
                @Nullable final ValueType value = valueFunction.apply(Objects.requireNonNull(viewClass.cast(view)));
                valueAtomicReference.set(Optional.ofNullable(value));
            }
        });

        return Objects.requireNonNull(valueAtomicReference.get()).orElse(null);
    }

    private EspressoUtil() {
    }
}
