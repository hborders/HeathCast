package com.github.hborders.heathcast.parcelables;

import android.os.Parcelable;

import javax.annotation.Nullable;

public interface UnparcelableHolder<U> extends Parcelable {
    interface Factory<
            U,
            H extends UnparcelableHolder<U>
            > {
        H newUnparcelableHolder(U unparcelable);
    }
    interface ArrayFactory<
            U,
            H extends UnparcelableHolder<U>
            > {
        H[] newUnparcelableHolderArray(int size);
    }

    @Nullable U getUnparcelable();
}
