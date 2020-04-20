package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

import java.util.List;

public interface Subscription<
        PodcastIdentifiedType extends Podcast.PodcastIdentified<
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast.PodcastIdentifier,
        PodcastType extends Podcast
        > {
    interface SubscriptionFactory2<
            SubscriptionType extends Subscription<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifiedType extends Podcast.PodcastIdentified<
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifierType extends Podcast.PodcastIdentifier,
            PodcastType extends Podcast
            > {
        SubscriptionType newSubscription(PodcastIdentifiedType podcastIdentified);
    }

    interface SubscriptionIdentified<
            SubscriptionIdentifierType extends SubscriptionIdentifier,
            SubscriptionType extends Subscription<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifiedType extends Podcast.PodcastIdentified<
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifierType extends Podcast.PodcastIdentifier,
            PodcastType extends Podcast
            > extends Identified<
            SubscriptionIdentifierType,
            SubscriptionType
            >, Subscription<
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > {
        interface SubscriptionIdentifiedList2<
                SubscriptionIdentifiedType extends SubscriptionIdentified<
                        SubscriptionIdentifierType,
                        SubscriptionType,
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                SubscriptionIdentifierType extends SubscriptionIdentifier,
                SubscriptionType extends Subscription<
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifiedType extends Podcast.PodcastIdentified<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends Podcast.PodcastIdentifier,
                PodcastType extends Podcast
                > extends List<SubscriptionIdentifiedType> {
        }

        interface SubscriptionIdentifiedOpt<
                SubscriptionIdentifiedType extends SubscriptionIdentified<
                        SubscriptionIdentifierType,
                        SubscriptionType,
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                SubscriptionIdentifierType extends SubscriptionIdentifier,
                SubscriptionType extends Subscription<
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifiedType extends Podcast.PodcastIdentified<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends Podcast.PodcastIdentifier,
                PodcastType extends Podcast
                > extends Opt<SubscriptionIdentifiedType> {
        }
    }

    interface SubscriptionIdentifier extends Identifier {
        interface SubscriptionIdentifierOpt<
                SubscriptionIdentifierType extends SubscriptionIdentifier
                > extends Opt<SubscriptionIdentifierType> {
        }
    }

    PodcastIdentifiedType getPodcastIdentified();
}
