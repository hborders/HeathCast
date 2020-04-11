package com.github.hborders.heathcast.dao;

public interface Identified2<
        IdentifierType extends Identified2.Identifier2,
        ModelType
        > {
    interface IdentifiedFactory2<
            IdentifiedType extends Identified2<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identified2.Identifier2,
            ModelType
            > {
        IdentifiedType newIdentified(
                IdentifierType identifier,
                ModelType model
        );
    }
    interface Identifier2 {
        interface IdentifierFactory2<IdentifierType extends Identifier2> {
            IdentifierType newIdentifier(long id);
        }

        long getId();
    }

    IdentifierType getIdentifier();

    ModelType getModel();
}
