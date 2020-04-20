package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.models.PodcastSearch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class PodcastSearchImpl implements PodcastSearch {
    public static final class PodcastSearchIdentifiedImpl
            extends IdentifiedImpl<
            PodcastSearchIdentifierImpl,
            PodcastSearchImpl
            >
            implements PodcastSearchIdentified<
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
                implements PodcastSearchIdentifiedOpt<
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
            implements PodcastSearchIdentifier {
        public static final class PodcastSearchIdentifierOptImpl
                extends IdentifierOptImpl<PodcastSearchIdentifierImpl>
                implements PodcastSearchIdentifierOpt<
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

    // Object

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastSearchImpl that = (PodcastSearchImpl) o;
        return search.equals(that.search);
    }

    @Override
    public int hashCode() {
        return Objects.hash(search);
    }

    @Override
    public String toString() {
        return "PodcastSearchImpl{" +
                "search='" + search + '\'' +
                '}';
    }

    // PodcastSearch

    @Override
    public String getSearch() {
        return search;
    }
}
