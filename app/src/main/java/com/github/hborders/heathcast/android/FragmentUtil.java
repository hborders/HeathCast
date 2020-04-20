package com.github.hborders.heathcast.android;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class FragmentUtil {
    public static <ListenerType> ListenerType requireFragmentListener(
            Fragment fragment,
            Context context,
            Class<ListenerType> listenerClass
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

    public static <ListenerType> ListenerType requireContextFragmentListener(
            Context context,
            Class<ListenerType> listenerClass
    ) {
        if (listenerClass.isInstance(context)) {
            return Objects.requireNonNull(listenerClass.cast(context));
        } else {
            throw new IllegalStateException(listenerClass + " is not implemented by context: " + context);
        }
    }

    public static <ListenerType> ListenerType requireParentFragmentFragmentListenerRecursive(
            Fragment fragment,
            Class<ListenerType> listenerClass
    ) {
        return requireParentFragmentFragmentListenerRecursive(
                fragment,
                fragment,
                listenerClass
        );
    }

    private static <ListenerType> ListenerType requireParentFragmentFragmentListenerRecursive(
            Fragment originalFragment,
            Fragment recursingFragment,
            Class<ListenerType> listenerClass
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
    public static <
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType
            > UnparcelableType getUnparcelableArgument(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            String key
    ) {
        return BundleUtil.getUnparcelable(
                fragment.getArguments(),
                unparcelableHolderClass,
                key
        );
    }

    public static <
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType
            > Optional<UnparcelableType> getUnparcelableArgumentOptional(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType
            > UnparcelableType requireUnparcelableArgument(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    @Nullable
    public static <
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType,
            UnparceableListType extends List<UnparcelableType>
            > UnparceableListType getUnparcelableListArgument(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<
                    UnparceableListType,
                    UnparcelableType
                    > capacityCollectionFactory,
            String key
    ) {
        return BundleUtil.getUnparcelableList(
                fragment.getArguments(),
                unparcelableHolderClass,
                capacityCollectionFactory,
                key
        );
    }

    public static <
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType,
            UnparceableListType extends List<UnparcelableType>
            > Optional<UnparceableListType> getUnparcelableListArgumentOptional(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<
                    UnparceableListType,
                    UnparcelableType
                    > capacityCollectionFactory,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableListArgument(
                        fragment,
                        unparcelableHolderClass,
                        capacityCollectionFactory,
                        key
                )
        );
    }

    public static <
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType,
            UnparceableListType extends List<UnparcelableType>
            > UnparceableListType requireUnparcelableListArgument(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<
                    UnparceableListType,
                    UnparcelableType
                    > capacityCollectionFactory,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableListArgument(
                        fragment,
                        unparcelableHolderClass,
                        capacityCollectionFactory,
                        key
                )
        );
    }

    private FragmentUtil() {
    }
}
