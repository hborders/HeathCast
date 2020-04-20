package com.github.hborders.heathcast.models;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Opt;

import java.net.URL;
import java.util.List;
import java.util.Set;

public interface Podcast {
    interface PodcastFactory2<PodcastType extends Podcast> {
        PodcastType newPodcast(
                @Nullable URL artworkURL,
                @Nullable String author,
                URL feedURL,
                String name
        );
    }

    interface PodcastIdentified<
            PodcastIdentifierType extends PodcastIdentifier,
            PodcastType extends Podcast
            > extends Identified<
            PodcastIdentifierType,
            PodcastType
            >, Podcast {
        interface PodcastIdentifiedList2<
                PodcastIdentifiedType extends PodcastIdentified<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends PodcastIdentifier,
                PodcastType extends Podcast
                > extends List<PodcastIdentifiedType> {
        }

        interface PodcastIdentifiedOpt<
                PodcastIdentifiedType extends PodcastIdentified<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends PodcastIdentifier,
                PodcastType extends Podcast
                > extends Opt<PodcastIdentifiedType> {
        }

        interface PodcastIdentifiedSet2<
                PodcastIdentifiedType extends PodcastIdentified<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends PodcastIdentifier,
                PodcastType extends Podcast
                > extends Set<PodcastIdentifiedType> {
        }
    }

    interface PodcastIdentifier extends Identifier {
        interface PodcastIdentifierOpt<
                PodcastIdentifierType extends PodcastIdentifier
                > extends Opt<PodcastIdentifierType> {
            interface PodcastIdentifierOptList2<
                    PodcastIdentifierOptType extends Opt<PodcastIdentifierType>,
                    PodcastIdentifierType extends PodcastIdentifier
                    > extends List<PodcastIdentifierOptType> {
            }
        }
    }

    interface PodcastList2<PodcastType extends Podcast> extends List<PodcastType> {
    }

    @Nullable
    URL getArtworkURL();

    @Nullable
    String getAuthor();

    URL getFeedURL();

    String getName();
}
