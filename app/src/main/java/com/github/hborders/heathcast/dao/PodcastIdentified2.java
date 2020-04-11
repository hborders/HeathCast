package com.github.hborders.heathcast.dao;

public interface PodcastIdentified2<
        PodcastIdentifierType extends PodcastIdentified2.PodcastIdentifier2,
        PodcastType extends Podcast2
        > extends Identified2<
        PodcastIdentifierType,
        PodcastType
        > {
    interface PodcastIdentifier2 extends Identified2.Identifier2 {
    }
}
