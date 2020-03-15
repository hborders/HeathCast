package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public final class PodcastIdentifierOpt extends Opt<PodcastIdentifier> {
    public static final PodcastIdentifierOpt EMPTY = new PodcastIdentifierOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<PodcastIdentifierOpt, PodcastIdentifier> {
        private Factory() {
        }

        @Override
        public PodcastIdentifierOpt empty() {
            return EMPTY;
        }

        @Override
        public PodcastIdentifierOpt of(PodcastIdentifier value) {
            return new PodcastIdentifierOpt(value);
        }
    }

    private PodcastIdentifierOpt() {
    }

    public PodcastIdentifierOpt(PodcastIdentifier value) {
        super(value);
    }
}
