package com.github.hborders.heathcast.features.model;

import com.github.hborders.heathcast.dao.PodcastSearch2;

import java.util.Arrays;
import java.util.Collection;

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
            public PodcastSearchIdentifiedListImpl() {
            }

            public PodcastSearchIdentifiedListImpl(int capacity) {
                super(capacity);
            }

            public PodcastSearchIdentifiedListImpl(Collection<? extends PodcastSearchIdentifiedImpl> identifieds) {
                super(identifieds);
            }

            public PodcastSearchIdentifiedListImpl(PodcastSearchIdentifiedImpl... podcastSearchIdentifieds) {
                this(Arrays.asList(podcastSearchIdentifieds));
            }
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
        public static final class PodcastSearchIdentifierOptImpl
                extends IdentifierOptImpl<PodcastSearchIdentifierImpl>
                implements PodcastSearchIdentifier2.PodcastSearchIdentifierOpt2<
                PodcastSearchIdentifierImpl
                > {
            public PodcastSearchIdentifierOptImpl() {
            }

            public PodcastSearchIdentifierOptImpl(PodcastSearchIdentifierImpl identifier) {
                super(identifier);
            }
        }

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
