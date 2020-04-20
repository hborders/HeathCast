package com.github.hborders.heathcast.features.model;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.models.Subscription;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class SubscriptionImpl implements Subscription<
        PodcastImpl.PodcastIdentifiedImpl,
        PodcastImpl.PodcastIdentifierImpl,
        PodcastImpl
        > {
    public static final class SubscriptionIdentifiedImpl
            extends IdentifiedImpl<
            SubscriptionIdentifierImpl,
            SubscriptionImpl
            > implements SubscriptionIdentified<
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

            public SubscriptionIdentifiedListImpl(SubscriptionIdentifiedImpl... subscriptionIdentifieds) {
                this(Arrays.asList(subscriptionIdentifieds));
            }
        }

        public static final class SubscriptionIdentifiedOptImpl
                extends IdentifiedOptImpl<
                SubscriptionIdentifiedImpl,
                SubscriptionIdentifierImpl,
                SubscriptionImpl
                > implements SubscriptionIdentifiedOpt<
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
            implements SubscriptionIdentifier {
        public static final class SubscriptionIdentifierOptImpl
                extends IdentifierOptImpl<SubscriptionIdentifierImpl>
                implements SubscriptionIdentifierOpt<SubscriptionIdentifierImpl> {
            public SubscriptionIdentifierOptImpl() {
            }

            public SubscriptionIdentifierOptImpl(SubscriptionIdentifierImpl identifier) {
                super(identifier);
            }
        }

        public SubscriptionIdentifierImpl(long id) {
            super(id);
        }
    }

    public final PodcastImpl.PodcastIdentifiedImpl podcastIdentified;

    public SubscriptionImpl(PodcastImpl.PodcastIdentifiedImpl podcastIdentified) {
        this.podcastIdentified = podcastIdentified;
    }

    // Object

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionImpl that = (SubscriptionImpl) o;
        return podcastIdentified.equals(that.podcastIdentified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(podcastIdentified);
    }

    @Override
    public String toString() {
        return "SubscriptionImpl{" +
                "podcastIdentified=" + podcastIdentified +
                '}';
    }

    // Podcast

    @Override
    public PodcastImpl.PodcastIdentifiedImpl getPodcastIdentified() {
        return podcastIdentified;
    }
}
