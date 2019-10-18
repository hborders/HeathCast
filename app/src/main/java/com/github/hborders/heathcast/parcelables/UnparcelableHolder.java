package com.github.hborders.heathcast.parcelables;

import android.os.Parcelable;

import javax.annotation.Nullable;

public interface UnparcelableHolder<U> extends Parcelable {
    @Nullable U getUnparcelable();
}
