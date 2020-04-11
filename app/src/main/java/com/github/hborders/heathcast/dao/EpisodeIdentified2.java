package com.github.hborders.heathcast.dao;

public interface EpisodeIdentified2 extends Identified2<
        EpisodeIdentified2.EpisodeIdentifier2,
        Episode2
        >, Episode2 {
    interface EpisodeIdentifier2 extends Identified2.Identifier2 {
    }
}
