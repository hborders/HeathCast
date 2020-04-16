package com.github.hborders.heathcast.parcelables;

import android.os.Parcelable;

import androidx.annotation.Nullable;

public interface UnparcelableHolder<UnparcelableType> extends Parcelable {
    interface UnparcelableHolderFactory<
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType
            > {
        UnparcelableHolderType newUnparcelableHolder(UnparcelableType unparcelable);
    }

    interface UnparcelableHolderArrayFactory<
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableType
            > {
        UnparcelableHolderType[] newUnparcelableHolderArray(int size);
    }

    @Nullable
    UnparcelableType getUnparcelable();
}
