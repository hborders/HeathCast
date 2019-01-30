package com.github.hborders.heathcast.utils;

import android.os.Parcel;

import java.net.URL;
import java.time.Duration;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.utils.URLUtil.fromString;

public final class ParcelUtil {
    private ParcelUtil() {
    }

    public static void writeURL(
            Parcel dest,
            @Nullable URL url
    ) {
        if (url == null) {
            dest.writeString(null);
        } else {
            dest.writeString(url.toExternalForm());
        }
    }

    @Nullable
    public static URL readURL(Parcel in) {
        return fromString(in.readString());
    }

    public static void writeDuration(
            Parcel dest,
            @Nullable Duration duration) {
        if (duration == null) {
            dest.writeLong(-1);
        } else {
            dest.writeLong(duration.getSeconds());
            dest.writeInt(duration.getNano());
        }
    }

    @Nullable
    public static Duration readDuration(Parcel in) {
        final long seconds = in.readLong();
        if (seconds < 0) {
            return null;
        } else {
            final int nano = in.readInt();
            return Duration.ofSeconds(
                    seconds,
                    nano
            );
        }
    }
}
