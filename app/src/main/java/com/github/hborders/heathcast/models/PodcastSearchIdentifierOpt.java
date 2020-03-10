package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public final class PodcastSearchIdentifierOpt extends Opt<PodcastSearchIdentifier> {
    public static final PodcastSearchIdentifierOpt EMPTY = new PodcastSearchIdentifierOpt();
    public static final PodcastSearchIdentifierOptFactory FACTORY = new PodcastSearchIdentifierOptFactory();

    public static final class PodcastSearchIdentifierOptFactory implements OptFactory<PodcastSearchIdentifierOpt, PodcastSearchIdentifier> {
        private PodcastSearchIdentifierOptFactory() {
        }

        @Override
        public PodcastSearchIdentifierOpt empty() {
            return EMPTY;
        }

        @Override
        public PodcastSearchIdentifierOpt of(PodcastSearchIdentifier podcastSearchIdentifier) {
            return new PodcastSearchIdentifierOpt(podcastSearchIdentifier);
        }
    }

    private PodcastSearchIdentifierOpt() {
    }

    public PodcastSearchIdentifierOpt(PodcastSearchIdentifier value) {
        super(value);
    }
}
