package com.github.hborders.heathcast.services;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.dao.AbstractDatabaseTest;
import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.PodcastSearchImpl;
import com.github.hborders.heathcast.features.model.SubscriptionImpl;

import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class AbstractPodcastServiceTest extends AbstractDatabaseTest<Object> {
    public interface TestPodcastListServiceResponse extends PodcastListServiceResponse<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
    }

    final class TestPodcastListServiceResponseLoading extends Either31.LeftImpl<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponse.PodcastListServiceResponseLoading<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, TestPodcastListServiceResponse {
        public TestPodcastListServiceResponseLoading(PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value) {
            super(
                    TestPodcastListServiceResponseLoading.class,
                    value
            );
        }
    }

    final class TestPodcastListServiceResponseComplete extends Either31.MiddleImpl<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponse.PodcastListServiceResponseComplete<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, TestPodcastListServiceResponse {
        public TestPodcastListServiceResponseComplete(
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value
        ) {
            super(
                    TestPodcastListServiceResponseComplete.class,
                    value
            );
        }
    }

    final class TestPodcastListServiceResponseFailed extends Either31.RightImpl<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponse.PodcastListServiceResponseFailed<
            TestPodcastListServiceResponseLoading,
            TestPodcastListServiceResponseComplete,
            TestPodcastListServiceResponseFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, TestPodcastListServiceResponse {
        public TestPodcastListServiceResponseFailed(
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value
        ) {
            super(
                    TestPodcastListServiceResponseFailed.class,
                    value
            );
        }
    }

    @Nullable
    private PodcastService<
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
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
            PodcastImpl.PodcastListImpl,
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
            > podcastService;

    protected final PodcastService<
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
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
            PodcastImpl.PodcastListImpl,
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
            > getPodcastService() {
        @Nullable final PodcastService<
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
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                PodcastImpl.PodcastIdentifierImpl,
                PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
                PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
                PodcastImpl.PodcastListImpl,
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
                > initialPodcastService = this.podcastService;
        if (initialPodcastService == null) {
            final PodcastService<
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
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
                    PodcastImpl.PodcastIdentifierImpl,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
                    PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
                    PodcastImpl.PodcastListImpl,
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
                    > newPodcastService = new PodcastService<>(
                    EpisodeImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl::new,
                    EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl::new,
                    EpisodeImpl.EpisodeIdentifierImpl::new,
                    EpisodeImpl.EpisodeListImpl::new,
                    EpisodeImpl.EpisodeListImpl::new,
                    PodcastImpl::new,
                    PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl::new,
                    PodcastImpl.PodcastListImpl::new,
                    getDatabase(),
                    Schedulers.trampoline()
            );

            this.podcastService = newPodcastService;
            return newPodcastService;
        } else {
            return initialPodcastService;
        }
    }
}
