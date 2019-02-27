package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;
import com.github.hborders.heathcast.util.DateUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest extends AbstractDatabaseTest {

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

    private Database getTestObject() {
        return getDatabase();
    }

    @Test
    public void testOuterReplacePodcastSearchResultsForFirstPodcastSearch() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1"));
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2"));
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
                    .subscribe(podcastTestObserver1);
            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver2 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
                    .subscribe(podcastTestObserver2);


            podcastTestObserver1.assertValueThat(containsNothing());
            podcastTestObserver2.assertValueThat(containsNothing());

            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier1,
                    Arrays.asList(
                            podcast1,
                            podcast2,
                            podcast3
                    )
            );

            podcastTestObserver1.assertValueSequenceThat(
                    IsIterableContainingInOrderUtil.containsInOrder(
                            containsNothing(),
                            IsIterableContainingInOrderUtil.containsInOrder(
                                    identifiedModel(podcast1),
                                    identifiedModel(podcast2),
                                    identifiedModel(podcast3)
                            )
                    )
            );
            podcastTestObserver2.assertValueSequenceThat(
                    IsIterableContainingInOrderUtil.containsInOrder(
                            containsNothing(),
                            containsNothing()
                    )
            );
        }
    }

    @Test
    public void testOuterReplacePodcastSearchResultsUpdatesExistingPodcastSearchResults() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1"));
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2"));
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier1,
                    Arrays.asList(
                            podcast11,
                            podcast2,
                            podcast3
                    )
            );
            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier1,
                    Arrays.asList(
                            podcast12,
                            podcast2,
                            podcast4
                    )
            );

            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
                    .subscribe(podcastTestObserver1);
            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver2 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
                    .subscribe(podcastTestObserver2);

            podcastTestObserver1.assertValueSequenceThat(
                    IsIterableContainingInOrderUtil.containsInOrder(
                            IsIterableContainingInOrderUtil.containsInOrder(
                                    identifiedModel(podcast12),
                                    identifiedModel(podcast2),
                                    identifiedModel(podcast4)
                            )
                    )
            );
            podcastTestObserver2.assertValueThat(containsNothing());
        }
    }

    @Test
    public void testOuterReplacePodcastSearchResultsCanIncludePodcastsFromOtherSearchResults() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1"));
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2"));
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier1,
                    Arrays.asList(
                            podcast1,
                            podcast2,
                            podcast3,
                            podcast4
                    )
            );
            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier2,
                    Arrays.asList(
                            podcast5,
                            podcast4,
                            podcast3,
                            podcast2
                    )
            );

            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
                    .subscribe(podcastTestObserver1);

            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver2 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
                    .subscribe(podcastTestObserver2);

            podcastTestObserver1.assertValueSequenceThat(
                    IsIterableContainingInOrderUtil.containsInOrder(
                            containsInOrder(
                                    identifiedModel(podcast1),
                                    identifiedModel(podcast2),
                                    identifiedModel(podcast3),
                                    identifiedModel(podcast4)
                            )
                    )
            );
            podcastTestObserver2.assertValueSequenceThat(
                    IsIterableContainingInOrderUtil.containsInOrder(
                            containsInOrder(
                                    identifiedModel(podcast5),
                                    identifiedModel(podcast4),
                                    identifiedModel(podcast3),
                                    identifiedModel(podcast2)
                            )
                    )
            );
        }
    }

    @Test
    public void testDeletePodcastSearchKeepsOtherSearchResultsWithSamePodcasts() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search1"));
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(new PodcastSearch("Search2"));
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier1,
                    Arrays.asList(
                            podcast1,
                            podcast2,
                            podcast3,
                            podcast4
                    )
            );
            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier2,
                    Arrays.asList(
                            podcast5,
                            podcast4,
                            podcast3,
                            podcast2
                    )
            );

            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
                    .subscribe(podcastTestObserver1);

            final MatcherTestObserver<List<Identified<Podcast>>> podcastTestObserver2 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
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

            final boolean deleted = getTestObject().deletePodcastSearch(podcastSearchIdentifier2);
            assertThat(
                    deleted,
                    is(true)
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
                            containsNothing()
                    )
            );
        }
    }

    @Test
    public void testDeletePodcastDeletesEpisodes() {
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getTestObject().upsertPodcast(podcast1);
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getTestObject().upsertPodcast(podcast2);
        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            getTestObject().upsertEpisodesForPodcast(
                    podcastIdentifier1,
                    Arrays.asList(
                            episode11,
                            episode12,
                            episode13
                    )
            );
            getTestObject().upsertEpisodesForPodcast(
                    podcastIdentifier2,
                    Arrays.asList(
                            episode21,
                            episode22,
                            episode23
                    )
            );

            final MatcherTestObserver<List<Identified<Episode>>> episodeTestObserver1 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier1)
                    .subscribe(episodeTestObserver1);

            final MatcherTestObserver<List<Identified<Episode>>> episodeTestObserver2 =
                    new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier2)
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

            final boolean deleted = getTestObject().deletePodcast(podcastIdentifier2);
            assertThat(
                    deleted,
                    is(true)
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
                            containsNothing()
                    )
            );
        }
    }
}