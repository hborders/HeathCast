package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class EpisodeIdentifiedList extends ArrayList<EpisodeIdentified> {
    public EpisodeIdentifiedList() {
    }

    public EpisodeIdentifiedList(int initialCapacity) {
        super(initialCapacity);
    }

    public EpisodeIdentifiedList(Collection<EpisodeIdentified> episodeIdentifieds) {
        super(episodeIdentifieds);
    }

    public EpisodeIdentifiedList(EpisodeIdentified... episodeIdentifieds) {
        super(Arrays.asList(episodeIdentifieds));
    }
}
