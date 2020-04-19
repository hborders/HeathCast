package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.EpisodeIdentifiedSet;
import com.github.hborders.heathcast.models.EpisodeIdentifier;
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
import java.util.Optional;

import io.reactivex.observers.TestObserver;

import static com.github.hborders.heathcast.matchers.IdentifiedMatchers.identifiedModel;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsInOrder;
import static com.github.hborders.heathcast.matchers.IsIterableContainingInOrderUtil.containsNothing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class EpisodeTableTest extends AbstractDatabaseTest<Object> {

    private EpisodeTable<
            Object,
            EpisodeImpl,
            EpisodeImpl.EpisodeIdentifiedImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
            EpisodeImpl.EpisodeIdentifierImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
            EpisodeImpl.EpisodeListImpl,
            PodcastImpl.PodcastIdentifierImpl
            > getTestObject() {
        return getDatabase().episodeTable;
    }

    private PodcastTable<
            Object,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl
            > getPodcastTable() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testInsertEpisodeWithAllNonnulls() throws Exception {
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode
                    ).orElseNull();
            if (episodeIdentifier == null) {
                fail();
            } else {
                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode = new EpisodeImpl(
                    null,
                    null,
                    null,
                    null,
                    "title1",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode
                    ).orElseNull();
            if (episodeIdentifier == null) {
                fail();
            } else {
                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElseNull();
            if (episodeIdentifier == null) {
                fail();
            } else {
                final EpisodeImpl episode2 = new EpisodeImpl(
                        null,
                        null,
                        null,
                        null,
                        "title2",
                        new URL("http://example.com/episode")
                );

                final int updatedRowCount = getTestObject().updateEpisodeIdentified(
                        podcastIdentifier,
                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                episodeIdentifier,
                                episode2
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
                    null,
                    null,
                    null,
                    null,
                    "title1",
                    new URL("http://example.com/episode")
            );
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElseNull();
            if (episodeIdentifier == null) {
                fail();
            } else {
                final EpisodeImpl episode2 = new EpisodeImpl(
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
                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                episodeIdentifier,
                                episode2
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getPodcastTable().insertPodcast(podcast1).orElseNull();
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
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
            final EpisodeImpl episode2 = new EpisodeImpl(
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
            final EpisodeImpl episode3 = new EpisodeImpl(
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
            final EpisodeImpl episode4 = new EpisodeImpl(
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

            final EpisodeImpl.EpisodeListImpl upsertingEpisodes = new EpisodeImpl.EpisodeListImpl(
                    episode1,
                    episode2,
                    episode3,
                    episode4
            );
            final EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl episodeIdentifierOpts =
                    getTestObject().upsertEpisodes(
                            podcastIdentifier1,
                            upsertingEpisodes
                    );
            if (episodeIdentifierOpts.size() != upsertingEpisodes.size()) {
                fail("Expected " + upsertingEpisodes.size() + ", got episodeIdentifiers: " + episodeIdentifierOpts);
            } else {
                @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier1 = episodeIdentifierOpts.get(0).orElseNull();
                @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier2 = episodeIdentifierOpts.get(1).orElseNull();
                @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier3 = episodeIdentifierOpts.get(2).orElseNull();
                @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier4 = episodeIdentifierOpts.get(3).orElseNull();
                if (episodeIdentifier1 == null) {
                    fail();
                } else if (episodeIdentifier2 == null) {
                    fail();
                } else if (episodeIdentifier3 == null) {
                    fail();
                } else if (episodeIdentifier4 == null) {
                    fail();
                } else {
                    final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl> episodeTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier1)
                            .subscribe(episodeTestObserver);

                    episodeTestObserver.assertValue(
                            new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl(
                                    new EpisodeImpl.EpisodeIdentifiedImpl(
                                            episodeIdentifier1,
                                            episode1
                                    ),
                                    new EpisodeImpl.EpisodeIdentifiedImpl(
                                            episodeIdentifier2,
                                            episode2
                                    ),
                                    new EpisodeImpl.EpisodeIdentifiedImpl(
                                            episodeIdentifier3,
                                            episode3
                                    ),
                                    new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getPodcastTable().insertPodcast(podcast1).orElseNull();
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
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
            final EpisodeImpl episode2 = new EpisodeImpl(
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
            final EpisodeImpl episode3 = new EpisodeImpl(
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

            final EpisodeImpl.EpisodeListImpl upsertingEpisodes = new EpisodeImpl.EpisodeListImpl(
                    episode1,
                    episode2,
                    episode3
            );
            final EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl episodeIdentifierOpts =
                    getTestObject().upsertEpisodes(
                            podcastIdentifier1,
                            upsertingEpisodes
                    );
            if (episodeIdentifierOpts.size() != upsertingEpisodes.size()) {
                fail("Expected " + upsertingEpisodes.size() + ", got episodeIdentifiers: " + episodeIdentifierOpts);
            } else {
                @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier1 =
                        episodeIdentifierOpts.get(0).orElseNull();
                if (episodeIdentifier1 == null) {
                    fail();
                } else {
                    assertThat(
                            episodeIdentifierOpts,
                            equalTo(
                                    Collections.nCopies(
                                            upsertingEpisodes.size(),
                                            new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(episodeIdentifier1)
                                    )
                            )
                    );

                    final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForAllEpisodeIdentifieds()
                            .subscribe(episodeTestObserver);

                    episodeTestObserver.assertValue(
                            new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                    new EpisodeImpl.EpisodeIdentifiedImpl(
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
        @Nullable final PodcastImpl.PodcastIdentifierImpl existingPodcastIdentifier =
                getPodcastTable().insertPodcast(
                        new PodcastImpl(
                                new URL("http://example.com/artwork1"),
                                "author1",
                                new URL("http://example.com/feedB"),
                                "name1"
                        )
                ).orElseNull();
        if (existingPodcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl existingEpisodeIdentifierOpts =
                    getTestObject().upsertEpisodes(
                            existingPodcastIdentifier,
                            new EpisodeImpl.EpisodeListImpl(
                                    new EpisodeImpl(
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
            if (existingEpisodeIdentifierOpts.size() != 1) {
                fail("Expected 1 identifier, but received: " + existingEpisodeIdentifierOpts);
            } else {
                final @Nullable EpisodeImpl.EpisodeIdentifierImpl existingEpisodeIdentifier =
                        existingEpisodeIdentifierOpts.get(0).orElseNull();
                if (existingEpisodeIdentifier == null) {
                    fail();
                } else {
                    final EpisodeImpl episode2 = new EpisodeImpl(
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
                    final EpisodeImpl episode3 = new EpisodeImpl(
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
                    final EpisodeImpl episode4 = new EpisodeImpl(
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
                    final EpisodeImpl episode5 = new EpisodeImpl(
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
                    final EpisodeImpl episode6 = new EpisodeImpl(
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
                    final EpisodeImpl episode7 = new EpisodeImpl(
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
                    final EpisodeImpl.EpisodeListImpl upsertingEpisodes = new EpisodeImpl.EpisodeListImpl(
                            episode2,
                            episode3,
                            episode4,
                            episode5,
                            episode6,
                            episode7
                    );
                    final EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl upsertedEpisodeIdentifierOpts =
                            getTestObject().upsertEpisodes(
                                    existingPodcastIdentifier,
                                    upsertingEpisodes
                            );
                    if (upsertedEpisodeIdentifierOpts.size() != upsertingEpisodes.size()) {
                        fail("Expected " + upsertingEpisodes.size() + " podcast identifiers, but got: " + upsertedEpisodeIdentifierOpts);
                    } else {
                        @Nullable final EpisodeImpl.EpisodeIdentifierImpl insertedEpisodeIdentifier =
                                upsertedEpisodeIdentifierOpts.get(0).orElseNull();
                        if (insertedEpisodeIdentifier == null) {
                            fail("Expected " + upsertingEpisodes.size() + " podcast identifiers, but got: " + upsertedEpisodeIdentifierOpts);
                        } else {
                            assertThat(
                                    upsertedEpisodeIdentifierOpts,
                                    equalTo(
                                            Arrays.asList(
                                                    new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(insertedEpisodeIdentifier),
                                                    new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(insertedEpisodeIdentifier),
                                                    new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(insertedEpisodeIdentifier),
                                                    new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(existingEpisodeIdentifier),
                                                    new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(existingEpisodeIdentifier),
                                                    new EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl(existingEpisodeIdentifier)
                                            )
                                    )
                            );

                            final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl> episodeTestObserver = new TestObserver<>();
                            getTestObject()
                                    .observeQueryForEpisodeIdentifiedsForPodcast(existingPodcastIdentifier)
                                    .subscribe(episodeTestObserver);

                            episodeTestObserver.assertValue(
                                    new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl(
                                            new EpisodeImpl.EpisodeIdentifiedImpl(
                                                    existingEpisodeIdentifier,
                                                    episode5
                                            ),
                                            new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode11 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElseNull();
            if (episodeIdentifier == null) {
                fail();
            } else {
                final EpisodeImpl episode12 = new EpisodeImpl(
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
                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                episodeIdentifier,
                                episode12
                        )
                );
                assertThat(
                        updatedRowCount,
                        equalTo(1)
                );

                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
                                        episodeIdentifier,
                                        episode12
                                )
                        )
                );
            }
        }
    }

    public void testUpdateMissingEpisode() throws Exception {
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode11 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElseNull();
            if (episodeIdentifier == null) {
                fail();
            } else {
                getTestObject().deleteEpisode(episodeIdentifier);

                final EpisodeImpl episode12 = new EpisodeImpl(
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
                        new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode11 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElseNull();

            final EpisodeImpl episode2 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElseNull();
            if (episodeIdentifier1 == null) {
                fail();
            } else if (episodeIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<
                        Optional<
                                EpisodeImpl.EpisodeIdentifiedImpl
                                >
                        > episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentified(episodeIdentifier1)
                        .subscribe(episodeTestObserver);

                episodeTestObserver.assertValueSequence(
                        Collections.singletonList(
                                Optional.of(
                                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                )
                        )
                );

                final EpisodeImpl episode12 = new EpisodeImpl(
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
                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                episodeIdentifier1,
                                episode12
                        )
                );

                episodeTestObserver.assertValueSequence(
                        Arrays.asList(
                                Optional.of(
                                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                ),
                                Optional.of(
                                        new EpisodeImpl.EpisodeIdentifiedImpl(
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
                                        new EpisodeImpl.EpisodeIdentifiedImpl(
                                                episodeIdentifier1,
                                                episode11
                                        )
                                ),
                                Optional.of(
                                        new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final MatcherTestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl> episodeTestObserver = new MatcherTestObserver<>();
            getTestObject()
                    .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                    .subscribe(episodeTestObserver);

            episodeTestObserver.assertValueSequenceThat(
                    containsInOrder(
                            containsNothing()
                    )
            );

            final EpisodeImpl episode11 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode11
                    ).orElseNull();
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

                final EpisodeImpl episode2 = new EpisodeImpl(
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
                @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier2 =
                        getTestObject().insertEpisode(
                                podcastIdentifier,
                                episode2
                        ).orElseNull();
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

                    final EpisodeImpl episode3 = new EpisodeImpl(
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
                    @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier3 =
                            getTestObject().insertEpisode(
                                    podcastIdentifier,
                                    episode3
                            ).orElseNull();
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

                        final EpisodeImpl episode12 = new EpisodeImpl(
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
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElseNull();

            final EpisodeImpl episode2 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElseNull();
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

                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier1 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode1
                    ).orElseNull();

            final EpisodeImpl episode2 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier2 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode2
                    ).orElseNull();

            final EpisodeImpl episode3 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier3 =
                    getTestObject().insertEpisode(
                            podcastIdentifier,
                            episode3
                    ).orElseNull();
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

                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier)
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
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
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1).orElseNull();

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final EpisodeImpl episode1 = new EpisodeImpl(
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
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier11 =
                    getTestObject().insertEpisode(
                            podcastIdentifier1,
                            episode1
                    ).orElseNull();
            @Nullable final EpisodeImpl.EpisodeIdentifierImpl episodeIdentifier21 =
                    getTestObject().insertEpisode(
                            podcastIdentifier2,
                            episode1
                    ).orElseNull();
            if (episodeIdentifier11 == null) {
                fail();
            } else if (episodeIdentifier21 == null) {
                fail();
            } else {
                final TestObserver<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl> episodeTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllEpisodeIdentifieds()
                        .subscribe(episodeTestObserver);
                episodeTestObserver.assertValue(
                        new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl(
                                new EpisodeImpl.EpisodeIdentifiedImpl(
                                        episodeIdentifier11,
                                        episode1
                                ),
                                new EpisodeImpl.EpisodeIdentifiedImpl(
                                        episodeIdentifier21,
                                        episode1
                                )
                        )
                );
            }
        }
    }
}
