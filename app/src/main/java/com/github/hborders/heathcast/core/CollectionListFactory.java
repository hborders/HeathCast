package com.github.hborders.heathcast.core;

import java.util.Collection;
import java.util.List;

public interface CollectionListFactory<L extends List<M>, M> {
    L newList(Collection<? extends M> items);
}
