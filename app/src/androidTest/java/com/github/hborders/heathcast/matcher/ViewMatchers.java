package com.github.hborders.heathcast.matcher;

import android.view.View;
import android.view.ViewParent;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nullable;

public final class ViewMatchers {
    public static Matcher<View> withAncestor(Matcher<View> ancestorMatcher) {
        return new WithAncestorMatcher(ancestorMatcher);
    }

    public static Matcher<View> atPosition(int position) {
        return new AtPositionMatcher(position);
    }

    private ViewMatchers() {
    }

    private static final class WithAncestorMatcher extends TypeSafeMatcher<View> {
        private final Matcher<View> ancestorMatcher;

        private WithAncestorMatcher(Matcher<View> ancestorMatcher) {
            this.ancestorMatcher = ancestorMatcher;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has ancestor matching: ");
            ancestorMatcher.describeTo(description);
        }

        @Override
        public boolean matchesSafely(View view) {
            for (
                    @Nullable ViewParent viewParent = view.getParent();
                    viewParent != null;
                    viewParent = viewParent.getParent()
            ) {
                if (ancestorMatcher.matches(viewParent)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static class AtPositionMatcher extends TypeSafeMatcher<View> {
        private final int position;

        private AtPositionMatcher(int position) {
            super(View.class);
            this.position = position;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is item at position ").appendValue(position).appendText(": ");
        }

        @Override
        protected boolean matchesSafely(final View view) {
            @Nullable RecyclerView recyclerView = findRecyclerViewAncestor(view);
            if (recyclerView == null) {
                return false;
            }


            @Nullable RecyclerView.ViewHolder viewHolder =
                    recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder == null) {
                // has no item on such position
                return false;
            }
            return viewHolder.itemView == view;
        }

        @Nullable
        private RecyclerView findRecyclerViewAncestor(View view) {
            if (view instanceof RecyclerView) {
                return (RecyclerView) view;
            }

            for (
                    @Nullable ViewParent viewParent = view.getParent();
                    viewParent != null;
                    viewParent = viewParent.getParent()
            ) {
                if (viewParent instanceof RecyclerView) {
                    return (RecyclerView) viewParent;
                }
            }

            return null;
        }
    }

}
