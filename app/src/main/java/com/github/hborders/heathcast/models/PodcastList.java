package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PodcastList extends ArrayList<Podcast> {
    public PodcastList() {
    }

    public PodcastList(int initialCapacity) {
        super(initialCapacity);
    }

    public PodcastList(Collection<Podcast> podcasts) {
        super(podcasts);
    }

    public PodcastList(Podcast... podcasts) {
        super(Arrays.asList(podcasts));
    }
}
