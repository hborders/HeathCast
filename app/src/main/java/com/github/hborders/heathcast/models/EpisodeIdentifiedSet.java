package com.github.hborders.heathcast.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public final class EpisodeIdentifiedSet extends HashSet<EpisodeIdentified> {
    public EpisodeIdentifiedSet() {
    }

    public EpisodeIdentifiedSet(int initialCapacity) {
        super(initialCapacity);
    }

    public EpisodeIdentifiedSet(Collection<EpisodeIdentified> episodeIdentifieds) {
        super(episodeIdentifieds);
    }

    public EpisodeIdentifiedSet(EpisodeIdentified... episodeIdentifieds) {
        super(Arrays.asList(episodeIdentifieds));
    }
}
