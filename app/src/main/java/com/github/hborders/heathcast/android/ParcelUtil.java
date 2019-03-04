package com.github.hborders.heathcast.android;

import android.os.Parcel;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.core.URLUtil.fromString;

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
            @Nullable Duration duration
    ) {
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

    public static void writeDate(
            Parcel dest,
            @Nullable Date date
    ) {
        if (date == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(date.getTime());
        }
    }

    @Nullable
    public static Date readDate(
        Parcel in
    ) {
        final byte exists = in.readByte();
        if (exists == 0) {
            return null;
        } else {
            final long time = in.readLong();
            return new Date(time);
        }
    }
}
