package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public final class PodcastSearchIdentifiedOpt extends Opt<PodcastSearchIdentified> {
    public static final PodcastSearchIdentifiedOpt EMPTY = new PodcastSearchIdentifiedOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<PodcastSearchIdentifiedOpt, PodcastSearchIdentified> {
        private Factory() {
        }

        @Override
        public PodcastSearchIdentifiedOpt empty() {
            return EMPTY;
        }

        @Override
        public PodcastSearchIdentifiedOpt of(PodcastSearchIdentified value) {
            return new PodcastSearchIdentifiedOpt(value);
        }
    }

    private PodcastSearchIdentifiedOpt() {
    }

    public PodcastSearchIdentifiedOpt(PodcastSearchIdentified value) {
        super(value);
    }
}
