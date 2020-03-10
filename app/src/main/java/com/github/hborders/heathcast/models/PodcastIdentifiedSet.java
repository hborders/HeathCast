package com.github.hborders.heathcast.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class PodcastIdentifiedSet extends HashSet<PodcastIdentified> {
    public PodcastIdentifiedSet() {
    }

    public PodcastIdentifiedSet(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastIdentifiedSet(Collection<PodcastIdentified> podcastIdentifieds) {
        super(podcastIdentifieds);
    }

    public PodcastIdentifiedSet(PodcastIdentified... podcastIdentifieds) {
        super(Arrays.asList(podcastIdentifieds));
    }
}
