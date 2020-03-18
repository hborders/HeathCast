package com.github.hborders.heathcast.viewaction;

import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ClickSearchViewCloseButtonViewAction implements ViewAction {
    public static ViewAction clickSearchViewCloseButton() {
        return new ClickSearchViewCloseButtonViewAction();
    }

    @Override
    public Matcher<View> getConstraints() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof SearchView)) {
                    return false;
                }
                final SearchView searchView = (SearchView) item;
                final int closeButtonId = searchView
                        .getContext()
                        .getResources()
                        .getIdentifier(
                                "android:id/search_close_btn",
                                null,
                                null
                        );
                @Nullable final View closeButton = searchView.findViewById(closeButtonId);
                return closeButton != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Is a SearchView (and captures the android:id/search_close_btn value)");
            }
        };
    }

    @Override
    public String getDescription() {
        return "Click a SearchView's Close Button";
    }

    @Override
    public void perform(UiController uiController, View view) {
        final SearchView searchView = (SearchView) view;
        final int closeButtonId = searchView
                .getContext()
                .getResources()
                .getIdentifier(
                        "android:id/search_close_btn",
                        null,
                        null
                );
        @Nullable final View closeButton = searchView.findViewById(closeButtonId);
        ViewActions.click().perform(uiController, closeButton);
    }
}
