package com.github.hborders.heathcast.parcelables;

import javax.annotation.Nullable;

public interface UnparcelableHolder<U> {
    @Nullable U getUnparcelable();
}
