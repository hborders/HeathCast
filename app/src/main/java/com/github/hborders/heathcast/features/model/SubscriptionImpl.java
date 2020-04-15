package com.github.hborders.heathcast.features.model;

import com.github.hborders.heathcast.dao.Subscription2;

import java.util.Collection;

public final class SubscriptionImpl implements Subscription2<
        PodcastImpl.PodcastIdentifiedImpl,
        PodcastImpl.PodcastIdentifierImpl,
        PodcastImpl
        > {
    public static final class SubscriptionIdentifiedImpl
            extends IdentifiedImpl<
            SubscriptionIdentifierImpl,
            SubscriptionImpl
            > implements SubscriptionIdentified2<
            SubscriptionIdentifierImpl,
            SubscriptionImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl
            > {
        public static final class SubscriptionIdentifiedListImpl
                extends IdentifiedListImpl<
                SubscriptionIdentifiedImpl,
                SubscriptionIdentifierImpl,
                SubscriptionImpl
                > implements SubscriptionIdentifiedList2<
                SubscriptionIdentifiedImpl,
                SubscriptionIdentifierImpl,
                SubscriptionImpl,
                PodcastImpl.PodcastIdentifiedImpl,
                PodcastImpl.PodcastIdentifierImpl,
                PodcastImpl
                > {
            public SubscriptionIdentifiedListImpl() {
            }

            public SubscriptionIdentifiedListImpl(int capacity) {
                super(capacity);
            }

            public SubscriptionIdentifiedListImpl(Collection<? extends SubscriptionIdentifiedImpl> identifieds) {
                super(identifieds);
            }
        }

        public static final class SubscriptionIdentifiedOptImpl
                extends IdentifiedOptImpl<
                SubscriptionIdentifiedImpl,
                SubscriptionIdentifierImpl,
                SubscriptionImpl
                > implements SubscriptionIdentifiedOpt2<
                SubscriptionIdentifiedImpl,
                SubscriptionIdentifierImpl,
                SubscriptionImpl,
                PodcastImpl.PodcastIdentifiedImpl,
                PodcastImpl.PodcastIdentifierImpl,
                PodcastImpl
                > {
            public SubscriptionIdentifiedOptImpl() {
            }

            public SubscriptionIdentifiedOptImpl(SubscriptionIdentifiedImpl value) {
                super(value);
            }
        }

        public SubscriptionIdentifiedImpl(
                SubscriptionIdentifierImpl identifier,
                SubscriptionImpl model
        ) {
            super(
                    identifier,
                    model
            );
        }

        @Override
        public PodcastImpl.PodcastIdentifiedImpl getPodcastIdentified() {
            return model.podcastIdentified;
        }
    }

    public static final class SubscriptionIdentifierImpl
            extends IdentifierImpl
            implements SubscriptionIdentifier2 {
        public SubscriptionIdentifierImpl(long id) {
            super(id);
        }
    }

    public final PodcastImpl.PodcastIdentifiedImpl podcastIdentified;

    public SubscriptionImpl(PodcastImpl.PodcastIdentifiedImpl podcastIdentified) {
        this.podcastIdentified = podcastIdentified;
    }

    @Override
    public PodcastImpl.PodcastIdentifiedImpl getPodcastIdentified() {
        return podcastIdentified;
    }
}
