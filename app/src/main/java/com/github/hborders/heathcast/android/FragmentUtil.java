package com.github.hborders.heathcast.android;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>
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
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>
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
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableOptType extends Opt<UnparcelableType>,
            UnparcelableOptFactoryType extends Opt.Factory<UnparcelableOptType, UnparcelableType>
            > Opt<UnparcelableType> getUnparcelableArgumentOpt(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            UnparcelableOptFactoryType unparcelableOptFactory,
            String key
    ) {
        return unparcelableOptFactory.ofNullable(
                getUnparcelableArgument(
                        fragment,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>
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
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparceableListType extends List<UnparcelableType>
            > UnparceableListType getUnparcelableListArgument(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparceableListType, UnparcelableType> capacityCollectionFactory,
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
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparceableListType extends List<UnparcelableType>
            > Optional<UnparceableListType> getUnparcelableListArgumentOptional(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparceableListType, UnparcelableType> capacityCollectionFactory,
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
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparceableListType extends List<UnparcelableType>,
            UnparcelableListOptType extends Opt<UnparceableListType>,
            UnparcelableListOptFactoryType extends Opt.Factory<UnparcelableListOptType, UnparceableListType>
            > UnparcelableListOptType getUnparcelableListArgumentOpt(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparceableListType, UnparcelableType> capacityCollectionFactory,
            UnparcelableListOptFactoryType unparcelableListOptFactory,
            String key
    ) {
        return unparcelableListOptFactory.ofNullable(
                getUnparcelableListArgument(
                        fragment,
                        unparcelableHolderClass,
                        capacityCollectionFactory,
                        key
                )
        );
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparceableListType extends List<UnparcelableType>
            > UnparceableListType requireUnparcelableListArgument(
            Fragment fragment,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparceableListType, UnparcelableType> capacityCollectionFactory,
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
}
