package com.github.hborders.heathcast.android;

import androidx.annotation.Nullable;

import java.time.Duration;

public final class DurationUtil {
    private DurationUtil() {
    }

    @Nullable
    public static Duration ofSecondsString(@Nullable String secondsString) {
        if (secondsString == null) {
            return null;
        } else {
            final long seconds;
            try {
                seconds = Long.parseLong(secondsString);
                return Duration.ofSeconds(seconds);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
