package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.core.Opt2;

public interface EpisodeIdentifierOpt2<
        EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
        EpisodeType extends Episode2
        > extends Opt2<EpisodeIdentifierType> {
}
