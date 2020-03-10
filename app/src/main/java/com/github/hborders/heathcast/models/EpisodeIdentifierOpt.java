package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public class EpisodeIdentifierOpt extends Opt<EpisodeIdentifier> {
    public static final EpisodeIdentifierOpt EMPTY = new EpisodeIdentifierOpt();
    public static final PodcastSearchIdentifiedOptFactory FACTORY = new PodcastSearchIdentifiedOptFactory();

    public static final class PodcastSearchIdentifiedOptFactory implements Opt.OptFactory<EpisodeIdentifierOpt, EpisodeIdentifier> {
        private PodcastSearchIdentifiedOptFactory() {
        }

        @Override
        public EpisodeIdentifierOpt empty() {
            return EMPTY;
        }

        @Override
        public EpisodeIdentifierOpt of(EpisodeIdentifier episodeIdentifier) {
            return new EpisodeIdentifierOpt(episodeIdentifier);
        }
    }

    private EpisodeIdentifierOpt() {
    }

    public EpisodeIdentifierOpt(EpisodeIdentifier value) {
        super(value);
    }
}
