package com.github.hborders.heathcast.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public final class PodcastIdentifiedSet extends HashSet<PodcastIdentified> {
    public PodcastIdentifiedSet() {
    }

    public PodcastIdentifiedSet(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastIdentifiedSet(Collection<PodcastIdentified> items) {
        super(items);
    }

    public PodcastIdentifiedSet(PodcastIdentified... items) {
        super(Arrays.asList(items));
    }
}
