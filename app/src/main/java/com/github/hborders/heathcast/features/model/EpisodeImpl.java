package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.models.Episode;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public final class EpisodeImpl implements Episode {
    public static final class EpisodeIdentifiedImpl
            extends IdentifiedImpl<
            EpisodeIdentifierImpl,
            EpisodeImpl
            >
            implements EpisodeIdentified<
            EpisodeIdentifierImpl,
            EpisodeImpl
            > {
        public static final class EpisodeIdentifiedListImpl
                extends IdentifiedListImpl<
                EpisodeIdentifiedImpl,
                EpisodeIdentifierImpl,
                EpisodeImpl
                >
                implements EpisodeIdentifiedList2<
                EpisodeIdentifiedImpl,
                EpisodeIdentifierImpl,
                EpisodeImpl
                > {
            public static final class EpisodeIdentifiedListVersionedImpl
                    extends VersionedImpl<
                    EpisodeIdentifiedListImpl
                    > implements EpisodeIdentifiedListVersioned<
                    EpisodeIdentifiedListImpl,
                    EpisodeIdentifiedImpl,
                    EpisodeIdentifierImpl,
                    EpisodeImpl
                    > {
                public EpisodeIdentifiedListVersionedImpl(
                        EpisodeIdentifiedListImpl value,
                        long version
                ) {
                    super(
                            value,
                            version
                    );
                }
            }

            public EpisodeIdentifiedListImpl() {
            }

            public EpisodeIdentifiedListImpl(int capacity) {
                super(capacity);
            }

            public EpisodeIdentifiedListImpl(Collection<? extends EpisodeIdentifiedImpl> identifieds) {
                super(identifieds);
            }

            public EpisodeIdentifiedListImpl(EpisodeIdentifiedImpl... episodeIdentifieds) {
                this(Arrays.asList(episodeIdentifieds));
            }
        }

        public static final class EpisodeIdentifiedOptImpl
                extends IdentifiedOptImpl<
                EpisodeIdentifiedImpl,
                EpisodeIdentifierImpl,
                EpisodeImpl
                > implements EpisodeIdentifiedOpt<
                EpisodeIdentifiedImpl,
                EpisodeIdentifierImpl,
                EpisodeImpl
                > {
            public static final class EpisodeIdentifiedOptListImpl
                    extends IdentifiedOptListImpl<
                    EpisodeIdentifiedOptImpl,
                    EpisodeIdentifiedImpl,
                    EpisodeIdentifierImpl,
                    EpisodeImpl
                    > implements EpisodeIdentifiedOptList2<
                    EpisodeIdentifiedOptImpl,
                    EpisodeIdentifiedImpl,
                    EpisodeIdentifierImpl,
                    EpisodeImpl
                    > {
                public EpisodeIdentifiedOptListImpl() {
                }

                public EpisodeIdentifiedOptListImpl(int initialCapacity) {
                    super(initialCapacity);
                }

                public EpisodeIdentifiedOptListImpl(Collection<? extends EpisodeIdentifiedOptImpl> c) {
                    super(c);
                }

                public EpisodeIdentifiedOptListImpl(EpisodeIdentifiedOptImpl... episodeIdentifiedOpts) {
                    this(Arrays.asList(episodeIdentifiedOpts));
                }
            }

            public EpisodeIdentifiedOptImpl() {
            }

            public EpisodeIdentifiedOptImpl(EpisodeIdentifiedImpl value) {
                super(value);
            }
        }

        public static final class EpisodeIdentifiedSetImpl
                extends IdentifiedSetImpl<
                EpisodeIdentifiedImpl,
                EpisodeIdentifierImpl,
                EpisodeImpl
                > implements EpisodeIdentifiedSet2<
                EpisodeIdentifiedImpl,
                EpisodeIdentifierImpl,
                EpisodeImpl
                > {
            public EpisodeIdentifiedSetImpl() {
            }

            public EpisodeIdentifiedSetImpl(int initialCapacity) {
                super(initialCapacity);
            }

            public EpisodeIdentifiedSetImpl(Collection<? extends EpisodeIdentifiedImpl> c) {
                super(c);
            }

            public EpisodeIdentifiedSetImpl(EpisodeIdentifiedImpl... episodeIdentifieds) {
                this(Arrays.asList(episodeIdentifieds));
            }
        }

        public EpisodeIdentifiedImpl(
                EpisodeIdentifierImpl identifier,
                EpisodeImpl model
        ) {
            super(
                    identifier,
                    model
            );
        }

        @Nullable
        @Override
        public URL getArtworkURL() {
            return model.artworkURL;
        }

        @Nullable
        @Override
        public Duration getDuration() {
            return model.duration;
        }

        @Nullable
        @Override
        public Date getPublishDate() {
            return model.publishDate;
        }

        @Nullable
        @Override
        public String getSummary() {
            return model.summary;
        }

        @Override
        public String getTitle() {
            return model.title;
        }

        @Override
        public URL getURL() {
            return model.url;
        }
    }

    public static final class EpisodeIdentifierImpl
            extends IdentifierImpl
            implements EpisodeIdentifier {
        public static final class EpisodeIdentifierOptImpl
                extends IdentifierOptImpl<EpisodeIdentifierImpl>
                implements EpisodeIdentifierOpt<EpisodeIdentifierImpl> {
            public static final class EpisodeIdentifierOptListImpl
                    extends IdentifierOptImpl.IdentifierOptListImpl<
                    EpisodeIdentifierOptImpl,
                    EpisodeIdentifierImpl
                    > implements EpisodeIdentifierOpt.EpisodeIdentifierOptList2<
                    EpisodeIdentifierOptImpl,
                    EpisodeIdentifierImpl
                    > {
                public EpisodeIdentifierOptListImpl() {
                }

                public EpisodeIdentifierOptListImpl(int initialCapacity) {
                    super(initialCapacity);
                }

                public EpisodeIdentifierOptListImpl(Collection<? extends EpisodeIdentifierOptImpl> c) {
                    super(c);
                }
            }

            public EpisodeIdentifierOptImpl() {
            }

            public EpisodeIdentifierOptImpl(EpisodeIdentifierImpl value) {
                super(value);
            }
        }

        public EpisodeIdentifierImpl(long id) {
            super(id);
        }
    }

    public static final class EpisodeListImpl
            extends ModelListImpl<EpisodeImpl>
            implements EpisodeList2<EpisodeImpl> {
        public EpisodeListImpl() {
        }

        public EpisodeListImpl(int initialCapacity) {
            super(initialCapacity);
        }

        public EpisodeListImpl(Collection<? extends EpisodeImpl> c) {
            super(c);
        }

        public EpisodeListImpl(EpisodeImpl... episodes) {
            this(Arrays.asList(episodes));
        }
    }


    @Nullable
    public final URL artworkURL;

    @Nullable
    public final Duration duration;

    @Nullable
    public final Date publishDate;

    @Nullable
    public final String summary;

    public final String title;

    public final URL url;

    public EpisodeImpl(
            @Nullable URL artworkURL,
            @Nullable Duration duration,
            @Nullable Date publishDate,
            @Nullable String summary,
            String title,
            URL url
    ) {
        this.artworkURL = artworkURL;
        this.duration = duration;
        this.publishDate = publishDate;
        this.summary = summary;
        this.title = title;
        this.url = url;
    }

    // Object

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodeImpl episode = (EpisodeImpl) o;
        return Objects.equals(artworkURL, episode.artworkURL) &&
                Objects.equals(duration, episode.duration) &&
                Objects.equals(publishDate, episode.publishDate) &&
                Objects.equals(summary, episode.summary) &&
                title.equals(episode.title) &&
                url.equals(episode.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                artworkURL,
                duration,
                publishDate,
                summary,
                title,
                url
        );
    }

    @Override
    public String toString() {
        return "EpisodeImpl{" +
                "artworkURL=" + artworkURL +
                ", duration=" + duration +
                ", publishDate=" + publishDate +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", url=" + url +
                '}';
    }

    // Episode

    @Nullable
    @Override
    public URL getArtworkURL() {
        return artworkURL;
    }

    @Nullable
    @Override
    public Duration getDuration() {
        return duration;
    }

    @Nullable
    @Override
    public Date getPublishDate() {
        return publishDate;
    }

    @Nullable
    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public URL getURL() {
        return url;
    }
}
