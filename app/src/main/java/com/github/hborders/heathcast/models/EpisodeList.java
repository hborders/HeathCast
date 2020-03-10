package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class EpisodeList extends ArrayList<Episode> {
    public EpisodeList() {
    }

    public EpisodeList(int initialCapacity) {
        super(initialCapacity);
    }

    public EpisodeList(Collection<Episode> episodes) {
        super(episodes);
    }

    public EpisodeList(Episode... episodes) {
        super(Arrays.asList(episodes));
    }
}
