package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;

import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsEmptySpecificIterable.specificallyEmpty;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static com.github.hborders.heathcast.matchers.IsSpecificIterableContainingInOrder.specificallyContains;
import static com.github.hborders.heathcast.matchers.ServiceResponse1Matchers.serviceResponse1Complete;
import static com.github.hborders.heathcast.matchers.ServiceResponse1Matchers.serviceResponse1Loading;
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
        final MatcherTestObserver<List<PodcastSearchIdentified>> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueThat(
                specificallyEmpty()
        );

        final MatcherTestObserver<ServiceResponse1<PodcastIdentifiedList>> planetMoneyMatcherTestObserver =
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
                specificallyContains(
                        serviceResponse1Loading(empty())
                )
        );

        planetMoneyNetworkPauser.resume();

        planetMoneyMatcherTestObserver.awaitCount(3);

        planetMoneyMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        serviceResponse1Loading(
                                specificallyEmpty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse1Complete(
                                not(empty())
                        )
                )
        );

        final ServiceResponse1<PodcastIdentifiedList> lastPlanetMoneyValue =
                Objects.requireNonNull(planetMoneyMatcherTestObserver.lastValue());

        final MatcherTestObserver<ServiceResponse1<PodcastIdentifiedList>> theIndicatorMatcherTestObserver =
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
                        serviceResponse1Loading(empty())
                )
        );

        theIndicatorNetworkPauser.resume();

        theIndicatorMatcherTestObserver.awaitCount(3);

        // no changes to Planet Money
        // don't assert a specific value stream because SqlBrite might cause requerying
        planetMoneyMatcherTestObserver.assertLastValue(lastPlanetMoneyValue);

        theIndicatorMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        serviceResponse1Loading(
                                empty()
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse1Complete(
                                not(empty())
                        )
                )
        );
    }

    @Test
    public void testSearchForPlanetMoneyTwiceQuickly() throws Throwable {
        final MatcherTestObserver<List<PodcastSearchIdentified>> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueThat(
                empty()
        );

        final MatcherTestObserver<ServiceResponse1<PodcastIdentifiedList>> planetMoneyMatcherTestObserver1 =
                new MatcherTestObserver<>();
        final MatcherTestObserver<ServiceResponse1<PodcastIdentifiedList>> planetMoneyMatcherTestObserver2 =
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
                        serviceResponse1Loading(empty()),
                        // a second empty loading is expected because the new search
                        // causes an upsert, which will trigger a new fetch
                        serviceResponse1Loading(empty())
                )
        );

        planetMoneyNetworkPauser1.resume();

        planetMoneyMatcherTestObserver1.awaitCount(4);

        planetMoneyMatcherTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        serviceResponse1Loading(
                                empty()
                        ),
                        serviceResponse1Loading(empty()),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse1Complete(
                                not(empty())
                        )
                )
        );

        planetMoneyMatcherTestObserver2.awaitCount(2);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        serviceResponse1Loading(
                                empty()
                        ),
                        // the first request finished, so we should get results
                        // from it, but we're still loading because our request didn't
                        // finish
                        serviceResponse1Loading(
                                not(empty())
                        )
                )
        );

        planetMoneyNetworkPauser2.resume();

        planetMoneyMatcherTestObserver2.awaitCount(4);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        serviceResponse1Loading(
                                empty()
                        ),
                        serviceResponse1Loading(
                                not(empty())
                        ),
                        // there is a race where List<PodcastIdentified> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse1Complete(
                                not(empty())
                        )
                )
        );
    }
}
