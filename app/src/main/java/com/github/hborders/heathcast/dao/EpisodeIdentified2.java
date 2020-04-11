package com.github.hborders.heathcast.dao;

public interface EpisodeIdentified2<
        EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
        EpisodeType extends Episode2
        > extends Identified2<
        EpisodeIdentifierType,
        EpisodeType
        >, Episode2 {
    interface EpisodeIdentifier2 extends Identified2.Identifier2 {
    }
}
