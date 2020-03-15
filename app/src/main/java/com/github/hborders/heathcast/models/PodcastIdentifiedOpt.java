package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public final class PodcastIdentifiedOpt extends Opt<PodcastIdentified> {
    public static final PodcastIdentifiedOpt EMPTY = new PodcastIdentifiedOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<PodcastIdentifiedOpt, PodcastIdentified> {
        private Factory() {
        }

        @Override
        public PodcastIdentifiedOpt empty() {
            return EMPTY;
        }

        @Override
        public PodcastIdentifiedOpt of(PodcastIdentified value) {
            return new PodcastIdentifiedOpt(value);
        }
    }

    private PodcastIdentifiedOpt() {
    }

    public PodcastIdentifiedOpt(PodcastIdentified value) {
        super(value);
    }
}
