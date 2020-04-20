package com.github.hborders.heathcast.models;

public interface Identifier {
    interface IdentifierFactory2<IdentifierType extends Identifier> {
        IdentifierType newIdentifier(long id);
    }

    long getId();
}
