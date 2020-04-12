package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.core.Opt2;

import java.util.List;

public interface Subscription2<
        PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
        PodcastType extends Podcast2
        > {
    interface SubscriptionFactory2<
            SubscriptionType extends Subscription2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
            PodcastType extends Podcast2
            > {
        SubscriptionType newSubscription(PodcastIdentifiedType podcastIdentified);
    }

    interface SubscriptionIdentified2<
            SubscriptionIdentifierType extends SubscriptionIdentifier2,
            SubscriptionType extends Subscription2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                    PodcastIdentifierType,
                    PodcastType
                    >,
            PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
            PodcastType extends Podcast2
            > extends Identified2<
            SubscriptionIdentifierType,
            SubscriptionType
            > {
        interface SubscriptionIdentifiedList2<
                SubscriptionIdentifiedType extends SubscriptionIdentified2<
                        SubscriptionIdentifierType,
                        SubscriptionType,
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                SubscriptionIdentifierType extends SubscriptionIdentifier2,
                SubscriptionType extends Subscription2<
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
                PodcastType extends Podcast2
                > extends List<SubscriptionIdentifiedType> {
        }

        interface SubscriptionIdentifiedOpt2<
                SubscriptionIdentifiedType extends SubscriptionIdentified2<
                        SubscriptionIdentifierType,
                        SubscriptionType,
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                SubscriptionIdentifierType extends SubscriptionIdentifier2,
                SubscriptionType extends Subscription2<
                        PodcastIdentifiedType,
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifiedType extends Podcast2.PodcastIdentified2<
                        PodcastIdentifierType,
                        PodcastType
                        >,
                PodcastIdentifierType extends Podcast2.PodcastIdentifier2,
                PodcastType extends Podcast2
                > extends Opt2<SubscriptionIdentifiedType> {
        }
    }

    interface SubscriptionIdentifier2 extends Identifier2 {
        interface SubscriptionIdentifierOpt2<
                SubscriptionIdentifierType extends SubscriptionIdentifier2
                > extends Opt2<SubscriptionIdentifierType> {
        }
    }

    PodcastIdentifiedType getPodcastIdentified();
}
