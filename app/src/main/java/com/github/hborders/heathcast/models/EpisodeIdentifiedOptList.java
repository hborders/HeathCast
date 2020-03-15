package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EpisodeIdentifiedOptList extends ArrayList<EpisodeIdentifiedOpt> {
    public EpisodeIdentifiedOptList() {
    }

    public EpisodeIdentifiedOptList(int capacity) {
        super(capacity);
    }

    public EpisodeIdentifiedOptList(Collection<EpisodeIdentifiedOpt> items) {
        super(items);
    }

    public EpisodeIdentifiedOptList(EpisodeIdentifiedOpt... items) {
        super(Arrays.asList(items));
    }
}
