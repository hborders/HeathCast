package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Collection;

public final class PodcastIdentifiedList extends ArrayList<Identified<Podcast>> {
    public PodcastIdentifiedList(Collection<Identified<Podcast>> podcastIdentifieds) {
        super(podcastIdentifieds);
    }
}
