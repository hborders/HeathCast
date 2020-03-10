package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.EpisodeIdentifiedSet;
import com.github.hborders.heathcast.models.EpisodeIdentifier;
import com.github.hborders.heathcast.models.EpisodeList;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.reactivex.MatcherTestObserver;
import com.github.hborders.heathcast.util.DateUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class EpisodeTableTest extends AbstractDatabaseTest<Object> {

    private EpisodeTable<Object> getTestObject() {
        return getDatabase().episodeTable;
    }

    private PodcastTable<Object> getPodcastTable() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testInsertEpisodeWithAllNonnulls() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode = new Episode(
                    new URL("http://example.com/episode/artwork1"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedSet(
                                new EpisodeIdentified(
                                        episodeIdentifier,
                                        episode
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testInsertEpisodeWithAllNullables() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode = new Episode(
                    null,
                    null,
                    null,
                    null,
                    "title1",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedSet(
                                new EpisodeIdentified(
                                        episodeIdentifier,
                                        episode
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testUpdateEpisodeFromAllNonnullsToAllNullables() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode/artwork1"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final Episode episode2 = new Episode(
                        null,
                        null,
                        null,
                        null,
                        "title2",
                        new URL("http://example.com/episode")
                );

                final int updatedRowCount = getTestObject().updateEpisodeIdentified(
                        podcastIdentifier,
                        new EpisodeIdentified(
                                episodeIdentifier,
                                episode2
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedSet(
                                new EpisodeIdentified(
                                        episodeIdentifier,
                                        episode2
                                )
                        )
                );
            }
        }
    }

    @Test
    public void updateEpisodeFromAllNullablesToNonnulls() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    null,
                    null,
                    null,
                    null,
                    "title1",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final Episode episode2 = new Episode(
                        new URL("http://example.com/episode/artwork2"),
                        Duration.ofSeconds(2),
                        DateUtil.from(
                                2019,
                                2,
                                2
                        ),
                        "summary2",
                        "title2",
                        new URL("http://example.com/episode")
                );

                final int updatedRowCount = getTestObject().updateEpisodeIdentified(
                        podcastIdentifier,
                        new EpisodeIdentified(
                                episodeIdentifier,
                                episode2
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedSet(
                                new EpisodeIdentified(
                                        episodeIdentifier,
                                        episode2
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testUpsertThreeNewEpisodesInsertsThemInOrder() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getPodcastTable().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode1/artwork"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode1")
            );
            final Episode episode2 = new Episode(
                    new URL("http://example.com/episode2/artwork"),
                    Duration.ofSeconds(2),
                    DateUtil.from(
                            2019,
                            2,
                            2
                    ),
                    "summary2",
                    "title12",
                    new URL("http://example.com/episode2")
            );
            final Episode episode3 = new Episode(
                    new URL("http://example.com/episode3/artwork"),
                    Duration.ofSeconds(3),
                    DateUtil.from(
                            2019,
                            3,
                            3
                    ),
                    "summary3",
                    "title3",
                    new URL("http://example.com/episode3")
            );
            final Episode episode4 = new Episode(
                    new URL("http://example.com/episode4/artwork"),
                    Duration.ofSeconds(4),
                    DateUtil.from(
                            2019,
                            4,
                            4
                    ),
                    "summary4",
                    "title4",
                    new URL("http://example.com/episode4")
            );

            final EpisodeList upsertingEpisodes = new EpisodeList(
                    episode1,
                    episode2,
                    episode3,
                    episode4
            );
            final List<Optional<EpisodeIdentifier>> episodeIdentifierOptionals =
                    getTestObject().upsertEpisodes(
                            podcastIdentifier1,
                            upsertingEpisodes
                    );
            if (episodeIdentifierOptionals.size() != upsertingEpisodes.size()) {
                fail("Expected " + upsertingEpisodes.size() + ", got episodeIdentifiers: " + episodeIdentifierOptionals);
            } else {
                @Nullable final EpisodeIdentifier episodeIdentifier1 = episodeIdentifierOptionals.get(0).orElse(null);
                @Nullable final EpisodeIdentifier episodeIdentifier2 = episodeIdentifierOptionals.get(1).orElse(null);
                @Nullable final EpisodeIdentifier episodeIdentifier3 = episodeIdentifierOptionals.get(2).orElse(null);
                @Nullable final EpisodeIdentifier episodeIdentifier4 = episodeIdentifierOptionals.get(3).orElse(null);
                if (episodeIdentifier1 == null) {
                    fail();
                } else if (episodeIdentifier2 == null) {
                    fail();
                } else if (episodeIdentifier3 == null) {
                    fail();
                } else if (episodeIdentifier4 == null) {
                    fail();
                } else {
                    final TestObserver<EpisodeIdentifiedList> episodeTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier1)
                            .subscribe(episodeTestObserver);

                    episodeTestObserver.assertValue(
                            new EpisodeIdentifiedList(
                                    new EpisodeIdentified(
                                            episodeIdentifier1,
                                            episode1
                                    ),
                                    new EpisodeIdentified(
                                            episodeIdentifier2,
                                            episode2
                                    ),
                                    new EpisodeIdentified(
                                            episodeIdentifier3,
                                            episode3
                                    ),
                                    new EpisodeIdentified(
                                            episodeIdentifier4,
                                            episode4
                                    )
                            )
                    );
                }
            }
        }
    }

    @Test
    public void testUpsertNewEpisodeWithSameURLThriceTogetherInsertsFirstEpisodeAndOthers() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getPodcastTable().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode1/artwork"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode")
            );
            final Episode episode2 = new Episode(
                    new URL("http://example.com/episode2/artwork"),
                    Duration.ofSeconds(2),
                    DateUtil.from(
                            2019,
                            2,
                            2
                    ),
                    "summary2",
                    "title12",
                    new URL("http://example.com/episode")
            );
            final Episode episode3 = new Episode(
                    new URL("http://example.com/episode3/artwork"),
                    Duration.ofSeconds(3),
                    DateUtil.from(
                            2019,
                            3,
                            3
                    ),
                    "summary3",
                    "title3",
                    new URL("http://example.com/episode")
            );

            final EpisodeList upsertingEpisodes = new EpisodeList(
                    episode1,
                    episode2,
                    episode3
            );
            final List<Optional<EpisodeIdentifier>> episodeIdentifierOptionals =
                    getTestObject().upsertEpisodes(
                            podcastIdentifier1,
                            upsertingEpisodes
                    );
            if (episodeIdentifierOptionals.size() != upsertingEpisodes.size()) {
                fail("Expected " + upsertingEpisodes.size() + ", got episodeIdentifiers: " + episodeIdentifierOptionals);
            } else {
                @Nullable final EpisodeIdentifier episodeIdentifier1 =
                        episodeIdentifierOptionals.get(0).orElse(null);
                if (episodeIdentifier1 == null) {
                    fail();
                } else {
                    assertThat(
                            episodeIdentifierOptionals,
                            equalTo(
                                    Collections.nCopies(
                                            upsertingEpisodes.size(),
                                            Optional.of(episodeIdentifier1)
                                    )
                            )
                    );

                    final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForAllEpisodeIdentifieds()
                            .subscribe(episodeTestObserver);

                    episodeTestObserver.assertValue(
                            new EpisodeIdentifiedSet(
                                    new EpisodeIdentified(
                                            episodeIdentifier1,
                                            episode1
                                    )
                            )
                    );
                }
            }
        }
    }

    @Test
    public void testUpsertNewEpisodeWithSameURLThriceTogetherAndExistingEpisodeWithSameURLThriceTogetherInsertsFirstOfNewAndUpdatesFirstOfExisting() throws Exception {
        @Nullable final PodcastIdentifier existingPodcastIdentifier =
                getPodcastTable().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork1"),
                                "author1",
                                new URL("http://example.com/feedB"),
                                "name1"
                        )
                ).orElse(null);
        if (existingPodcastIdentifier == null) {
            fail();
        } else {
            final List<Optional<EpisodeIdentifier>> existingEpisodeIdentifiers =
                    getTestObject().upsertEpisodes(
                            existingPodcastIdentifier,
                            new EpisodeList(
                                    new Episode(
                                            new URL("http://example.com/episodeB/artwork1"),
                                            Duration.ofSeconds(1),
                                            DateUtil.from(
                                                    2019,
                                                    1,
                                                    1
                                            ),
                                            "summary1",
                                            "title1",
                                            new URL("http://example.com/episodeB")
                                    )
                            )
                    );
            if (existingEpisodeIdentifiers.size() != 1) {
                fail("Expected 1 identifier, but received: " + existingEpisodeIdentifiers);
            } else {
                final @Nullable EpisodeIdentifier existingEpisodeIdentifier =
                        existingEpisodeIdentifiers.get(0).orElse(null);
                if (existingEpisodeIdentifier == null) {
                    fail();
                } else {
                    final Episode episode2 = new Episode(
                            new URL("http://example.com/episodeA/artwork2"),
                            Duration.ofSeconds(2),
                            DateUtil.from(
                                    2019,
                                    2,
                                    2
                            ),
                            "summary2",
                            "title2",
                            new URL("http://example.com/episodeA")
                    );
                    final Episode episode3 = new Episode(
                            new URL("http://example.com/episodeA/artwork3"),
                            Duration.ofSeconds(3),
                            DateUtil.from(
                                    2019,
                                    3,
                                    3
                            ),
                            "summary3",
                            "title3",
                            new URL("http://example.com/episodeA")
                    );
                    final Episode episode4 = new Episode(
                            new URL("http://example.com/episodeA/artwork4"),
                            Duration.ofSeconds(4),
                            DateUtil.from(
                                    2019,
                                    4,
                                    4
                            ),
                            "summary4",
                            "title4",
                            new URL("http://example.com/episodeA")
                    );
                    final Episode episode5 = new Episode(
                            new URL("http://example.com/episodeB/artwork5"),
                            Duration.ofSeconds(5),
                            DateUtil.from(
                                    2019,
                                    5,
                                    5
                            ),
                            "summary5",
                            "title5",
                            new URL("http://example.com/episodeB")
                    );
                    final Episode episode6 = new Episode(
                            new URL("http://example.com/episodeB/artwork6"),
                            Duration.ofSeconds(6),
                            DateUtil.from(
                                    2019,
                                    6,
                                    6
                            ),
                            "summary6",
                            "title6",
                            new URL("http://example.com/episodeB")
                    );
                    final Episode episode7 = new Episode(
                            new URL("http://example.com/episodeB/artwork7"),
                            Duration.ofSeconds(7),
                            DateUtil.from(
                                    2019,
                                    7,
                                    7
                            ),
                            "summary7",
                            "title7",
                            new URL("http://example.com/episodeB")
                    );
                    final EpisodeList upsertingEpisodes = new EpisodeList(
                            episode2,
                            episode3,
                            episode4,
                            episode5,
                            episode6,
                            episode7
                    );
                    final List<Optional<EpisodeIdentifier>> upsertedEpisodeIdentifierOptionals =
                            getTestObject().upsertEpisodes(
                                    existingPodcastIdentifier,
                                    upsertingEpisodes
                            );
                    if (upsertedEpisodeIdentifierOptionals.size() != upsertingEpisodes.size()) {
                        fail("Expected " + upsertingEpisodes.size() + " podcast identifiers, but got: " + upsertedEpisodeIdentifierOptionals);
                    } else {
                        @Nullable final EpisodeIdentifier insertedEpisodeIdentifier =
                                upsertedEpisodeIdentifierOptionals.get(0).orElse(null);
                        if (insertedEpisodeIdentifier == null) {
                            fail("Expected " + upsertingEpisodes.size() + " podcast identifiers, but got: " + upsertedEpisodeIdentifierOptionals);
                        } else {
                            assertThat(
                                    upsertedEpisodeIdentifierOptionals,
                                    equalTo(
                                            Arrays.asList(
                                                    Optional.of(insertedEpisodeIdentifier),
                                                    Optional.of(insertedEpisodeIdentifier),
                                                    Optional.of(insertedEpisodeIdentifier),
                                                    Optional.of(existingEpisodeIdentifier),
                                                    Optional.of(existingEpisodeIdentifier),
                                                    Optional.of(existingEpisodeIdentifier)
                                            )
                                    )
                            );

                            final TestObserver<EpisodeIdentifiedList> episodeTestObserver = new TestObserver<>();
                            getTestObject()
                                    .observeQueryForEpisodeIdentifiedsForPodcast(existingPodcastIdentifier)
                                    .subscribe(episodeTestObserver);

                            episodeTestObserver.assertValue(
                                    new EpisodeIdentifiedList(
                                            new EpisodeIdentified(
                                                    existingEpisodeIdentifier,
                                                    episode5
                                            ),
                                            new EpisodeIdentified(
                                                    insertedEpisodeIdentifier,
                                                    episode2
                                            )
                                    )
                            );
                        }
                    }
                }
            }
        }
    }

    public void testUpdateExistingEpisode() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode11 = new Episode(
                    new URL("http://example.com/episode/artwork11"),
                    Duration.ofSeconds(11),
                    DateUtil.from(
                            2019,
                            11,
                            11
                    ),
                    "summary11",
                    "title11",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final Episode episode12 = new Episode(
                        new URL("http://example.com/episode/artwork12"),
                        Duration.ofSeconds(12),
                        DateUtil.from(
                                2019,
                                12,
                                12
                        ),
                        "summary12",
                        "title12",
                        new URL("http://example.com/episode")
                );

                final int updatedRowCount = getTestObject().updateEpisodeIdentified(
                        podcastIdentifier,
                        new EpisodeIdentified(
                                episodeIdentifier,
                                episode12
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedSet(
                                new EpisodeIdentified(
                                        episodeIdentifier,
                                        episode12
                                )
                        )
                );
            }
        }
    }

    public void testUpdateMissingEpisode() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode11 = new Episode(
                    new URL("http://example.com/episode/artwork11"),
                    Duration.ofSeconds(11),
                    DateUtil.from(
                            2019,
                            11,
                            11
                    ),
                    "summary11",
                    "title11",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                getTestObject().deleteEpisode(episodeIdentifier);

                final Episode episode12 = new Episode(
                        new URL("http://example.com/episode/artwork12"),
                        Duration.ofSeconds(12),
                        DateUtil.from(
                                2019,
                                12,
                                12
                        ),
                        "summary12",
                        "title12",
                        new URL("http://example.com/episode")
                );

                final int updatedRowCount = getTestObject().updateEpisodeIdentified(
                        podcastIdentifier,
                        new EpisodeIdentified(
                                episodeIdentifier,
                                episode12
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(0)
                );
            }
        }
    }

    @Test
    public void testObserveQueryForEpisodeIdentified() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode11 = new Episode(
                    new URL("http://example.com/episode1/artwork11"),
                    Duration.ofSeconds(11),
                    DateUtil.from(
                            2019,
                            11,
                            11
                    ),
                    "summary11",
                    "title11",
                    new URL("http://example.com/episode1")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElse(null);

            final Episode episode2 = new Episode(
                    new URL("http://example.com/episode2/artwork"),
                    Duration.ofSeconds(2),
                    DateUtil.from(
                            2019,
                            2,
                            2
                    ),
                    "summary2",
                    "title2",
                    new URL("http://example.com/episode2")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElse(null);
            if (episodeIdentifier1 == null) {
                fail();
            } else if (episodeIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<Optional<EpisodeIdentified>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentified(episodeIdentifier1)
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValueSequence(
                        Collections.singletonList(
                                Optional.of(
                                        new EpisodeIdentified(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                )
                        )
                );

                final Episode episode12 = new Episode(
                        new URL("http://example.com/episode/artwork12"),
                        Duration.ofSeconds(12),
                        DateUtil.from(
                                2019,
                                12,
                                12
                        ),
                        "summary12",
                        "title12",
                        new URL("http://example.com/episode")
                );

                getTestObject().updateEpisodeIdentified(
                        podcastIdentifier,
                        new EpisodeIdentified(
                                episodeIdentifier1,
                                episode12
                        )
                );

                episodeTestObserver.assertValueSequence(
                        Arrays.asList(
                                Optional.of(
                                        new EpisodeIdentified(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                ),
                                Optional.of(
                                        new EpisodeIdentified(
                                                episodeIdentifier1,
                                                episode12
                                        )
                                )
                        )
                );

                getTestObject().deleteEpisode(episodeIdentifier1);

                episodeTestObserver.assertValueSequence(
                        Arrays.asList(
                                Optional.of(
                                        new EpisodeIdentified(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                ),
                                Optional.of(
                                        new EpisodeIdentified(
                                                episodeIdentifier1,
                                                episode12
                                        )
                                ),
                                Optional.empty()
                        )
                );
            }
        }
    }

    @Test
    public void testObserveQueryForEpisodeIdentifiedsForPodcast() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final MatcherTestObserver<EpisodeIdentifiedList> episodeTestObserver = new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                    .subscribe(episodeTestObserver);

            episodeTestObserver.assertValueSequenceThat(
                    containsInOrder(
                            containsNothing()
                    )
            );

            final Episode episode11 = new Episode(
                    new URL("http://example.com/episode1/artwork11"),
                    Duration.ofSeconds(11),
                    DateUtil.from(
                            2019,
                            11,
                            11
                    ),
                    "summary11",
                    "title11",
                    new URL("http://example.com/episode1")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElse(null);
            if (episodeIdentifier1 == null) {
                fail();
            } else {
                episodeTestObserver.assertValueSequenceThat(
                        containsInOrder(
                                containsNothing(),
                                containsInOrder(
                                        identifiedModel(episode11)
                                )
                        )
                );

                final Episode episode2 = new Episode(
                        new URL("http://example.com/episode2/artwork"),
                        Duration.ofSeconds(2),
                        DateUtil.from(
                                2019,
                                2,
                                2
                        ),
                        "summary2",
                        "title2",
                        new URL("http://example.com/episode2")
                );
                @Nullable final EpisodeIdentifier episodeIdentifier2 =
                        getTestObject().insertEpisode(
                                podcastIdentifier,
                                episode2
                        ).orElse(null);
                if (episodeIdentifier2 == null) {
                    fail();
                } else {
                    episodeTestObserver.assertValueSequenceThat(
                            containsInOrder(
                                    containsNothing(),
                                    containsInOrder(
                                            identifiedModel(episode11)
                                    ),
                                    containsInOrder(
                                            identifiedModel(episode11),
                                            identifiedModel(episode2)
                                    )
                            )
                    );

                    final Episode episode3 = new Episode(
                            new URL("http://example.com/episode3/artwork"),
                            Duration.ofSeconds(3),
                            DateUtil.from(
                                    2019,
                                    3,
                                    3
                            ),
                            "summary3",
                            "title3",
                            new URL("http://example.com/episode3")
                    );
                    @Nullable final EpisodeIdentifier episodeIdentifier3 =
                            getTestObject().insertEpisode(
                                    podcastIdentifier,
                                    episode3
                            ).orElse(null);
                    if (episodeIdentifier3 == null) {
                        fail();
                    } else {
                        episodeTestObserver.assertValueSequenceThat(
                                containsInOrder(
                                        containsNothing(),
                                        containsInOrder(
                                                identifiedModel(episode11)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode11),
                                                identifiedModel(episode2)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode11),
                                                identifiedModel(episode2),
                                                identifiedModel(episode3)
                                        )
                                )
                        );

                        final Episode episode12 = new Episode(
                                new URL("http://example.com/episode/artwork12"),
                                Duration.ofSeconds(12),
                                DateUtil.from(
                                        2019,
                                        12,
                                        12
                                ),
                                "summary12",
                                "title12",
                                new URL("http://example.com/episode")
                        );

                        getTestObject().updateEpisodeIdentified(
                                podcastIdentifier,
                                new EpisodeIdentified(
                                        episodeIdentifier1,
                                        episode12
                                )
                        );

                        episodeTestObserver.assertValueSequenceThat(
                                containsInOrder(
                                        containsNothing(),
                                        containsInOrder(
                                                identifiedModel(episode11)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode11),
                                                identifiedModel(episode2)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode11),
                                                identifiedModel(episode2),
                                                identifiedModel(episode3)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode12),
                                                identifiedModel(episode2),
                                                identifiedModel(episode3)
                                        )
                                )
                        );

                        getTestObject().deleteEpisode(episodeIdentifier1);

                        episodeTestObserver.assertValueSequenceThat(
                                containsInOrder(
                                        containsNothing(),
                                        containsInOrder(
                                                identifiedModel(episode11)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode11),
                                                identifiedModel(episode2)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode11),
                                                identifiedModel(episode2),
                                                identifiedModel(episode3)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode12),
                                                identifiedModel(episode2),
                                                identifiedModel(episode3)
                                        ),
                                        containsInOrder(
                                                identifiedModel(episode2),
                                                identifiedModel(episode3)
                                        )
                                )
                        );
                    }
                }
            }
        }
    }

    @Test
    public void testDeleteEpisode() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode1/artwork"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode1")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElse(null);

            final Episode episode2 = new Episode(
                    new URL("http://example.com/episode2/artwork"),
                    Duration.ofSeconds(2),
                    DateUtil.from(
                            2019,
                            2,
                            2
                    ),
                    "summary2",
                    "title2",
                    new URL("http://example.com/episode2")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElse(null);
            if (episodeIdentifier1 == null) {
                fail();
            } else if (episodeIdentifier2 == null) {
                fail();
            } else {
                final int deletedEpisodeCount = getTestObject().deleteEpisode(episodeIdentifier1);
                assertThat(
                        deletedEpisodeCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeIdentifiedList> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedList(
                                new EpisodeIdentified(
                                        episodeIdentifier2,
                                        episode2
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testDeleteEpisodes() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode1/artwork"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode1")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElse(null);

            final Episode episode2 = new Episode(
                    new URL("http://example.com/episode2/artwork"),
                    Duration.ofSeconds(2),
                    DateUtil.from(
                            2019,
                            2,
                            2
                    ),
                    "summary2",
                    "title2",
                    new URL("http://example.com/episode2")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElse(null);

            final Episode episode3 = new Episode(
                    new URL("http://example.com/episode3/artwork"),
                    Duration.ofSeconds(3),
                    DateUtil.from(
                            2019,
                            3,
                            3
                    ),
                    "summary3",
                    "title3",
                    new URL("http://example.com/episode3")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier3 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode3
                    ).orElse(null);
            if (episodeIdentifier1 == null) {
                fail();
            } else if (episodeIdentifier2 == null) {
                fail();
            } else if (episodeIdentifier3 == null) {
                fail();
            } else {
                final int deletedEpisodeCount = getTestObject().deleteEpisodes(
                        Arrays.asList(
                                episodeIdentifier1,
                                episodeIdentifier2
                        )
                );
                assertThat(
                        deletedEpisodeCount,
                        equalTo(2)
                );

                final TestObserver<EpisodeIdentifiedList> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedList(
                                new EpisodeIdentified(
                                        episodeIdentifier3,
                                        episode3
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testUpsertTheSameEpisodesForMultiplePodcasts() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastIdentifier podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode1/artwork"),
                    Duration.ofSeconds(1),
                    DateUtil.from(
                            2019,
                            1,
                            1
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode1")
            );
            @Nullable final EpisodeIdentifier episodeIdentifier11 =
                    getTestObject().insertEpisode(
                            podcastIdentifier1,
                            episode1
                    ).orElse(null);
            @Nullable final EpisodeIdentifier episodeIdentifier21 =
                    getTestObject().insertEpisode(
                            podcastIdentifier2,
                            episode1
                    ).orElse(null);
            if (episodeIdentifier11 == null) {
                fail();
            } else if (episodeIdentifier21 == null) {
                fail();
            } else {
                final TestObserver<EpisodeIdentifiedSet> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        new EpisodeIdentifiedSet(
                                new EpisodeIdentified(
                                        episodeIdentifier11,
                                        episode1
                                ),
                                new EpisodeIdentified(
                                        episodeIdentifier21,
                                        episode1
                                )
                        )
                );
            }
        }
    }
}
