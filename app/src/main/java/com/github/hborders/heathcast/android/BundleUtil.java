package com.github.hborders.heathcast.android;

import android.os.Bundle;
import android.os.Parcelable;

import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public final class BundleUtil {
    @Nullable
    public static <U, H extends UnparcelableHolder<U>> U getUnparcelableHolder(
            @Nullable Bundle bundle,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        if (bundle == null) {
            return null;
        } else {
            @Nullable final Parcelable parcelable = bundle.getParcelable(key);
            if (unparcelableHolderClass.isInstance(parcelable)) {
                final UnparcelableHolder<U> unparcelableHolder =
                        Objects.requireNonNull(unparcelableHolderClass.cast(parcelable));
                return unparcelableHolder.getUnparcelable();
            } else {
                return null;
            }
        }
    }

    public static <U, H extends UnparcelableHolder<U>> Optional<U> getUnparcelableHolderOptional(
            @Nullable Bundle bundle,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableHolder(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <U, H extends UnparcelableHolder<U>> U requireUnparcelableHolderArgument(
            @Nullable Bundle bundle,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableHolder(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    @Nullable
    public static <U, H extends UnparcelableHolder<U>> List<U> getUnparcelableHolderList(
            @Nullable Bundle bundle,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        if (bundle == null) {
            return null;
        } else {
            @Nullable final Parcelable[] parcelableArray = bundle.getParcelableArray(key);
            if (parcelableArray == null) {
                return null;
            } else {
                return Arrays
                        .stream(parcelableArray)
                        .filter(unparcelableHolderClass::isInstance)
                        .map(parcelable -> {
                            // could be more cleanly represented a method references,
                            // but Android Studio's Inspector incorrectly reports
                            // that the method references might cause a NullPointerException
                            // because it doesn't can't infer null safety through the
                            // Java Streams API
                            // https://stackoverflow.com/q/54360011/9636
                            @Nullable final UnparcelableHolder<U> unparcelableHolder =
                                    unparcelableHolderClass.cast(parcelable);
                            return Objects.requireNonNull(unparcelableHolder).getUnparcelable();
                        })
                        .collect(Collectors.toList());
            }
        }
    }

    public static <U, H extends UnparcelableHolder<U>> Optional<List<U>> getUnparcelableHolderListOptional(
            @Nullable Bundle bundle,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableHolderList(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <U, H extends UnparcelableHolder<U>> List<U> requireUnparcelableHolderList(
            @Nullable Bundle bundle,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableHolderList(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    private BundleUtil() {
    }
}
