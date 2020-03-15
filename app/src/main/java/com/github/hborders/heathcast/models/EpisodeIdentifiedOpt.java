package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public class EpisodeIdentifiedOpt extends Opt<EpisodeIdentified> {
    public static final EpisodeIdentifiedOpt EMPTY = new EpisodeIdentifiedOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<EpisodeIdentifiedOpt, EpisodeIdentified> {
        private Factory() {
        }

        @Override
        public EpisodeIdentifiedOpt empty() {
            return EMPTY;
        }

        @Override
        public EpisodeIdentifiedOpt of(EpisodeIdentified value) {
            return new EpisodeIdentifiedOpt(value);
        }
    }

    private EpisodeIdentifiedOpt() {
    }

    public EpisodeIdentifiedOpt(EpisodeIdentified value) {
        super(value);
    }
}
