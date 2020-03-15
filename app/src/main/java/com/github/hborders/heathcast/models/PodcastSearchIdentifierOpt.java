package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public final class PodcastSearchIdentifierOpt extends Opt<PodcastSearchIdentifier> {
    public static final PodcastSearchIdentifierOpt EMPTY = new PodcastSearchIdentifierOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<PodcastSearchIdentifierOpt, PodcastSearchIdentifier> {
        private Factory() {
        }

        @Override
        public PodcastSearchIdentifierOpt empty() {
            return EMPTY;
        }

        @Override
        public PodcastSearchIdentifierOpt of(PodcastSearchIdentifier value) {
            return new PodcastSearchIdentifierOpt(value);
        }
    }

    private PodcastSearchIdentifierOpt() {
    }

    public PodcastSearchIdentifierOpt(PodcastSearchIdentifier value) {
        super(value);
    }
}
