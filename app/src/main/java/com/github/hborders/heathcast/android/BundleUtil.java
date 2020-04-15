package com.github.hborders.heathcast.android;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BundleUtil {
    private BundleUtil() {
    }

    @Nullable
    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>
            > UnparcelableType getUnparcelable(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            String key
    ) {
        if (bundle == null) {
            return null;
        } else {
            @Nullable final Parcelable parcelable = bundle.getParcelable(key);
            if (unparcelableHolderClass.isInstance(parcelable)) {
                @Nullable final UnparcelableHolderType unparcelableHolder =
                        unparcelableHolderClass.cast(parcelable);
                return Objects.requireNonNull(unparcelableHolder).getUnparcelable();
            } else {
                return null;
            }
        }
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>
            > Optional<UnparcelableType> getUnparcelableOptional(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelable(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableOptType extends Opt<UnparcelableType>,
            UnparcelableOptFactoryType extends Opt.Factory<
                                UnparcelableOptType,
                                UnparcelableType
                                >
            > UnparcelableOptType getUnparcelableOpt(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            UnparcelableOptFactoryType unparcelableOptFactory,
            String key
    ) {
        return unparcelableOptFactory.ofNullable(
                getUnparcelable(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }


    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>
            > UnparcelableType requireUnparcelableArgument(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelable(
                        bundle,
                        unparcelableHolderClass,
                        key
                )
        );
    }

    @Nullable
    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableListType extends List<UnparcelableType>
            > UnparcelableListType getUnparcelableList(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparcelableListType, UnparcelableType> capacityCollectionFactory,
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
                            @Nullable final UnparcelableHolderType unparcelableHolder =
                                    unparcelableHolderClass.cast(parcelable);
                            return Objects.requireNonNull(unparcelableHolder).getUnparcelable();
                        })
                        .collect(
                                Collectors.toCollection(
                                        // we're optimistic that all parcelables will parse into unparcelables
                                        () -> capacityCollectionFactory.newCollection(parcelableArray.length)
                                )
                        );
            }
        }
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableListType extends List<UnparcelableType>
            > Optional<UnparcelableListType> getUnparcelableListOptional(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparcelableListType, UnparcelableType> capacityCollectionFactory,
            String key
    ) {
        return Optional.ofNullable(
                getUnparcelableList(
                        bundle,
                        unparcelableHolderClass,
                        capacityCollectionFactory,
                        key
                )
        );
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableListType extends List<UnparcelableType>,
            UnparcelableListOptType extends Opt<UnparcelableListType>,
            UnparcelableListOptFactoryType extends Opt.Factory<UnparcelableListOptType, UnparcelableListType>
            > UnparcelableListOptType getUnparcelableListOpt(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparcelableListType, UnparcelableType> capacityCollectionFactory,
            UnparcelableListOptFactoryType unparcelableListOptFactory,
            String key
    ) {
        return unparcelableListOptFactory.ofNullable(
                getUnparcelableList(
                        bundle,
                        unparcelableHolderClass,
                        capacityCollectionFactory,
                        key
                )
        );
    }

    public static <
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableListType extends List<UnparcelableType>
            > UnparcelableListType requireUnparcelableList(
            @Nullable Bundle bundle,
            Class<UnparcelableHolderType> unparcelableHolderClass,
            CollectionFactory.Capacity<UnparcelableListType, UnparcelableType> capacityCollectionFactory,
            String key
    ) {
        return Objects.requireNonNull(
                getUnparcelableList(
                        bundle,
                        unparcelableHolderClass,
                        capacityCollectionFactory,
                        key
                )
        );
    }
}
