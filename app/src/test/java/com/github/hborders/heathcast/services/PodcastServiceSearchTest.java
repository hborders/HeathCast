package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.NonnullPair;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
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
import static com.github.hborders.heathcast.matchers.NonnullPairMatchers.nonnullPair;
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

        final MatcherTestObserver<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>> planetMoneyMatcherTestObserver =
                new MatcherTestObserver<>();
        final NetworkPauser planetMoneyNetworkPauser = new NetworkPauser();
        final PodcastSearch planetMoneyPodcastSearch = new PodcastSearch("Planet Money");
        getTestObject()
                .searchForPodcasts(
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
                        nonnullPair(
                                containsNothing(),
                                is(ServiceRequestState.loading())
                        )
                )
        );

        planetMoneyNetworkPauser.resume();

        planetMoneyMatcherTestObserver.awaitCount(3);

        planetMoneyMatcherTestObserver.assertValueSequenceThat(
                containsInOrder(
                        nonnullPair(
                                containsNothing(),
                                is(ServiceRequestState.loading())
                        ),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        nonnullPair(
                                not(emptyIterable()),
                                is(ServiceRequestState.loaded())
                        )
                )
        );

        final NonnullPair<List<Identified<Podcast>>, ServiceRequestState> lastPlanetMoneyValue =
                Objects.requireNonNull(planetMoneyMatcherTestObserver.lastValue());

        final MatcherTestObserver<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>> theIndicatorMatcherTestObserver =
                new MatcherTestObserver<>();
        final NetworkPauser theIndicatorNetworkPauser = new NetworkPauser();
        final PodcastSearch theIndicatorPodcastSearch = new PodcastSearch("The Indicator");
        getTestObject()
                .searchForPodcasts(
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
                        nonnullPair(
                                containsNothing(),
                                is(ServiceRequestState.loading())
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
                        nonnullPair(
                                containsNothing(),
                                is(ServiceRequestState.loading())
                        ),
                        // there is a race where List<Identified<Podcast>> or ServiceRequestState
                        // could change first, so just ignore this value.
                        anything(),
                        nonnullPair(
                                not(emptyIterable()),
                                is(ServiceRequestState.loaded())
                        )
                )
        );
    }
}