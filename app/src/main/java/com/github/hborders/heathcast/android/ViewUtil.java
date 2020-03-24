package com.github.hborders.heathcast.android;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public final class ViewUtil {
    @SuppressLint("ClickableViewAccessibility")
    private static final View.OnTouchListener DISABLE_USER_INTERACTION_ON_TOUCH_LISTENER =
            (
                    View view,
                    MotionEvent motionEvent
            ) -> true;

    // https://stackoverflow.com/a/26586277/9636
    public static void setUserInteractionEnabled(
            View view,
            boolean userInteractionEnabled
    ) {
        if (userInteractionEnabled) {
            view.setOnTouchListener(null);
        } else {
            view.setOnTouchListener(DISABLE_USER_INTERACTION_ON_TOUCH_LISTENER);
        }
    }

    private ViewUtil() {
    }
}
