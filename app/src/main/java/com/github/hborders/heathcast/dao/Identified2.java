package com.github.hborders.heathcast.dao;

public interface Identified2<
        IdentifierType extends Identified2.Identifier2,
        ModelType
        > {
    interface Identifier2 {
        long getId();
    }

    IdentifierType getIdentifier();

    ModelType getModel();
}
