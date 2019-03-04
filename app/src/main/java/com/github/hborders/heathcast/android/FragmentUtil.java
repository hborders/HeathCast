package com.github.hborders.heathcast.android;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FragmentUtil {
    private FragmentUtil() {
    }

    @Nonnull
    public static <L> L requireFragmentListener(
            Fragment fragment,
            Context context,
            Class<L> listenerClass
    ) {
        if (listenerClass.isInstance(context)) {
            return Objects.requireNonNull(listenerClass.cast(context));
        } else {
            return requireParentFragmentFragmentListenerRecursive(
                    fragment,
                    listenerClass
            );
        }
    }

    @Nonnull
    public static <L> L requireContextFragmentListener(
            Context context,
            Class<L> listenerClass
    ) {
        if (listenerClass.isInstance(context)) {
            return Objects.requireNonNull(listenerClass.cast(context));
        } else {
            throw new IllegalStateException(listenerClass + " is not implemented by context: " + context);
        }
    }

    @Nonnull
    public static <L> L requireParentFragmentFragmentListenerRecursive(
            Fragment fragment,
            Class<L> listenerClass
    ) {
        return requireParentFragmentFragmentListenerRecursive(
                fragment,
                fragment,
                listenerClass
        );
    }

    @Nonnull
    private static <L> L requireParentFragmentFragmentListenerRecursive(
            Fragment originalFragment,
            Fragment recursingFragment,
            Class<L> listenerClass
    ) {
        final @Nullable Fragment parentFragment = recursingFragment.getParentFragment();
        if (parentFragment == null) {
            throw new IllegalStateException("No parent fragment implements " + listenerClass + " in the ancestry of: " + originalFragment);
        }

        if (listenerClass.isInstance(parentFragment)) {
            return Objects.requireNonNull(listenerClass.cast(parentFragment));
        } else {
            return requireParentFragmentFragmentListenerRecursive(
                    originalFragment,
                    parentFragment,
                    listenerClass
            );
        }
    }

    @Nullable
    public static <U, H extends UnparcelableHolder<U>> U getUnparcelableHolderArgument(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        @Nullable final Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            return null;
        } else {
            @Nullable final Parcelable parcelable = arguments.getParcelable(key);
            if (unparcelableHolderClass.isInstance(parcelable)) {
                final UnparcelableHolder<U> unparcelableHolder =
                        Objects.requireNonNull(unparcelableHolderClass.cast(parcelable));
                return unparcelableHolder.getUnparcelable();
            } else {
                return null;
            }
        }
    }

    @Nullable
    public static <U, H extends UnparcelableHolder<U>> List<U> getUnparcelableHolderListArgument(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        @Nullable final Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            return null;
        } else {
            @Nullable final Parcelable[] parcelableArray = arguments.getParcelableArray(key);
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
}
