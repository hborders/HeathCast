package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public class PodcastSearchIdentifiedOpt extends Opt<PodcastSearchIdentified> {
    public static final PodcastSearchIdentifiedOpt EMPTY = new PodcastSearchIdentifiedOpt();
    public static final PodcastSearchIdentifiedOptFactory FACTORY = new PodcastSearchIdentifiedOptFactory();

    public static final class PodcastSearchIdentifiedOptFactory implements Opt.OptFactory<PodcastSearchIdentifiedOpt, PodcastSearchIdentified> {
        private PodcastSearchIdentifiedOptFactory() {
        }

        @Override
        public PodcastSearchIdentifiedOpt empty() {
            return EMPTY;
        }

        @Override
        public PodcastSearchIdentifiedOpt of(PodcastSearchIdentified podcastSearchIdentified) {
            return new PodcastSearchIdentifiedOpt(podcastSearchIdentified);
        }
    }

    private PodcastSearchIdentifiedOpt() {
    }

    public PodcastSearchIdentifiedOpt(PodcastSearchIdentified value) {
        super(value);
    }
}
