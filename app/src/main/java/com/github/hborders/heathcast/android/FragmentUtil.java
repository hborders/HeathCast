package com.github.hborders.heathcast.android;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

public final class FragmentUtil {
    private FragmentUtil() {
    }

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
        return BundleUtil.getUnparcelableHolder(
                fragment.getArguments(),
                unparcelableHolderClass,
                key
        );
    }

    public static <U, H extends UnparcelableHolder<U>> Optional<U> getUnparcelableHolderArgumentOptional(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableHolderArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <U, H extends UnparcelableHolder<U>> U requireUnparcelableHolderArgument(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableHolderArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    @Nullable
    public static <U, H extends UnparcelableHolder<U>> List<U> getUnparcelableHolderListArgument(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return BundleUtil.getUnparcelableHolderList(
                fragment.getArguments(),
                unparcelableHolderClass,
                key
        );
    }

    public static <U, H extends UnparcelableHolder<U>> Optional<List<U>> getUnparcelableHolderListArgumentOptional(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableHolderListArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <U, H extends UnparcelableHolder<U>> List<U> requireUnparcelableHolderListArgument(
            Fragment fragment,
            Class<H> unparcelableHolderClass,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableHolderListArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }
}
