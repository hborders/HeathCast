package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.core.Opt2;

import java.util.List;

public interface PodcastSearch2 {
    interface PodcastSearchFactory2<PodcastSearchType extends PodcastSearch2> {
        PodcastSearchType newPodcastSearch(String search);
    }

    interface PodcastSearchIdentified2<
            PodcastSearchIdentifierType extends PodcastSearchIdentifier2,
            PodcastSearchType extends PodcastSearch2
            > extends Identified2<
            PodcastSearchIdentifierType,
            PodcastSearchType
            >, PodcastSearch2 {
        interface PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType extends PodcastSearchIdentified2<
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
                PodcastSearchIdentifierType extends PodcastSearchIdentifier2,
                PodcastSearchType extends PodcastSearch2
                > extends List<PodcastSearchIdentifiedType> {
        }

        interface PodcastSearchIdentifiedOpt2<
                PodcastSearchIdentifiedType extends PodcastSearchIdentified2<
                        PodcastSearchIdentifierType,
                        PodcastSearchType
                        >,
                PodcastSearchIdentifierType extends PodcastSearchIdentifier2,
                PodcastSearchType extends PodcastSearch2
                > extends Opt2<PodcastSearchIdentifiedType> {
        }
    }

    interface PodcastSearchIdentifier2 extends Identifier2 {
        interface PodcastSearchIdentifierOpt2<
                PodcastSearchIdentifierType extends PodcastSearchIdentifier2
                > extends Opt2<PodcastSearchIdentifierType> {
        }
    }

    String getSearch();
}
