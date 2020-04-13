package com.github.hborders.heathcast.features.model;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ModelListImpl<ModelType> extends ArrayList<ModelType> {
    protected ModelListImpl() {
    }

    protected ModelListImpl(int initialCapacity) {
        super(initialCapacity);
    }

    protected ModelListImpl(Collection<? extends ModelType> c) {
        super(c);
    }
}
