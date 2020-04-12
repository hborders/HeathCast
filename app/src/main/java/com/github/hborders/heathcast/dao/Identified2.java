package com.github.hborders.heathcast.dao;

public interface Identified2<
        IdentifierType extends Identifier2,
        ModelType
        > {
    interface IdentifiedFactory2<
            IdentifiedType extends Identified2<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier2,
            ModelType
            > {
        IdentifiedType newIdentified(
                IdentifierType identifier,
                ModelType model
        );
    }

    IdentifierType getIdentifier();

    ModelType getModel();
}
