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

    public PodcastSearchIdentifiedList(Collection<PodcastSearchIdentified> items) {
        super(items);
    }

    public PodcastSearchIdentifiedList(PodcastSearchIdentified... items) {
        super(Arrays.asList(items));
    }
}
