package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

import java.util.List;

public interface PodcastSearch {
    interface PodcastSearchFactory2<PodcastSearchType extends PodcastSearch> {
        PodcastSearchType newPodcastSearch(String search);
    }

    interface PodcastSearchIdentified<
            PodcastSearchIdentifierType extends PodcastSearchIdentifier,
            PodcastSearchType extends PodcastSearch
            > extends Identified<
            PodcastSearchIdentifierType,
            PodcastSearchType
            >, PodcastSearch {
        interface PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType extends PodcastSearchIdentified<
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
                PodcastSearchIdentifierType extends PodcastSearchIdentifier,
                PodcastSearchType extends PodcastSearch
                > extends List<PodcastSearchIdentifiedType> {
        }

        interface PodcastSearchIdentifiedOpt<
                PodcastSearchIdentifiedType extends PodcastSearchIdentified<
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
                PodcastSearchIdentifierType extends PodcastSearchIdentifier,
                PodcastSearchType extends PodcastSearch
                > extends Opt<PodcastSearchIdentifiedType> {
        }
    }

    interface PodcastSearchIdentifier extends Identifier {
        interface PodcastSearchIdentifierOpt<
                PodcastSearchIdentifierType extends PodcastSearchIdentifier
                > extends Opt<PodcastSearchIdentifierType> {
        }
    }

    String getSearch();
}
