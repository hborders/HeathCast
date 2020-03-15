package com.github.hborders.heathcast.core;

public interface CollectionFactory {
    interface Capacity<C extends java.util.Collection<M>, M> {
        C newCollection(int capacity);
    }

    interface Collection<C extends java.util.Collection<M>, M> {
        C newCollection(java.util.Collection<? extends M> items);
    }

    interface Empty<C extends java.util.Collection<M>, M> {
        C newCollection();
    }
}
