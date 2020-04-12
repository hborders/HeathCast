package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Opt2;

import java.net.URL;
import java.util.List;
import java.util.Set;

public interface Podcast2 {
    interface PodcastFactory2<PodcastType extends Podcast2> {
        PodcastType newPodcast(
                @Nullable URL artworkURL,
                @Nullable String author,
                URL feedURL,
                String name
        );
    }

    interface PodcastIdentified2<
            PodcastIdentifierType extends PodcastIdentifier2,
            PodcastType extends Podcast2
            > extends Identified2<
            PodcastIdentifierType,
            PodcastType
            > {
        interface PodcastIdentifiedList2<
                PodcastIdentifiedType extends PodcastIdentified2<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends PodcastIdentifier2,
                PodcastType extends Podcast2
                > extends List<PodcastIdentifiedType> {
        }

        interface PodcastIdentifiedOpt2<
                PodcastIdentifiedType extends PodcastIdentified2<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends PodcastIdentifier2,
                PodcastType extends Podcast2
                > extends Opt2<PodcastIdentifiedType> {
        }

        interface PodcastIdentifiedSet2<
                PodcastIdentifiedType extends PodcastIdentified2<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends PodcastIdentifier2,
                PodcastType extends Podcast2
                > extends Set<PodcastIdentifiedType> {
        }
    }

    interface PodcastIdentifier2 extends Identifier2 {
        interface PodcastIdentifierOpt2<
                PodcastIdentifierType extends PodcastIdentifier2
                > extends Opt2<PodcastIdentifierType> {
            interface PodcastIdentifierOptList2<
                    PodcastIdentifierOptType extends Opt2<PodcastIdentifierType>,
                    PodcastIdentifierType extends PodcastIdentifier2
                    > extends List<PodcastIdentifierOptType> {
            }
        }
    }

    @Nullable
    URL getArtworkURL();

    @Nullable
    String getAuthor();

    URL getFeedURL();

    String getName();
}
