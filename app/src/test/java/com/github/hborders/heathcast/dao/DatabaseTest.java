package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.PodcastSearchImpl;
import com.github.hborders.heathcast.features.model.SubscriptionImpl;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;
import com.github.hborders.heathcast.util.DateUtil;
import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import io.reactivex.rxjava3.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsEmptySpecificIterable.specificallyEmpty;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static com.github.hborders.heathcast.matchers.IsSpecificIterableContainingInOrder.specificallyContainsInOrder;
import static com.github.hborders.heathcast.matchers.MarkedValueMatchers.markedValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest extends AbstractDatabaseTest<Object> {
    private final PodcastImpl podcast1;
    private final PodcastImpl podcast11;
    private final PodcastImpl podcast12;
    private final PodcastImpl podcast2;
    private final PodcastImpl podcast3;
    private final PodcastImpl podcast4;
    private final PodcastImpl podcast5;

    private final EpisodeImpl episode11;
    private final EpisodeImpl episode12;
    private final EpisodeImpl episode13;
    private final EpisodeImpl episode21;
    private final EpisodeImpl episode22;
    private final EpisodeImpl episode23;

    public DatabaseTest() throws Exception {
        podcast1 = new PodcastImpl(
                new URL("http://example.com/podcast1/artwork"),
                "author1",
                new URL("http://example.com/feed1"),
                "Podcast1"
        );
        podcast11 = new PodcastImpl(
                new URL("http://example.com/podcast11/artwork"),
                "author11",
                new URL("http://example.com/feed1"),
                "Podcast11"
        );
        podcast12 = new PodcastImpl(
                new URL("http://example.com/podcast12/artwork"),
                "author12",
                new URL("http://example.com/feed1"),
                "Podcast12"
        );
        podcast2 = new PodcastImpl(
                new URL("http://example.com/podcast2/artwork"),
                "author2",
                new URL("http://example.com/feed2"),
                "Podcast2"
        );
        podcast3 = new PodcastImpl(
                new URL("http://example.com/podcast3/artwork"),
                "author3",
                new URL("http://example.com/feed3"),
                "Podcast3"
        );
        podcast4 = new PodcastImpl(
                new URL("http://example.com/podcast4/artwork"),
                "author4",
                new URL("http://example.com/feed4"),
                "Podcast4"
        );
        podcast5 = new PodcastImpl(
                new URL("http://example.com/podcast5/artwork"),
                "author5",
                new URL("http://example.com/feed5"),
                "Podcast5"
        );

        episode11 = new EpisodeImpl(
                new URL("http://example.com/podcast1/episode1/artwork"),
                Duration.ofSeconds(11),
                DateUtil.from(
                        2019,
                        1,
                        1
                ),
                "summary11",
                "title11",
                new URL("http://example.com/podcast1/episode1")
        );
        episode12 = new EpisodeImpl(
                new URL("http://example.com/podcast1/episode2/artwork"),
                Duration.ofSeconds(12),
                DateUtil.from(
                        2019,
                        1,
                        2
                ),
                "summary12",
                "title12",
                new URL("http://example.com/podcast1/episode2")
        );
        episode13 = new EpisodeImpl(
                new URL("http://example.com/podcast1/episode3/artwork"),
                Duration.ofSeconds(13),
                DateUtil.from(
                        2019,
                        1,
                        3
                ),
                "summary13",
                "title13",
                new URL("http://example.com/podcast1/episode3")
        );

        episode21 = new EpisodeImpl(
                new URL("http://example.com/podcast2/episode1/artwork"),
                Duration.ofSeconds(21),
                DateUtil.from(
                        2019,
                        2,
                        1
                ),
                "summary21",
                "title21",
                new URL("http://example.com/podcast2/episode1")
        );
        episode22 = new EpisodeImpl(
                new URL("http://example.com/podcast2/episode2/artwork"),
                Duration.ofSeconds(22),
                DateUtil.from(
                        2019,
                        2,
                        2
                ),
                "summary22",
                "title22",
                new URL("http://example.com/podcast2/episode2")
        );
        episode23 = new EpisodeImpl(
                new URL("http://example.com/podcast2/episode3/artwork"),
                Duration.ofSeconds(23),
                DateUtil.from(
                        2019,
                        2,
                        3
                ),
                "summary23",
                "title23",
                new URL("http://example.com/podcast2/episode3")
        );
    }

    private Database<
            Object,
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
            > getTestObject() {
        return getDatabase();
    }

    @Test
    public void testEmptyReplacePodcastSearchResultsForNewPodcastSearch() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search")).orElseNull();
        if (podcastSearchIdentified == null) {
            fail();
        }

        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified.identifier)
                .subscribe(podcastIdentifiedsTestObserver);

        podcastIdentifiedsTestObserver.assertValueThat(
                markedValue(
                        specificallyEmpty()
                )
        );

        final Object marker1 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker1,
                podcastSearchIdentified,
                new PodcastImpl.PodcastListImpl()
        );

        podcastIdentifiedsTestObserver.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyEmpty()
                        ),
                        markedValue(
                                marker1,
                                specificallyEmpty()
                        )
                )
        );
    }

    @Test
    public void testEmptyReplacePodcastSearchResultsForExistingPodcastSearch() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search")).orElseNull();
        if (podcastSearchIdentified == null) {
            fail();
        }

        final Object marker1 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker1,
                podcastSearchIdentified,
                new PodcastImpl.PodcastListImpl(
                        podcast1,
                        podcast2,
                        podcast3
                )
        );

        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsTestObserver =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified.identifier)
                .subscribe(podcastIdentifiedsTestObserver);

        podcastIdentifiedsTestObserver.assertValueThat(
                markedValue(
                        // won't contain marker1 since we weren't observing when
                        // we did the marker1 replacement
                        specificallyContainsInOrder(
                                identifiedModel(podcast1),
                                identifiedModel(podcast2),
                                identifiedModel(podcast3)
                        )
                )
        );

        final Object marker2 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker2,
                podcastSearchIdentified,
                new PodcastImpl.PodcastListImpl()
        );

        podcastIdentifiedsTestObserver.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                // won't contain marker1 since we weren't observing when
                                // we did the marker1 replacement
                                specificallyContainsInOrder(
                                        identifiedModel(podcast1),
                                        identifiedModel(podcast2),
                                        identifiedModel(podcast3)
                                )
                        ),
                        markedValue(
                                marker2,
                                specificallyEmpty()
                        )
                )
        );
    }

    @Test
    public void testOuterReplacePodcastSearchResultsForFirstPodcastSearch() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search1")).orElseNull();
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search2")).orElseNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        }
        if (podcastSearchIdentified2 == null) {
            fail();
        }

        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastIdentifiedsTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastIdentifiedsTestObserver1);
        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastIdentifiedsTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastIdentifiedsTestObserver2);


        podcastIdentifiedsTestObserver1.assertValueThat(empty());
        podcastIdentifiedsTestObserver2.assertValueThat(empty());

        final Object marker = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker,
                podcastSearchIdentified1,
                new PodcastImpl.PodcastListImpl(
                        podcast1,
                        podcast2,
                        podcast3
                )
        );

        podcastIdentifiedsTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        specificallyEmpty(),
                        containsInOrder(
                                identifiedModel(podcast1),
                                identifiedModel(podcast2),
                                identifiedModel(podcast3)
                        )
                )
        );
        podcastIdentifiedsTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        specificallyEmpty(),
                        specificallyEmpty()
                )
        );
    }

    @Test
    public void testOuterReplacePodcastSearchResultsUpdatesExistingPodcastSearchResults() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search1")).orElseNull();
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search2")).orElseNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        }
        if (podcastSearchIdentified2 == null) {
            fail();
        }

        final Object marker1 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker1,
                podcastSearchIdentified1,
                new PodcastImpl.PodcastListImpl(
                        podcast11,
                        podcast2,
                        podcast3
                )
        );
        final Object marker2 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker2,
                podcastSearchIdentified1,
                new PodcastImpl.PodcastListImpl(
                        podcast12,
                        podcast2,
                        podcast4
                )
        );

        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastIdentifiedsTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastIdentifiedsTestObserver1);
        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastIdentifiedsTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastIdentifiedsTestObserver2);

        podcastIdentifiedsTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast12),
                                identifiedModel(podcast2),
                                identifiedModel(podcast4)
                        )
                )
        );
        podcastIdentifiedsTestObserver2.assertValueThat(empty());

        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsMarkedValueTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastIdentifiedsMarkedValueTestObserver1);
        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsMarkedValueTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastIdentifiedsMarkedValueTestObserver2);

        podcastIdentifiedsMarkedValueTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast12),
                                        identifiedModel(podcast2),
                                        identifiedModel(podcast4)
                                )
                        )
                )
        );
        podcastIdentifiedsMarkedValueTestObserver2.assertValueThat(markedValue(specificallyEmpty()));
    }

    @Test
    public void testOuterReplacePodcastSearchResultsCanIncludePodcastsFromOtherSearchResults() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search1")).orElseNull();
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search2")).orElseNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        }
        if (podcastSearchIdentified2 == null) {
            fail();
        }

        final Object marker1 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker1,
                podcastSearchIdentified1,
                new PodcastImpl.PodcastListImpl(
                        podcast1,
                        podcast2,
                        podcast3,
                        podcast4
                )
        );
        final Object marker2 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker2,
                podcastSearchIdentified2,
                new PodcastImpl.PodcastListImpl(
                        podcast5,
                        podcast4,
                        podcast3,
                        podcast2
                )
        );

        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastTestObserver1);

        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastTestObserver2);

        podcastTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast1),
                                identifiedModel(podcast2),
                                identifiedModel(podcast3),
                                identifiedModel(podcast4)
                        )
                )
        );
        podcastTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast5),
                                identifiedModel(podcast4),
                                identifiedModel(podcast3),
                                identifiedModel(podcast2)
                        )
                )
        );

        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsMarkedValueTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastIdentifiedsMarkedValueTestObserver1);
        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsMarkedValueTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastIdentifiedsMarkedValueTestObserver2);


        podcastIdentifiedsMarkedValueTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast1),
                                        identifiedModel(podcast2),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast4)
                                )
                        )
                )
        );
        podcastIdentifiedsMarkedValueTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast5),
                                        identifiedModel(podcast4),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast2)
                                )
                        )
                )
        );
    }

    @Test
    public void testDeletePodcastSearchKeepsOtherSearchResultsWithSamePodcasts() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search1")).orElseNull();
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifiedImpl podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearchImpl("Search2")).orElseNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        }
        if (podcastSearchIdentified2 == null) {
            fail();
        }

        final Object marker1 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker1,
                podcastSearchIdentified1,
                new PodcastImpl.PodcastListImpl(
                        podcast1,
                        podcast2,
                        podcast3,
                        podcast4
                )
        );
        final Object marker2 = new Object();
        getTestObject().replacePodcastSearchPodcasts(
                marker2,
                podcastSearchIdentified2,
                new PodcastImpl.PodcastListImpl(
                        podcast5,
                        podcast4,
                        podcast3,
                        podcast2
                )
        );

        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastTestObserver1);

        final MatcherTestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl> podcastTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastTestObserver2);

        podcastTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast1),
                                identifiedModel(podcast2),
                                identifiedModel(podcast3),
                                identifiedModel(podcast4)
                        )
                )
        );

        podcastTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast5),
                                identifiedModel(podcast4),
                                identifiedModel(podcast3),
                                identifiedModel(podcast2)
                        )
                )
        );

        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsMarkedValueTestObserver1 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                .subscribe(podcastIdentifiedsMarkedValueTestObserver1);
        final MatcherTestObserver<
                MarkedValue<
                        Object,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                        >
                > podcastIdentifiedsMarkedValueTestObserver2 =
                new MatcherTestObserver<>();
        getTestObject()
                .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified2.identifier)
                .subscribe(podcastIdentifiedsMarkedValueTestObserver2);


        podcastIdentifiedsMarkedValueTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast1),
                                        identifiedModel(podcast2),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast4)
                                )
                        )
                )
        );
        podcastIdentifiedsMarkedValueTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast5),
                                        identifiedModel(podcast4),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast2)
                                )
                        )
                )
        );

        final boolean deleted = getTestObject().deletePodcastSearch(podcastSearchIdentified2.identifier);
        assertThat(
                deleted,
                equalTo(true)
        );

        podcastTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast1),
                                identifiedModel(podcast2),
                                identifiedModel(podcast3),
                                identifiedModel(podcast4)
                        ),
                        containsInOrder(
                                identifiedModel(podcast1),
                                identifiedModel(podcast2),
                                identifiedModel(podcast3),
                                identifiedModel(podcast4)
                        )
                )
        );

        podcastTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(podcast5),
                                identifiedModel(podcast4),
                                identifiedModel(podcast3),
                                identifiedModel(podcast2)
                        ),
                        specificallyEmpty()
                )
        );

        podcastIdentifiedsMarkedValueTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast1),
                                        identifiedModel(podcast2),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast4)
                                )
                        ),
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast1),
                                        identifiedModel(podcast2),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast4)
                                )
                        )
                )
        );

        podcastIdentifiedsMarkedValueTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        markedValue(
                                specificallyContainsInOrder(
                                        identifiedModel(podcast5),
                                        identifiedModel(podcast4),
                                        identifiedModel(podcast3),
                                        identifiedModel(podcast2)
                                )
                        ),
                        markedValue(
                                specificallyEmpty()
                        )
                )
        );
    }

    @Test
    public void testDeletePodcastDeletesEpisodes() {
        @Nullable final PodcastImpl.PodcastIdentifiedImpl podcastIdentified1 =
                getTestObject().upsertPodcast(podcast1).orElse(null);
        @Nullable final PodcastImpl.PodcastIdentifiedImpl podcastIdentified2 =
                getTestObject().upsertPodcast(podcast2).orElse(null);
        if (podcastIdentified1 == null) {
            fail();
        }
        if (podcastIdentified2 == null) {
            fail();
        }

        getTestObject().upsertEpisodesForPodcast(
                podcastIdentified1.identifier,
                new EpisodeImpl.EpisodeListImpl(
                        episode11,
                        episode12,
                        episode13
                )
        );
        getTestObject().upsertEpisodesForPodcast(
                podcastIdentified2.identifier,
                new EpisodeImpl.EpisodeListImpl(
                        episode21,
                        episode22,
                        episode23
                )
        );

        final MatcherTestObserver<
                EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl
                > episodeIdentifiedsTestObserver1 = new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentified1.identifier)
                .subscribe(episodeIdentifiedsTestObserver1);

        final MatcherTestObserver<
                EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl
                > episodeIdentifiedsTestObserver2 = new MatcherTestObserver<>();
        getTestObject()
                .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentified2.identifier)
                .subscribe(episodeIdentifiedsTestObserver2);

        episodeIdentifiedsTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(episode11),
                                identifiedModel(episode12),
                                identifiedModel(episode13)
                        )
                )
        );

        episodeIdentifiedsTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(episode21),
                                identifiedModel(episode22),
                                identifiedModel(episode23)
                        )
                )
        );

        final boolean deleted = getTestObject().deletePodcast(podcastIdentified2.identifier);
        assertThat(
                deleted,
                equalTo(true)
        );

        episodeIdentifiedsTestObserver1.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(episode11),
                                identifiedModel(episode12),
                                identifiedModel(episode13)
                        ),
                        containsInOrder(
                                identifiedModel(episode11),
                                identifiedModel(episode12),
                                identifiedModel(episode13)
                        )
                )
        );

        episodeIdentifiedsTestObserver2.assertValueSequenceThat(
                containsInOrder(
                        containsInOrder(
                                identifiedModel(episode21),
                                identifiedModel(episode22),
                                identifiedModel(episode23)
                        ),
                        containsNothing()
                )
        );
    }

    @Test
    public void testDeletePodcastDeletesSubscription() {
        @Nullable final PodcastImpl.PodcastIdentifiedImpl podcastIdentified1 =
                getTestObject().upsertPodcast(podcast1).orElse(null);
        @Nullable final PodcastImpl.PodcastIdentifiedImpl podcastIdentified2 =
                getTestObject().upsertPodcast(podcast2).orElse(null);
        @Nullable final PodcastImpl.PodcastIdentifiedImpl podcastIdentified3 =
                getTestObject().upsertPodcast(podcast3).orElse(null);
        if (podcastIdentified1 == null) {
            fail();
        }
        if (podcastIdentified2 == null) {
            fail();
        }
        if (podcastIdentified3 == null) {
            fail();
        }

        @Nullable final SubscriptionImpl.SubscriptionIdentifiedImpl subscriptionIdentified1 =
                getTestObject().subscribe(podcastIdentified1).orElse(null);
        @Nullable final SubscriptionImpl.SubscriptionIdentifiedImpl subscriptionIdentified2 =
                getTestObject().subscribe(podcastIdentified2).orElse(null);
        @Nullable final SubscriptionImpl.SubscriptionIdentifiedImpl subscriptionIdentified3 =
                getTestObject().subscribe(podcastIdentified3).orElse(null);
        if (subscriptionIdentified1 == null) {
            fail();
        }
        if (subscriptionIdentified2 == null) {
            fail();
        }
        if (subscriptionIdentified3 == null) {
            fail();
        }

        final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                new TestObserver<>();
        getTestObject()
                .observeQueryForSubscriptions()
                .subscribe(subscriptionTestObserver);

        subscriptionTestObserver.assertValueSequence(
                Collections.singletonList(
                        new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                subscriptionIdentified1,
                                subscriptionIdentified2,
                                subscriptionIdentified3
                        )
                )
        );

        getTestObject().deletePodcast(podcastIdentified1.identifier);

        subscriptionTestObserver.assertValueSequence(
                Arrays.asList(
                        new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                subscriptionIdentified1,
                                subscriptionIdentified2,
                                subscriptionIdentified3
                        ),
                        new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                subscriptionIdentified2,
                                subscriptionIdentified3
                        )
                )
        );

    }

    @Test
    public void testSubscribeUnsubscribeSubscribeUnsubscribeUpdatesSubscriptionIdentifierQuery() {
        @Nullable final PodcastImpl.PodcastIdentifiedImpl podcastIdentified =
                getTestObject().upsertPodcast(podcast1).orElse(null);
        if (podcastIdentified == null) {
            fail();
        }

        final TestObserver<
                Optional<
                        SubscriptionImpl.SubscriptionIdentifierImpl
                        >
                > subscriptionIdentifierTestObserver =
                new TestObserver<>();
        getTestObject()
                .observeQueryForSubscriptionIdentifier(podcastIdentified.identifier)
                .subscribe(subscriptionIdentifierTestObserver);

        @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                getTestObject().subscribe(podcastIdentified.identifier).orElse(null);
        if (subscriptionIdentifier1 == null) {
            fail();
        }

        final Result result1 = getTestObject()
                .unsubscribe(subscriptionIdentifier1);
        if (result1.reduce(
                success -> false,
                failure -> true
        )) {
            fail();
        }

        @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                getTestObject().subscribe(podcastIdentified.identifier).orElse(null);
        if (subscriptionIdentifier2 == null) {
            fail();
        }

        final Result result2 = getTestObject()
                .unsubscribe(subscriptionIdentifier2);
        if (result2.reduce(
                success -> false,
                failure -> true
        )) {
            fail();
        }

        subscriptionIdentifierTestObserver.assertValueSequence(
                Arrays.asList(
                        Optional.empty(),
                        Optional.of(subscriptionIdentifier1),
                        Optional.empty(),
                        Optional.of(subscriptionIdentifier2),
                        Optional.empty()
                )
        );
    }
}