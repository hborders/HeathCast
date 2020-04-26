package com.github.hborders.heathcast.models;

public interface Versioned<ValueType> {
    ValueType getValue();
    long getVersion();
}
