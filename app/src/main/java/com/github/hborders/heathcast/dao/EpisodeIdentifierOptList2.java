package com.github.hborders.heathcast.dao;

import java.util.List;

public interface EpisodeIdentifierOptList2<
        EpisodeIdentifierOptType extends EpisodeIdentifierOpt2<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
        EpisodeType extends Episode2
        > extends List<EpisodeIdentifierOptType> {
}
