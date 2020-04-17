package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.dao.Podcast2;

import java.net.URL;
import java.util.Collection;
import java.util.Objects;

public final class PodcastImpl implements Podcast2 {
    public static final class PodcastIdentifiedImpl
            extends IdentifiedImpl<
            PodcastIdentifierImpl,
            PodcastImpl
            >
            implements PodcastIdentified2<
            PodcastIdentifierImpl,
            PodcastImpl
            > {
        public static final class PodcastIdentifiedListImpl
                extends IdentifiedListImpl<
                PodcastIdentifiedImpl,
                PodcastIdentifierImpl,
                PodcastImpl
                > implements PodcastIdentifiedList2<
                PodcastIdentifiedImpl,
                PodcastIdentifierImpl,
                PodcastImpl
                > {
            public PodcastIdentifiedListImpl() {
            }

            public PodcastIdentifiedListImpl(int capacity) {
                super(capacity);
            }

            public PodcastIdentifiedListImpl(Collection<? extends PodcastIdentifiedImpl> identifieds) {
                super(identifieds);
            }
        }

        public static final class PodcastIdentifiedOptImpl
                extends IdentifiedOptImpl<
                PodcastIdentifiedImpl,
                PodcastIdentifierImpl,
                PodcastImpl
                >
                implements PodcastIdentifiedOpt2<
                PodcastIdentifiedImpl,
                PodcastIdentifierImpl,
                PodcastImpl
                > {
            public PodcastIdentifiedOptImpl() {
            }

            public PodcastIdentifiedOptImpl(PodcastIdentifiedImpl value) {
                super(value);
            }
        }

        public static final class PodcastIdentifiedSetImpl
                extends IdentifiedSetImpl<
                PodcastIdentifiedImpl,
                PodcastIdentifierImpl,
                PodcastImpl
                > implements PodcastIdentifiedSet2<
                PodcastIdentifiedImpl,
                PodcastIdentifierImpl,
                PodcastImpl
                > {
            public PodcastIdentifiedSetImpl() {
            }

            public PodcastIdentifiedSetImpl(int initialCapacity) {
                super(initialCapacity);
            }

            public PodcastIdentifiedSetImpl(Collection<? extends PodcastIdentifiedImpl> c) {
                super(c);
            }
        }

        public PodcastIdentifiedImpl(
                PodcastIdentifierImpl identifier,
                PodcastImpl model
        ) {
            super(
                    identifier,
                    model
            );
        }

        // Podcast

        @Nullable
        @Override
        public URL getArtworkURL() {
            return model.artworkURL;
        }

        @Nullable
        @Override
        public String getAuthor() {
            return model.author;
        }

        @Override
        public URL getFeedURL() {
            return model.feedURL;
        }

        @Override
        public String getName() {
            return model.name;
        }
    }

    public static final class PodcastIdentifierImpl
            extends IdentifierImpl
            implements PodcastIdentifier2 {
        public static final class PodcastIdentifierOptImpl
                extends IdentifierImpl.IdentifierOptImpl<
                PodcastIdentifierImpl
                > implements PodcastIdentifierOpt2<
                PodcastIdentifierImpl
                > {
            public static final class PodcastIdentifierOptListImpl
                    extends IdentifierImpl.IdentifierOptImpl.IdentifierOptListImpl<
                    PodcastIdentifierOptImpl,
                    PodcastIdentifierImpl
                    > implements PodcastIdentifierOptList2<
                    PodcastIdentifierOptImpl,
                    PodcastIdentifierImpl
                    > {
                public PodcastIdentifierOptListImpl() {
                }

                public PodcastIdentifierOptListImpl(int initialCapacity) {
                    super(initialCapacity);
                }

                public PodcastIdentifierOptListImpl(Collection<? extends PodcastIdentifierOptImpl> c) {
                    super(c);
                }
            }

            public PodcastIdentifierOptImpl() {
            }

            public PodcastIdentifierOptImpl(PodcastIdentifierImpl identifier) {
                super(identifier);
            }
        }

        public PodcastIdentifierImpl(long id) {
            super(id);
        }
    }

    public static final class PodcastListImpl
            extends ModelListImpl<PodcastImpl>
            implements PodcastList2<
            PodcastImpl
            > {
    }

    @Nullable
    public final URL artworkURL;

    @Nullable
    public final String author;

    public final URL feedURL;

    public final String name;

    public PodcastImpl(
            @Nullable URL artworkURL,
            @Nullable String author,
            URL feedURL,
            String name
    ) {
        this.artworkURL = artworkURL;
        this.author = author;
        this.feedURL = feedURL;
        this.name = name;
    }

    // Object

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastImpl podcast = (PodcastImpl) o;
        return Objects.equals(artworkURL, podcast.artworkURL) &&
                Objects.equals(author, podcast.author) &&
                feedURL.equals(podcast.feedURL) &&
                name.equals(podcast.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                artworkURL,
                author,
                feedURL,
                name
        );
    }

    @Override
    public String toString() {
        return "PodcastImpl{" +
                "artworkURL=" + artworkURL +
                ", author='" + author + '\'' +
                ", feedURL=" + feedURL +
                ", name='" + name + '\'' +
                '}';
    }

    // Podcast

    @Nullable
    @Override
    public URL getArtworkURL() {
        return artworkURL;
    }

    @Nullable
    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public URL getFeedURL() {
        return feedURL;
    }

    @Override
    public String getName() {
        return name;
    }
}
