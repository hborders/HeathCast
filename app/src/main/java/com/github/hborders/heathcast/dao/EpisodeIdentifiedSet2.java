package com.github.hborders.heathcast.dao;

import java.util.Set;

public interface EpisodeIdentifiedSet2<
        EpisodeIdentifiedType extends EpisodeIdentified2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
        EpisodeType extends Episode2
        > extends Set<EpisodeIdentifiedType> {
}
