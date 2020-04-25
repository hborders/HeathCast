package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.PodcastSearchImpl;
import com.github.hborders.heathcast.features.model.SubscriptionImpl;
import com.github.hborders.heathcast.matchers.IsSpecificIterableContainingInOrder;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;

import org.junit.Test;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsEmptySpecificIterable.specificallyEmpty;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static com.github.hborders.heathcast.matchers.ServiceResponseMatchers.serviceResponseComplete;
import static com.github.hborders.heathcast.matchers.ServiceResponseMatchers.serviceResponseLoading;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

@org.junit.runner.RunWith(org.robolectric.RobolectricTestRunner.class)
//@org.junit.runner.RunWith(org.junit.runners.BlockJUnit4ClassRunner.class)
public final class PodcastServiceSearchTest extends AbstractPodcastServiceTest {
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
            > getTestObject() throws Throwable {
        return getPodcastService();
    }

    @Test
    public void testSearchForPlanetMoneyThenTheIndicator() throws Throwable {
        final MatcherTestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueThat(
                specificallyEmpty()
        );

        final MatcherTestObserver<TestPodcastListServiceResponse> planetMoneyMatcherTestObserver =
                new MatcherTestObserver<>();
        final NetworkPauser planetMoneyNetworkPauser = new NetworkPauser();
        final PodcastSearchImpl planetMoneyPodcastSearch = new PodcastSearchImpl("Planet Money");
        final Observable<TestPodcastListServiceResponse> testPodcastListServiceResponseObservable =
                getTestObject()
                        .searchForPodcasts2(
                                TestPodcastListServiceResponseLoading::new,
                                TestPodcastListServiceResponseComplete::new,
                                TestPodcastListServiceResponseFailed::new,
                                planetMoneyNetworkPauser,
                                planetMoneyPodcastSearch
                        );
        testPodcastListServiceResponseObservable
                .subscribe(planetMoneyMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        containsNothing(),
                        containsInOrder(
                                identifiedModel(
                                        planetMoneyPodcastSearch
                                )
                        )
                )
        );

        planetMoneyMatcherTestObserver.assertValueSequenceThat(
                IsSpecificIterableContainingInOrder.specificallyContainsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        )
                )
        );

        planetMoneyNetworkPauser.resume();

        planetMoneyMatcherTestObserver.awaitCount(3);

        planetMoneyMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                specificallyEmpty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                TestPodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );

        final TestPodcastListServiceResponse lastPlanetMoneyValue =
                Objects.requireNonNull(planetMoneyMatcherTestObserver.lastValue());

        final MatcherTestObserver<TestPodcastListServiceResponse> theIndicatorMatcherTestObserver =
                new MatcherTestObserver<>();
        final NetworkPauser theIndicatorNetworkPauser = new NetworkPauser();
        final PodcastSearchImpl theIndicatorPodcastSearch = new PodcastSearchImpl("The Indicator");
        final Observable<TestPodcastListServiceResponse> podcastListServiceResponseObservable =
                getTestObject()
                        .searchForPodcasts2(
                                TestPodcastListServiceResponseLoading::new,
                                TestPodcastListServiceResponseComplete::new,
                                TestPodcastListServiceResponseFailed::new,
                                theIndicatorNetworkPauser,
                                theIndicatorPodcastSearch
                        );
        podcastListServiceResponseObservable
                .subscribe(theIndicatorMatcherTestObserver);

        // no changes to Planet Money
        // don't assert a specific value stream because SqlBrite might cause requerying
        planetMoneyMatcherTestObserver.assertLastValue(lastPlanetMoneyValue);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        containsNothing(),
                        containsInOrder(
                                identifiedModel(
                                        planetMoneyPodcastSearch
                                )
                        ),
                        containsInOrder(
                                identifiedModel(
                                        theIndicatorPodcastSearch
                                ),
                                identifiedModel(
                                        planetMoneyPodcastSearch
                                )
                        )
                )
        );

        theIndicatorMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        )
                )
        );

        theIndicatorNetworkPauser.resume();

        theIndicatorMatcherTestObserver.awaitCount(3);

        // no changes to Planet Money
        // don't assert a specific value stream because SqlBrite might cause requerying
        planetMoneyMatcherTestObserver.assertLastValue(lastPlanetMoneyValue);

        theIndicatorMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                TestPodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );
    }

    @Test
    public void testSearchForPlanetMoneyTwiceQuickly() throws Throwable {
        final MatcherTestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueThat(
                empty()
        );

        final MatcherTestObserver<TestPodcastListServiceResponse> planetMoneyMatcherTestObserver1 =
                new MatcherTestObserver<>();
        final MatcherTestObserver<TestPodcastListServiceResponse> planetMoneyMatcherTestObserver2 =
                new MatcherTestObserver<>();
        final NetworkPauser planetMoneyNetworkPauser1 = new NetworkPauser();
        final PodcastSearchImpl planetMoneyPodcastSearch = new PodcastSearchImpl("Planet Money");
        final Observable<TestPodcastListServiceResponse> testPodcastListServiceResponseObservable1 =
                getTestObject()
                        .searchForPodcasts2(
                                TestPodcastListServiceResponseLoading::new,
                                TestPodcastListServiceResponseComplete::new,
                                TestPodcastListServiceResponseFailed::new,
                                planetMoneyNetworkPauser1,
                                planetMoneyPodcastSearch
                        );
        testPodcastListServiceResponseObservable1.subscribe(planetMoneyMatcherTestObserver1);
        final NetworkPauser planetMoneyNetworkPauser2 = new NetworkPauser();
        final Observable<TestPodcastListServiceResponse> testPodcastListServiceResponseObservable2 =
                getTestObject()
                        .searchForPodcasts2(
                                TestPodcastListServiceResponseLoading::new,
                                TestPodcastListServiceResponseComplete::new,
                                TestPodcastListServiceResponseFailed::new,
                                planetMoneyNetworkPauser2,
                                planetMoneyPodcastSearch
                        );
        testPodcastListServiceResponseObservable2.subscribe(planetMoneyMatcherTestObserver2);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        containsNothing(),
                        containsInOrder(
                                identifiedModel(
                                        planetMoneyPodcastSearch
                                )
                        ),
                        containsInOrder(
                                identifiedModel(
                                        planetMoneyPodcastSearch
                                )
                        )
                )
        );

        planetMoneyMatcherTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        ),
                        // a second empty loading is expected because the new search
                        // causes an upsert, which will trigger a new fetch
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        )
                )
        );

        planetMoneyNetworkPauser1.resume();

        planetMoneyMatcherTestObserver1.awaitCount(4);

        planetMoneyMatcherTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        ),
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                TestPodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );

        planetMoneyMatcherTestObserver2.awaitCount(2);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        ),
                        // the first request finished, so we should get results
                        // from it, but we're still loading because our request didn't
                        // finish
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );

        planetMoneyNetworkPauser2.resume();

        planetMoneyMatcherTestObserver2.awaitCount(4);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                empty()
                        ),
                        serviceResponseLoading(
                                TestPodcastListServiceResponse.class,
                                not(empty())
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                TestPodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );
    }
}
