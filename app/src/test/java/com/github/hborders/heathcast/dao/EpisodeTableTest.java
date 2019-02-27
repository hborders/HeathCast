package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
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
import java.util.Set;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class EpisodeTableTest extends AbstractDatabaseTest {

    private EpisodeTable getTestObject() {
        return getDatabase().episodeTable;
    }

    private PodcastTable getPodcastTable() {
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        Collections.singleton(
                                new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode
                    ).orElse(null);
            if (episodeIdentifier == null) {
                fail();
            } else {
                final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        Collections.singleton(
                                new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier =
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
                        new Identified<>(
                                episodeIdentifier,
                                episode2
                        )
                );
                assertThat(
                        updatedRowCount,
                        is(1)
                );

                final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        Collections.singleton(
                                new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier =
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
                        new Identified<>(
                                episodeIdentifier,
                                episode2
                        )
                );
                assertThat(
                        updatedRowCount,
                        is(1)
                );

                final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        Collections.singleton(
                                new Identified<>(
                                        episodeIdentifier,
                                        episode2
                                )
                        )
                );
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
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode11/artwork"),
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

            final List<Episode> upsertingEpisodes = Arrays.asList(
                    episode1,
                    episode2,
                    episode3
            );
            final List<Optional<Identifier<Episode>>> episodeIdentifierOptionals =
                    getTestObject().upsertEpisodes(
                            podcastIdentifier1,
                            upsertingEpisodes
                    );
            if (episodeIdentifierOptionals.size() != upsertingEpisodes.size()) {
                fail("Expected " + upsertingEpisodes.size() + ", got episodeIdentifiers: " + episodeIdentifierOptionals);
            } else {
                @Nullable final Identifier<Episode> episodeIdentifier1 =
                        episodeIdentifierOptionals.get(0).orElse(null);
                if (episodeIdentifier1 == null) {
                    fail();
                } else {
                    assertThat(
                            episodeIdentifierOptionals,
                            is(
                                    Collections.nCopies(
                                            upsertingEpisodes.size(),
                                            Optional.of(episodeIdentifier1)
                                    )
                            )
                    );

                    final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForAllEpisodeIdentifieds()
                            .subscribe(episodeTestObserver);

                    episodeTestObserver.assertValue(
                            Collections.singleton(
                                    new Identified<>(
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
        @Nullable final Identifier<Podcast> existingPodcastIdentifier =
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
            final List<Optional<Identifier<Episode>>> existingEpisodeIdentifiers =
                    getTestObject().upsertEpisodes(
                            existingPodcastIdentifier,
                            Collections.singletonList(
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
                final @Nullable Identifier<Episode> existingEpisodeIdentifier =
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
                    final List<Episode> upsertingEpisodes = Arrays.asList(
                            episode2,
                            episode3,
                            episode4,
                            episode5,
                            episode6,
                            episode7
                    );
                    final List<Optional<Identifier<Episode>>> upsertedEpisodeIdentifierOptionals =
                            getTestObject().upsertEpisodes(
                                    existingPodcastIdentifier,
                                    upsertingEpisodes
                            );
                    if (upsertedEpisodeIdentifierOptionals.size() != upsertingEpisodes.size()) {
                        fail("Expected " + upsertingEpisodes.size() + " podcast identifiers, but got: " + upsertedEpisodeIdentifierOptionals);
                    } else {
                        @Nullable final Identifier<Episode> insertedEpisodeIdentifier =
                                upsertedEpisodeIdentifierOptionals.get(0).orElse(null);
                        if (insertedEpisodeIdentifier == null) {
                            fail("Expected " + upsertingEpisodes.size() + " podcast identifiers, but got: " + upsertedEpisodeIdentifierOptionals);
                        } else {
                            assertThat(
                                    upsertedEpisodeIdentifierOptionals,
                                    is(
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

                            final TestObserver<List<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                            getTestObject()
                                    .observeQueryForEpisodeIdentifiedsForPodcast(existingPodcastIdentifier)
                                    .subscribe(episodeTestObserver);

                            episodeTestObserver.assertValue(
                                    Arrays.asList(
                                            new Identified<>(
                                                    existingEpisodeIdentifier,
                                                    episode5
                                            ),
                                            new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier =
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
                        new Identified<>(
                                episodeIdentifier,
                                episode12
                        )
                );
                assertThat(
                        updatedRowCount,
                        is(1)
                );

                final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        Collections.singleton(
                                new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier =
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
                        new Identified<>(
                                episodeIdentifier,
                                episode12
                        )
                );
                assertThat(
                        updatedRowCount,
                        is(0)
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier1 =
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
            @Nullable final Identifier<Episode> episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElse(null);
            if (episodeIdentifier1 == null) {
                fail();
            } else if (episodeIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<Optional<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentified(episodeIdentifier1)
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValueSequence(
                        Collections.singletonList(
                                Optional.of(
                                        new Identified<>(
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
                        new Identified<>(
                                episodeIdentifier1,
                                episode12
                        )
                );

                episodeTestObserver.assertValueSequence(
                        Arrays.asList(
                                Optional.of(
                                        new Identified<>(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                ),
                                Optional.of(
                                        new Identified<>(
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
                                        new Identified<>(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                ),
                                Optional.of(
                                        new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final MatcherTestObserver<List<Identified<Episode>>> episodeTestObserver = new MatcherTestObserver<>();
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
            @Nullable final Identifier<Episode> episodeIdentifier1 =
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
                @Nullable final Identifier<Episode> episodeIdentifier2 =
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
                    @Nullable final Identifier<Episode> episodeIdentifier3 =
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
                                new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier1 =
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
            @Nullable final Identifier<Episode> episodeIdentifier2 =
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
                        is(1)
                );

                final TestObserver<List<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        Collections.singletonList(
                                new Identified<>(
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
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast);
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
            @Nullable final Identifier<Episode> episodeIdentifier1 =
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
            @Nullable final Identifier<Episode> episodeIdentifier2 =
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
            @Nullable final Identifier<Episode> episodeIdentifier3 =
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
                        is(2)
                );

                final TestObserver<List<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        Collections.singletonList(
                                new Identified<>(
                                        episodeIdentifier3,
                                        episode3
                                )
                        )
                );
            }
        }
    }
}
