package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class PodcastSearchIdentifiedList extends ArrayList<PodcastSearchIdentified> {
    public PodcastSearchIdentifiedList() {
    }

    public PodcastSearchIdentifiedList(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastSearchIdentifiedList(Collection<PodcastSearchIdentified> podcastSearchIdentifieds) {
        super(podcastSearchIdentifieds);
    }

    public PodcastSearchIdentifiedList(PodcastSearchIdentified... podcastSearchIdentifieds) {
        super(Arrays.asList(podcastSearchIdentifieds));
    }
}
