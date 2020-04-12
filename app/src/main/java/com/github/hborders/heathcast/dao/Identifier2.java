package com.github.hborders.heathcast.dao;

public interface Identifier2 {
    interface IdentifierFactory2<IdentifierType extends Identifier2> {
        IdentifierType newIdentifier(long id);
    }

    long getId();
}
