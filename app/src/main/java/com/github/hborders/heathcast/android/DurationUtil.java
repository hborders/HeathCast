package com.github.hborders.heathcast.android;

import java.time.Duration;

import javax.annotation.Nullable;

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
