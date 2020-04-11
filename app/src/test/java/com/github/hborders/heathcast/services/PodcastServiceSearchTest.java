package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.matchers.IsSpecificIterableContainingInOrder;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.PodcastSearchIdentifiedList;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;

import org.junit.Test;

import java.util.Objects;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsEmptySpecificIterable.specificallyEmpty;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static com.github.hborders.heathcast.matchers.IsSpecificIterableContainingInOrder.specificallyContainsInOrder;
import static com.github.hborders.heathcast.matchers.ServiceResponseMatchers.serviceResponseComplete;
import static com.github.hborders.heathcast.matchers.ServiceResponseMatchers.serviceResponseLoading;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

@org.junit.runner.RunWith(org.robolectric.RobolectricTestRunner.class)
//@org.junit.runner.RunWith(org.junit.runners.BlockJUnit4ClassRunner.class)
public final class PodcastServiceSearchTest extends AbstractPodcastServiceTest {
    private PodcastService getTestObject() throws Throwable {
        return getPodcastService();
    }

    @Test
    public void testSearchForPlanetMoneyThenTheIndicator() throws Throwable {
        final MatcherTestObserver<PodcastSearchIdentifiedList> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueThat(
                specificallyEmpty()
        );

        final MatcherTestObserver<PodcastListServiceResponse> planetMoneyMatcherTestObserver =
                new MatcherTestObserver<>();
        final NetworkPauser planetMoneyNetworkPauser = new NetworkPauser();
        final PodcastSearch planetMoneyPodcastSearch = new PodcastSearch("Planet Money");
        getTestObject()
                .searchForPodcasts2(
                        planetMoneyNetworkPauser,
                        planetMoneyPodcastSearch
                )
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
                                PodcastListServiceResponse.class,
                                empty()
                        )
                )
        );

        planetMoneyNetworkPauser.resume();

        planetMoneyMatcherTestObserver.awaitCount(3);

        planetMoneyMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                specificallyEmpty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                PodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );

        final PodcastListServiceResponse lastPlanetMoneyValue =
                Objects.requireNonNull(planetMoneyMatcherTestObserver.lastValue());

        final MatcherTestObserver<PodcastListServiceResponse> theIndicatorMatcherTestObserver =
                new MatcherTestObserver<>();
        final NetworkPauser theIndicatorNetworkPauser = new NetworkPauser();
        final PodcastSearch theIndicatorPodcastSearch = new PodcastSearch("The Indicator");
        getTestObject()
                .searchForPodcasts2(
                        theIndicatorNetworkPauser,
                        theIndicatorPodcastSearch
                )
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
                                PodcastListServiceResponse.class,
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
                                PodcastListServiceResponse.class,
                                empty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                PodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );
    }

    @Test
    public void testSearchForPlanetMoneyTwiceQuickly() throws Throwable {
        final MatcherTestObserver<PodcastSearchIdentifiedList> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueThat(
                empty()
        );

        final MatcherTestObserver<PodcastListServiceResponse> planetMoneyMatcherTestObserver1 =
                new MatcherTestObserver<>();
        final MatcherTestObserver<PodcastListServiceResponse> planetMoneyMatcherTestObserver2 =
                new MatcherTestObserver<>();
        final NetworkPauser planetMoneyNetworkPauser1 = new NetworkPauser();
        final PodcastSearch planetMoneyPodcastSearch = new PodcastSearch("Planet Money");
        getTestObject()
                .searchForPodcasts2(
                        planetMoneyNetworkPauser1,
                        planetMoneyPodcastSearch
                )
                .subscribe(planetMoneyMatcherTestObserver1);
        final NetworkPauser planetMoneyNetworkPauser2 = new NetworkPauser();
        getTestObject()
                .searchForPodcasts2(
                        planetMoneyNetworkPauser2,
                        planetMoneyPodcastSearch
                )
                .subscribe(planetMoneyMatcherTestObserver2);

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
                                PodcastListServiceResponse.class,
                                empty()
                        ),
                        // a second empty loading is expected because the new search
                        // causes an upsert, which will trigger a new fetch
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                empty()
                        )
                )
        );

        planetMoneyNetworkPauser1.resume();

        planetMoneyMatcherTestObserver1.awaitCount(4);

        planetMoneyMatcherTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                empty()
                        ),
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                empty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                PodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );

        planetMoneyMatcherTestObserver2.awaitCount(2);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                empty()
                        ),
                        // the first request finished, so we should get results
                        // from it, but we're still loading because our request didn't
                        // finish
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );

        planetMoneyNetworkPauser2.resume();

        planetMoneyMatcherTestObserver2.awaitCount(4);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                empty()
                        ),
                        serviceResponseLoading(
                                PodcastListServiceResponse.class,
                                not(empty())
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponseComplete(
                                PodcastListServiceResponse.class,
                                not(empty())
                        )
                )
        );
    }
}
