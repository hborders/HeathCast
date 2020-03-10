package com.github.hborders.heathcast.core;

import java.util.List;

public interface CapacityListFactory<L extends List<M>, M> {
    L newList(int capacity);
}
