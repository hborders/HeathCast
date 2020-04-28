package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.PodcastSearchImpl;
import com.github.hborders.heathcast.features.model.SubscriptionImpl;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class AbstractDatabaseTest<MarkerType> {
    @Nullable
    private Database<
            Object,
            EpisodeImpl,
            EpisodeImpl.EpisodeIdentifiedImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
            EpisodeImpl.EpisodeIdentifierImpl,
            EpisodeImpl.EpisodeListImpl,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastSearchImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl,
            SubscriptionImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl
            > database;

    protected final Database<
            Object,
            EpisodeImpl,
            EpisodeImpl.EpisodeIdentifiedImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
            EpisodeImpl.EpisodeIdentifierImpl,
            EpisodeImpl.EpisodeListImpl,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastSearchImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl,
            SubscriptionImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl
            > getDatabase() {
        @Nullable final Database<
                Object,
                EpisodeImpl,
                EpisodeImpl.EpisodeIdentifiedImpl,
                EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
                EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
                EpisodeImpl.EpisodeIdentifierImpl,
                EpisodeImpl.EpisodeListImpl,
                PodcastImpl,
                PodcastImpl.PodcastIdentifiedImpl,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                PodcastImpl.PodcastIdentifierImpl,
                PodcastSearchImpl,
                PodcastSearchImpl.PodcastSearchIdentifiedImpl,
                PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
                PodcastSearchImpl.PodcastSearchIdentifierImpl,
                SubscriptionImpl,
                SubscriptionImpl.SubscriptionIdentifiedImpl,
                SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
                SubscriptionImpl.SubscriptionIdentifierImpl
                > initialDatabase = this.database;
        if (initialDatabase == null) {
            final Database<
                    Object,
                    EpisodeImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
                    EpisodeImpl.EpisodeIdentifierImpl,
                    EpisodeImpl.EpisodeListImpl,
                    PodcastImpl,
                    PodcastImpl.PodcastIdentifiedImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                    PodcastImpl.PodcastIdentifierImpl,
                    PodcastSearchImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl,
                    SubscriptionImpl,
                    SubscriptionImpl.SubscriptionIdentifiedImpl,
                    SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
                    SubscriptionImpl.SubscriptionIdentifierImpl
                    > newDatabase = new Database<>(
                    ApplicationProvider.getApplicationContext(),
                    null, // in-memory
                    Schedulers.trampoline(),
                    EpisodeImpl::new,
                    EpisodeImpl.EpisodeIdentifierImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl::new,
                    PodcastImpl::new,
                    PodcastImpl.PodcastIdentifierImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl::new,
                    PodcastSearchImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl::new,
                    SubscriptionImpl::new,
                    SubscriptionImpl.SubscriptionIdentifierImpl::new,
                    SubscriptionImpl.SubscriptionIdentifiedImpl::new,
                    SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl::new
            );
            this.database = newDatabase;
            return newDatabase;
        } else {
            return initialDatabase;
        }
    }
}
