package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public final class EpisodeIdentifierOpt extends Opt<EpisodeIdentifier> {
    public static final EpisodeIdentifierOpt EMPTY = new EpisodeIdentifierOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<EpisodeIdentifierOpt, EpisodeIdentifier> {
        private Factory() {
        }

        @Override
        public EpisodeIdentifierOpt empty() {
            return EMPTY;
        }

        @Override
        public EpisodeIdentifierOpt of(EpisodeIdentifier value) {
            return new EpisodeIdentifierOpt(value);
        }
    }

    private EpisodeIdentifierOpt() {
    }

    public EpisodeIdentifierOpt(EpisodeIdentifier value) {
        super(value);
    }
}
