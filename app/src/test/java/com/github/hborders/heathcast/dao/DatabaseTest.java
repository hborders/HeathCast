package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.EpisodeList;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.PodcastSearchIdentified;
import com.github.hborders.heathcast.models.SubscriptionIdentified;
import com.github.hborders.heathcast.models.SubscriptionIdentifiedList;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
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

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsEmptySpecificIterable.specificallyEmpty;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsSpecificIterableContainingInOrder.specificallyContainsInOrder;
import static com.github.hborders.heathcast.matchers.MarkedValueMatchers.markedValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest extends AbstractDatabaseTest<Object> {
    private final Podcast podcast1;
    private final Podcast podcast11;
    private final Podcast podcast12;
    private final Podcast podcast2;
    private final Podcast podcast3;
    private final Podcast podcast4;
    private final Podcast podcast5;

    private final Episode episode11;
    private final Episode episode12;
    private final Episode episode13;
    private final Episode episode21;
    private final Episode episode22;
    private final Episode episode23;

    public DatabaseTest() throws Exception {
        podcast1 = new Podcast(
                new URL("http://example.com/podcast1/artwork"),
                "author1",
                new URL("http://example.com/feed1"),
                "Podcast1"
        );
        podcast11 = new Podcast(
                new URL("http://example.com/podcast11/artwork"),
                "author11",
                new URL("http://example.com/feed1"),
                "Podcast11"
        );
        podcast12 = new Podcast(
                new URL("http://example.com/podcast12/artwork"),
                "author12",
                new URL("http://example.com/feed1"),
                "Podcast12"
        );
        podcast2 = new Podcast(
                new URL("http://example.com/podcast2/artwork"),
                "author2",
                new URL("http://example.com/feed2"),
                "Podcast2"
        );
        podcast3 = new Podcast(
                new URL("http://example.com/podcast3/artwork"),
                "author3",
                new URL("http://example.com/feed3"),
                "Podcast3"
        );
        podcast4 = new Podcast(
                new URL("http://example.com/podcast4/artwork"),
                "author4",
                new URL("http://example.com/feed4"),
                "Podcast4"
        );
        podcast5 = new Podcast(
                new URL("http://example.com/podcast5/artwork"),
                "author5",
                new URL("http://example.com/feed5"),
                "Podcast5"
        );

        episode11 = new Episode(
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
        episode12 = new Episode(
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
        episode13 = new Episode(
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

        episode21 = new Episode(
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
        episode22 = new Episode(
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
        episode23 = new Episode(
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

    private Database<Object> getTestObject() {
        return getDatabase();
    }

    @Test
    public void testEmptyReplacePodcastSearchResultsForNewPodcastSearch() {
        @Nullable final PodcastSearchIdentified podcastSearchIdentified =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search")).orNull();
        if (podcastSearchIdentified == null) {
            fail();
        } else {
            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsTestObserver =
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
                    new PodcastList()
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
    }

    @Test
    public void testEmptyReplacePodcastSearchResultsForExistingPodcastSearch() {
        @Nullable final PodcastSearchIdentified podcastSearchIdentified =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search")).orNull();
        if (podcastSearchIdentified == null) {
            fail();
        } else {
            final Object marker1 = new Object();
            getTestObject().replacePodcastSearchPodcasts(
                    marker1,
                    podcastSearchIdentified,
                    new PodcastList(
                            podcast1,
                            podcast2,
                            podcast3
                    )
            );

            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsTestObserver =
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
                    new PodcastList()
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
    }

    @Test
    public void testOuterReplacePodcastSearchResultsForFirstPodcastSearch() {
        @Nullable final PodcastSearchIdentified podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1")).orNull();
        @Nullable final PodcastSearchIdentified podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2")).orNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        } else if (podcastSearchIdentified2 == null) {
            fail();
        } else {
            final MatcherTestObserver<PodcastIdentifiedList> podcastIdentifiedsTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastIdentifiedsTestObserver1);
            final MatcherTestObserver<PodcastIdentifiedList> podcastIdentifiedsTestObserver2 =
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
                    new PodcastList(
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
    }

    @Test
    public void testOuterReplacePodcastSearchResultsUpdatesExistingPodcastSearchResults() {
        @Nullable final PodcastSearchIdentified podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1")).orNull();
        @Nullable final PodcastSearchIdentified podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2")).orNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        } else if (podcastSearchIdentified2 == null) {
            fail();
        } else {
            final Object marker1 = new Object();
            getTestObject().replacePodcastSearchPodcasts(
                    marker1,
                    podcastSearchIdentified1,
                    new PodcastList(
                            podcast11,
                            podcast2,
                            podcast3
                    )
            );
            final Object marker2 = new Object();
            getTestObject().replacePodcastSearchPodcasts(
                    marker2,
                    podcastSearchIdentified1,
                    new PodcastList(
                            podcast12,
                            podcast2,
                            podcast4
                    )
            );

            final MatcherTestObserver<PodcastIdentifiedList> podcastIdentifiedsTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastIdentifiedsTestObserver1);
            final MatcherTestObserver<PodcastIdentifiedList> podcastIdentifiedsTestObserver2 =
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

            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsMarkedValueTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastIdentifiedsMarkedValueTestObserver1);
            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsMarkedValueTestObserver2 =
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
    }

    @Test
    public void testOuterReplacePodcastSearchResultsCanIncludePodcastsFromOtherSearchResults() {
        @Nullable final PodcastSearchIdentified podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1")).orNull();
        @Nullable final PodcastSearchIdentified podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2")).orNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        } else if (podcastSearchIdentified2 == null) {
            fail();
        } else {
            final Object marker1 = new Object();
            getTestObject().replacePodcastSearchPodcasts(
                    marker1,
                    podcastSearchIdentified1,
                    new PodcastList(
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
                    new PodcastList(
                            podcast5,
                            podcast4,
                            podcast3,
                            podcast2
                    )
            );

            final MatcherTestObserver<PodcastIdentifiedList> podcastTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastTestObserver1);

            final MatcherTestObserver<PodcastIdentifiedList> podcastTestObserver2 =
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

            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsMarkedValueTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastIdentifiedsMarkedValueTestObserver1);
            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsMarkedValueTestObserver2 =
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
    }

    @Test
    public void testDeletePodcastSearchKeepsOtherSearchResultsWithSamePodcasts() {
        @Nullable final PodcastSearchIdentified podcastSearchIdentified1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1")).orNull();
        @Nullable final PodcastSearchIdentified podcastSearchIdentified2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2")).orNull();
        if (podcastSearchIdentified1 == null) {
            fail();
        } else if (podcastSearchIdentified2 == null) {
            fail();
        } else {
            final Object marker1 = new Object();
            getTestObject().replacePodcastSearchPodcasts(
                    marker1,
                    podcastSearchIdentified1,
                    new PodcastList(
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
                    new PodcastList(
                            podcast5,
                            podcast4,
                            podcast3,
                            podcast2
                    )
            );

            final MatcherTestObserver<PodcastIdentifiedList> podcastTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastTestObserver1);

            final MatcherTestObserver<PodcastIdentifiedList> podcastTestObserver2 =
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

            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsMarkedValueTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeMarkedQueryForPodcastIdentifieds(podcastSearchIdentified1.identifier)
                    .subscribe(podcastIdentifiedsMarkedValueTestObserver1);
            final MatcherTestObserver<MarkedValue<Object, PodcastIdentifiedList>> podcastIdentifiedsMarkedValueTestObserver2 =
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
    }

    @Test
    public void testDeletePodcastDeletesEpisodes() {
        @Nullable final PodcastIdentified podcastIdentified1 =
                getTestObject().upsertPodcast(podcast1).orNull();
        @Nullable final PodcastIdentified podcastIdentified2 =
                getTestObject().upsertPodcast(podcast2).orNull();
        if (podcastIdentified1 == null) {
            fail();
        } else if (podcastIdentified2 == null) {
            fail();
        } else {
            getTestObject().upsertEpisodesForPodcast(
                    podcastIdentified1.identifier,
                    new EpisodeList(
                            episode11,
                            episode12,
                            episode13
                    )
            );
            getTestObject().upsertEpisodesForPodcast(
                    podcastIdentified2.identifier,
                    new EpisodeList(
                            episode21,
                            episode22,
                            episode23
                    )
            );

            final MatcherTestObserver<EpisodeIdentifiedList> episodeTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentified1.identifier)
                    .subscribe(episodeTestObserver1);

            final MatcherTestObserver<EpisodeIdentifiedList> episodeTestObserver2 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentified2.identifier)
                    .subscribe(episodeTestObserver2);

            episodeTestObserver1.assertValueSequenceThat(
                    containsInOrder(
                            containsInOrder(
                                    identifiedModel(episode11),
                                    identifiedModel(episode12),
                                    identifiedModel(episode13)
                            )
                    )
            );

            episodeTestObserver2.assertValueSequenceThat(
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

            episodeTestObserver1.assertValueSequenceThat(
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

            episodeTestObserver2.assertValueSequenceThat(
                    containsInOrder(
                            containsInOrder(
                                    identifiedModel(episode21),
                                    identifiedModel(episode22),
                                    identifiedModel(episode23)
                            ),
                            specificallyEmpty()
                    )
            );
        }
    }

    @Test
    public void testDeletePodcastDeletesSubscription() {
        @Nullable final PodcastIdentified podcastIdentified1 =
                getTestObject().upsertPodcast(podcast1).orNull();
        @Nullable final PodcastIdentified podcastIdentified2 =
                getTestObject().upsertPodcast(podcast2).orNull();
        @Nullable final PodcastIdentified podcastIdentified3 =
                getTestObject().upsertPodcast(podcast3).orNull();
        if (podcastIdentified1 == null) {
            fail();
        } else if (podcastIdentified2 == null) {
            fail();
        } else if (podcastIdentified3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionIdentified subscriptionIdentified1 =
                    getTestObject().subscribe(podcastIdentified1).orNull();
            @Nullable final SubscriptionIdentified subscriptionIdentified2 =
                    getTestObject().subscribe(podcastIdentified2).orNull();
            @Nullable final SubscriptionIdentified subscriptionIdentified3 =
                    getTestObject().subscribe(podcastIdentified3).orNull();
            if (subscriptionIdentified1 == null) {
                fail();
            } else if (subscriptionIdentified2 == null) {
                fail();
            } else if (subscriptionIdentified3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionIdentifiedList> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValueSequence(
                        Collections.singletonList(
                                new SubscriptionIdentifiedList(
                                        subscriptionIdentified1,
                                        subscriptionIdentified2,
                                        subscriptionIdentified3
                                )
                        )
                );

                getTestObject().deletePodcast(podcastIdentified1.identifier);

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionIdentifiedList(
                                        subscriptionIdentified1,
                                        subscriptionIdentified2,
                                        subscriptionIdentified3
                                ),
                                new SubscriptionIdentifiedList(
                                        subscriptionIdentified2,
                                        subscriptionIdentified3
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testSubscribeUnsubscribeSubscribeUnsubscribeUpdatesSubscriptionIdentifierQuery() {
        @Nullable final PodcastIdentified podcastIdentified =
                getTestObject().upsertPodcast(podcast1).orElse(null);
        if (podcastIdentified == null) {
            fail();
        } else {
            final TestObserver<Optional<SubscriptionIdentifier>> subscriptionIdentifierTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptionIdentifier(podcastIdentified.identifier)
                    .subscribe(subscriptionIdentifierTestObserver);

            @Nullable final SubscriptionIdentifier subscriptionIdentifier1 =
                    getTestObject().subscribe(podcastIdentified.identifier).orElse(null);
            if (subscriptionIdentifier1 == null) {
                fail();
            } else {
                final Result result1 = getTestObject()
                        .unsubscribe(subscriptionIdentifier1);
                if (result1.reduce(
                        success -> false,
                        failure -> true
                )) {
                    fail();
                } else {
                    @Nullable final SubscriptionIdentifier subscriptionIdentifier2 =
                            getTestObject().subscribe(podcastIdentified.identifier).orElse(null);
                    if (subscriptionIdentifier2 == null) {
                        fail();
                    } else {
                        final Result result2 = getTestObject()
                                .unsubscribe(subscriptionIdentifier2);
                        if (result2.reduce(
                                success -> false,
                                failure -> true
                        )) {
                            fail();
                        } else {
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
                }
            }
        }
    }
}