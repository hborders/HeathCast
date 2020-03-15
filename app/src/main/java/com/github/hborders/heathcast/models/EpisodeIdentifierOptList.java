package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class EpisodeIdentifierOptList extends ArrayList<EpisodeIdentifierOpt> {
    public EpisodeIdentifierOptList() {
    }

    public EpisodeIdentifierOptList(int capacity) {
        super(capacity);
    }

    public EpisodeIdentifierOptList(Collection<EpisodeIdentifierOpt> items) {
        super(items);
    }

    public EpisodeIdentifierOptList(EpisodeIdentifierOpt... items) {
        super(Arrays.asList(items));
    }
}
