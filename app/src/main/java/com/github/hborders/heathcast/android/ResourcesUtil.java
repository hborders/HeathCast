package com.github.hborders.heathcast.android;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.RawRes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourcesUtil {
    private static final String TAG = "ResourcesUtil";

    public static String readRawUtf8Resource(
            Resources resources,
            @RawRes int rawRes
    ) throws IOException {
        final InputStream inputStream;
        final int maxSize;
        try (final AssetFileDescriptor assetFileDescriptor = resources.openRawResourceFd(rawRes)) {
            long maxSizeLong = assetFileDescriptor.getLength();
            if (maxSizeLong == AssetFileDescriptor.UNKNOWN_LENGTH) {
                throw new IOException("Can't read raw resources of unknown length");
            }
            if (maxSizeLong > (long) Integer.MAX_VALUE) {
                throw new IOException("Raw Resource is too big: " + maxSizeLong);
            }
            inputStream = assetFileDescriptor.createInputStream();
            maxSize = (int) maxSizeLong;
        } catch (Resources.NotFoundException rethrown) {
            Log.e(
                    TAG,
                    "NotFoundException",
                    rethrown
            );
            throw new IOException(rethrown);
        }

        final byte[] bytes = new byte[maxSize];
        int totalBytesRead = 0;
        while (true) {
            int bytesRead = inputStream.read(
                    bytes,
                    totalBytesRead,
                    maxSize - totalBytesRead
            );
            if (bytesRead != -1) {
                totalBytesRead += bytesRead;
            } else {
                break;
            }
        }
        return new String(
                bytes,
                0,
                totalBytesRead,
                StandardCharsets.UTF_8
        );
    }

    private ResourcesUtil() {
    }
}
