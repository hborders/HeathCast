package com.github.hborders.heathcast.idlingresource;

import android.util.Log;

import androidx.test.espresso.IdlingResource;

public final class IdlingResourceLog {
    private static final String TAG = "IdlingResource";

    public interface MessageProvider {
        String provideMessage();
    }

    public static void v(
            IdlingResource idlingResource,
            String msg
    ) {
        if (
                Log.isLoggable(
                        TAG,
                        Log.VERBOSE
                )
        ) {
            Log.v(
                    TAG,
                    getState(idlingResource) + ", " + msg
            );
        }
    }

    public static void v(
            IdlingResource idlingResource,
            MessageProvider messageProvider
    ) {
        if (
                Log.isLoggable(
                        TAG,
                        Log.VERBOSE
                )
        ) {
            Log.v(
                    TAG,
                    getState(idlingResource) + ", " + messageProvider.provideMessage()
            );
        }
    }

    public static void d(
            IdlingResource idlingResource,
            String msg
    ) {
        if (
                Log.isLoggable(
                        TAG,
                        Log.DEBUG
                )
        ) {
            Log.d(
                    TAG,
                    getState(idlingResource) + ", " + msg
            );
        }
    }

    public static void d(
            IdlingResource idlingResource,
            MessageProvider messageProvider
    ) {
        if (
                Log.isLoggable(
                        TAG,
                        Log.DEBUG
                )
        ) {
            Log.v(
                    TAG,
                    getState(idlingResource) + ", " + messageProvider.provideMessage()
            );
        }
    }

    public static void initial(IdlingResource idlingResource) {
        if (
                Log.isLoggable(
                        TAG,
                        Log.DEBUG
                )
        ) {
            Log.d(TAG, "Created " + getState(idlingResource));
        }
    }

    private static String getState(IdlingResource idlingResource) {
        return idlingResource.getName() + ": " + (idlingResource.isIdleNow() ? "idle" : "busy");
    }

    private IdlingResourceLog() {
    }
}
