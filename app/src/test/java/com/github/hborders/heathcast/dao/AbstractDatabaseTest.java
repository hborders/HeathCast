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
            MarkerType,
            EpisodeImpl,
            EpisodeImpl.EpisodeIdentifiedImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
            EpisodeImpl.EpisodeIdentifierImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
            EpisodeImpl.EpisodeListImpl,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
            PodcastSearchImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl,
            SubscriptionImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl
            > database;

    protected final Database<
            MarkerType,
            EpisodeImpl,
            EpisodeImpl.EpisodeIdentifiedImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
            EpisodeImpl.EpisodeIdentifierImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
            EpisodeImpl.EpisodeListImpl,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
            PodcastSearchImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl,
            SubscriptionImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl
            > getDatabase() {
        @Nullable final Database<
                MarkerType,
                EpisodeImpl,
                EpisodeImpl.EpisodeIdentifiedImpl,
                EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
                EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
                EpisodeImpl.EpisodeIdentifierImpl,
                EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
                EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
                EpisodeImpl.EpisodeListImpl,
                PodcastImpl,
                PodcastImpl.PodcastIdentifiedImpl,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                PodcastImpl.PodcastIdentifierImpl,
                PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
                PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
                PodcastSearchImpl,
                PodcastSearchImpl.PodcastSearchIdentifiedImpl,
                PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
                PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl,
                PodcastSearchImpl.PodcastSearchIdentifierImpl,
                PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl,
                SubscriptionImpl,
                SubscriptionImpl.SubscriptionIdentifiedImpl,
                SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
                SubscriptionImpl.SubscriptionIdentifierImpl
                > initialDatabase = this.database;
        if (initialDatabase == null) {
            final Database<
                    MarkerType,
                    EpisodeImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
                    EpisodeImpl.EpisodeIdentifierImpl,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
                    EpisodeImpl.EpisodeListImpl,
                    PodcastImpl,
                    PodcastImpl.PodcastIdentifiedImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                    PodcastImpl.PodcastIdentifierImpl,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
                    PodcastSearchImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl,
                    SubscriptionImpl,
                    SubscriptionImpl.SubscriptionIdentifiedImpl,
                    SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
                    SubscriptionImpl.SubscriptionIdentifierImpl
                    > newDatabase = new Database<
                    MarkerType,
                    EpisodeImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
                    EpisodeImpl.EpisodeIdentifierImpl,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
                    EpisodeImpl.EpisodeListImpl,
                    PodcastImpl,
                    PodcastImpl.PodcastIdentifiedImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                    PodcastImpl.PodcastIdentifierImpl,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
                    PodcastSearchImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl,
                    SubscriptionImpl,
                    SubscriptionImpl.SubscriptionIdentifiedImpl,
                    SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
                    SubscriptionImpl.SubscriptionIdentifierImpl
                    >(
                    ApplicationProvider.getApplicationContext(),
                    null, // in-memory
                    Schedulers.trampoline(),
                    EpisodeImpl::new,
                    EpisodeImpl.EpisodeIdentifierImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl::new,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl::new,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl::new,
                    EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl::new,
                    PodcastImpl::new,
                    PodcastImpl.PodcastIdentifierImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl::new,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl::new,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl::new,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl::new,
                    PodcastSearchImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl::new,
                    PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl::new,
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
