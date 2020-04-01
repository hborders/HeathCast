/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hborders.heathcast.android;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;

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

    // taken from androidx.core.view.View.kt
    // translated from Kotlin to Java

    public interface ViewAction {
        void act(View view);
    }

    /**
     * Performs the given action when this view is next laid out.
     * <p>
     * The action will only be invoked once on the next layout and then removed.
     *
     * @see #doOnLayout
     */
    public static void doOnNextLayout(
            View view,
            ViewAction viewAction
    ) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View view_,
                    int left,
                    int top,
                    int right,
                    int bottom,
                    int oldLeft,
                    int oldTop,
                    int oldRight,
                    int oldBottom
            ) {
                // it's important that we reference [view_] here and not [view]
                // so that we don't need to call extra accessors
                view_.removeOnLayoutChangeListener(this);
                viewAction.act(view_);
            }
        });
    }

    /**
     * Performs the given action when this view is laid out. If the view has been laid out and it
     * has not requested a layout, the action will be performed straight away, otherwise the
     * action will be performed after the view is next laid out.
     * <p>
     * The action will only be invoked once on the next layout and then removed.
     *
     * @see #doOnNextLayout
     */
    public static void doOnLayout(
            View view,
            ViewAction viewAction
    ) {
        if (ViewCompat.isLaidOut(view)) {
            viewAction.act(view);
        } else {
            doOnNextLayout(
                    view,
                    viewAction
            );
        }
    }

    /**
     * Performs the given action when the view tree is about to be drawn.
     *
     * The action will only be invoked once prior to the next draw and then removed.
     */
    public static OneShotPreDrawListener doOnPreDraw(
            View view,
            ViewAction viewAction
    ) {
        return OneShotPreDrawListener.add(
                view,
                () -> {
                    viewAction.act(view);
                }
        );
    }

    /**
     * Performs the given action when this view is attached to a window. If the view is already
     * attached to a window the action will be performed immediately, otherwise the
     * action will be performed after the view is next attached.
     *
     * The action will only be invoked once, and any listeners will then be removed.
     *
     * @see #doOnDetach
     */
    public static void doOnAttach(
            View view,
            ViewAction viewAction
    ) {
        if (ViewCompat.isAttachedToWindow(view)) {
            viewAction.act(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view_) {
                    // it's important that we reference [view_] here and not [view]
                    // so that we don't need to call extra accessors
                    view_.removeOnAttachStateChangeListener(this);
                    viewAction.act(view_);
                }

                @Override
                public void onViewDetachedFromWindow(View view_) {}
            });
        }
    }

    /**
     * Performs the given action when this view is detached from a window. If the view is not
     * attached to a window the action will be performed immediately, otherwise the
     * action will be performed after the view is detached from its current window.
     *
     * The action will only be invoked once, and any listeners will then be removed.
     *
     * @see #doOnAttach
     */
    public static void doOnDetach(
            View view,
            ViewAction viewAction
    ) {
        if (!ViewCompat.isAttachedToWindow(view)) {
            viewAction.act(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view_) {
                }

                @Override
                public void onViewDetachedFromWindow(View view_) {
                    // it's important that we reference [view_] here and not [view]
                    // so that we don't need to call extra accessors
                    view_.removeOnAttachStateChangeListener(this);
                    viewAction.act(view_);
                }
            });
        }
    }

    /**
     * @see #drawToBitmap(View, android.graphics.Bitmap.Config)
     */
    public static Bitmap drawToBitmap(View view) {
        return drawToBitmap(
                view,
                Bitmap.Config.ARGB_8888
        );
    }

    /**
     * Return a [Bitmap] representation of this [View].
     *
     * The resulting bitmap will be the same width and height as this view's current layout
     * dimensions. This does not take into account any transformations such as scale or translation.
     *
     * Note, this will use the software rendering pipeline to draw the view to the bitmap. This may
     * result with different drawing to what is rendered on a hardware accelerated canvas (such as
     * the device screen).
     *
     * If this view has not been laid out this method will throw a [IllegalStateException].
     *
     * @param config Bitmap config of the desired bitmap. Defaults to [Bitmap.Config.ARGB_8888].
     */
    public static Bitmap drawToBitmap(
            View view,
            Bitmap.Config config
    ) {
        if (!ViewCompat.isLaidOut(view)) {
            throw new IllegalStateException("View needs to be laid out before calling drawToBitmap()");
        }
        final Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                config
        );
        final Canvas canvas = new Canvas(bitmap);
        canvas.translate(
                -((float) view.getScrollX()),
                -((float) view.getScrollY())
        );
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Returns true when this view's visibility is [View.VISIBLE], false otherwise.
     *
     * ```
     * if (view.isVisible) {
     *     // Behavior...
     * }
     * ```
     */
    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    /**
     * Setting this property to true sets the visibility to [View.VISIBLE], false to [View.GONE].
     *
     * ```
     * view.isVisible = true
     * ```
     */
    public static void setVisible(
            View view,
            boolean visible
    ) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Returns true when this view's visibility is [View.INVISIBLE], false otherwise.
     *
     * ```
     * if (view.isInvisible) {
     *     // Behavior...
     * }
     * ```
     */
    public static boolean isInvisible(View view) {
        return view.getVisibility() == View.INVISIBLE;
    }

    /**
     * Returns true when this view's visibility is [View.INVISIBLE], false otherwise.
     *
     * ```
     * if (view.isInvisible) {
     *     // Behavior...
     * }
     * ```
     */
    public static void setInvisible(
            View view,
            boolean invisible
    ) {
        view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Returns true when this view's visibility is [View.GONE], false otherwise.
     *
     * ```
     * if (view.isGone) {
     *     // Behavior...
     * }
     * ```
     *
     * Setting this property to true sets the visibility to [View.GONE], false to [View.VISIBLE].
     *
     * ```
     * view.isGone = true
     * ```
     */
    public static boolean isGone(View view) {
        return view.getVisibility() == View.GONE;
    }

    /**
     * Setting this property to true sets the visibility to [View.GONE], false to [View.VISIBLE].
     *
     * ```
     * view.isGone = true
     * ```
     */
    public static void setGone(
            View view,
            boolean gone
    ) {
        view.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    private ViewUtil() {
    }
}
