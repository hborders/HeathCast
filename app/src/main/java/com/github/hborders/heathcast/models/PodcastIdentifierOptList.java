package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class PodcastIdentifierOptList extends ArrayList<PodcastIdentifierOpt> {
    public PodcastIdentifierOptList() {
    }

    public PodcastIdentifierOptList(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastIdentifierOptList(Collection<PodcastIdentifierOpt> items) {
        super(items);
    }

    public PodcastIdentifierOptList(PodcastIdentifierOpt... items) {
        super(Arrays.asList(items));
    }
}
