package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.CollectionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class PodcastIdentifiedList extends ArrayList<PodcastIdentified> {
    public interface CapacityFactory extends CollectionFactory.Capacity<PodcastIdentifiedList, PodcastIdentified> {
    }

    public PodcastIdentifiedList() {
    }

    public PodcastIdentifiedList(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastIdentifiedList(Collection<PodcastIdentified> items) {
        super(items);
    }

    public PodcastIdentifiedList(PodcastIdentified... items) {
        super(Arrays.asList(items));
    }
}
