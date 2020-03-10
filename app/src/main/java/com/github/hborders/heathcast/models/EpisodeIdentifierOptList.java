package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EpisodeIdentifierOptList extends ArrayList<EpisodeIdentifierOpt> {
    public EpisodeIdentifierOptList() {
    }

    public EpisodeIdentifierOptList(int capacity) {
        super(capacity);
    }

    public EpisodeIdentifierOptList(Collection<EpisodeIdentifierOpt> episodeIdentifierOpts) {
        super(episodeIdentifierOpts);
    }

    public EpisodeIdentifierOptList(EpisodeIdentifierOpt... episodeIdentifierOpts) {
        super(Arrays.asList(episodeIdentifierOpts));
    }
}
