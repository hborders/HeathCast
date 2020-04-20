package com.github.hborders.heathcast.models;

public interface Identified<
        IdentifierType extends Identifier,
        ModelType
        > {
    interface IdentifiedFactory2<
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier,
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
