package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static com.github.hborders.heathcast.matchers.ServiceResponseMatchers.serviceResponse;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyIterable;

@RunWith(RobolectricTestRunner.class)
public final class PodcastServiceSearchTest extends AbstractPodcastServiceTest {
    private PodcastService getTestObject() {
        return getPodcastService();
    }

    @Test
    public void testSearchForPlanetMoneyThenTheIndicator() {
        final MatcherTestObserver<List<Identified<PodcastSearch>>> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueSequence(
                Collections.singletonList(
                        Collections.emptyList()
                )
        );

        final MatcherTestObserver<ServiceResponse<PodcastIdentifiedList>> planetMoneyMatcherTestObserver =
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
                containsInOrder(
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        )
                )
        );

        planetMoneyNetworkPauser.resume();

        planetMoneyMatcherTestObserver.awaitCount(3);

        planetMoneyMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        ),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse(
                                not(emptyIterable()),
                                ServiceResponse.RemoteStatus.complete()
                        )
                )
        );

        final ServiceResponse<PodcastIdentifiedList> lastPlanetMoneyValue =
                Objects.requireNonNull(planetMoneyMatcherTestObserver.lastValue());

        final MatcherTestObserver<ServiceResponse<PodcastIdentifiedList>>  theIndicatorMatcherTestObserver =
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
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
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
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        ),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse(
                                not(emptyIterable()),
                                ServiceResponse.RemoteStatus.complete()
                        )
                )
        );
    }

    @Test
    public void testSearchForPlanetMoneyTwiceQuickly() {
        final MatcherTestObserver<List<Identified<PodcastSearch>>> podcastSearchIdentifiedsMatcherTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchIdentifiedsMatcherTestObserver);

        podcastSearchIdentifiedsMatcherTestObserver.assertValueSequence(
                Collections.singletonList(
                        Collections.emptyList()
                )
        );

        final MatcherTestObserver<ServiceResponse<PodcastIdentifiedList>> planetMoneyMatcherTestObserver1 =
                new MatcherTestObserver<>();
        final MatcherTestObserver<ServiceResponse<PodcastIdentifiedList>> planetMoneyMatcherTestObserver2 =
                new MatcherTestObserver<>();
        final NetworkPauser planetMoneyNetworkPauser = new NetworkPauser();
        final PodcastSearch planetMoneyPodcastSearch = new PodcastSearch("Planet Money");
        getTestObject()
                .searchForPodcasts2(
                        planetMoneyNetworkPauser,
                        planetMoneyPodcastSearch
                )
                .subscribe(planetMoneyMatcherTestObserver1);
        getTestObject()
                .searchForPodcasts2(
                        planetMoneyNetworkPauser,
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
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        ),
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        )
                )
        );

        planetMoneyNetworkPauser.resume();

        planetMoneyMatcherTestObserver1.awaitCount(4);

        planetMoneyMatcherTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        ),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse(
                                not(emptyIterable()),
                                ServiceResponse.RemoteStatus.complete()
                        )
                )
        );

        planetMoneyMatcherTestObserver2.awaitCount(4);

        planetMoneyMatcherTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        is(
                                new ServiceResponse<>(
                                        PodcastIdentifiedList.class,
                                        new PodcastIdentifiedList(),
                                        ServiceResponse.RemoteStatus.loading()
                                )
                        ),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        serviceResponse(
                                not(emptyIterable()),
                                ServiceResponse.RemoteStatus.complete()
                        )
                )
        );
    }
}
