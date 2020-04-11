package com.github.hborders.heathcast.dao;

import java.util.List;

public interface EpisodeIdentifiedList2<
        EpisodeIdentifiedType extends EpisodeIdentified2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
        EpisodeType extends Episode2
        > extends List<EpisodeIdentifiedType> {
}
