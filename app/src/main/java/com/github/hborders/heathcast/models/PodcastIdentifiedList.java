package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Collection;

public final class PodcastIdentifiedList extends ArrayList<PodcastIdentified> {

    public PodcastIdentifiedList() {
    }

    public PodcastIdentifiedList(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastIdentifiedList(Collection<PodcastIdentified> podcastIdentifieds) {
        super(podcastIdentifieds);
    }
}
