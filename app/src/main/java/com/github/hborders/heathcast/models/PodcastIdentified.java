package com.github.hborders.heathcast.models;

public final class PodcastIdentified extends Identified<PodcastIdentifier, Podcast> {
    public PodcastIdentified(
            PodcastIdentifier identifier,
            Podcast model
    ) {
        super(identifier, model);
    }
}
