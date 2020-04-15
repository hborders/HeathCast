package com.github.hborders.heathcast.features.model;

import com.github.hborders.heathcast.dao.PodcastSearch2;

public final class PodcastSearchImpl implements PodcastSearch2 {
    public static final class PodcastSearchIdentifiedImpl
            extends IdentifiedImpl<
            PodcastSearchIdentifierImpl,
            PodcastSearchImpl
            >
            implements PodcastSearchIdentified2<
            PodcastSearchIdentifierImpl,
            PodcastSearchImpl
            > {
        public static final class PodcastSearchIdentifiedListImpl
                extends IdentifiedListImpl<
                PodcastSearchIdentifiedImpl,
                PodcastSearchIdentifierImpl,
                PodcastSearchImpl
                >
                implements PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedImpl,
                PodcastSearchIdentifierImpl,
                PodcastSearchImpl
                > {
        }

        public static final class PodcastSearchIdentifiedOptImpl
                extends IdentifiedOptImpl<
                PodcastSearchIdentifiedImpl,
                PodcastSearchIdentifierImpl,
                PodcastSearchImpl
                >
                implements PodcastSearchIdentifiedOpt2<
                PodcastSearchIdentifiedImpl,
                PodcastSearchIdentifierImpl,
                PodcastSearchImpl
                > {
            public PodcastSearchIdentifiedOptImpl() {
            }

            public PodcastSearchIdentifiedOptImpl(PodcastSearchIdentifiedImpl value) {
                super(value);
            }
        }

        public PodcastSearchIdentifiedImpl(
                PodcastSearchIdentifierImpl identifier,
                PodcastSearchImpl model
        ) {
            super(
                    identifier,
                    model
            );
        }

        @Override
        public String getSearch() {
            return model.search;
        }
    }

    public static final class PodcastSearchIdentifierImpl
            extends IdentifierImpl
            implements PodcastSearchIdentifier2 {
        public PodcastSearchIdentifierImpl(long id) {
            super(id);
        }
    }

    public final String search;

    public PodcastSearchImpl(String search) {
        this.search = search;
    }

    @Override
    public String getSearch() {
        return search;
    }
}
