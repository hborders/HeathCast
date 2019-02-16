package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastTableTest extends AbstractDatabaseTest {

    private PodcastTable getTestObject() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testInsertPodcast() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getTestObject().insertPodcast(podcast);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier,
                                    podcast
                            )
                    )
            );
        }
    }

    @Test
    public void testInsertPodcastTwiceThrowsAndDoesntModifyPodcasts() throws Exception {
        Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1);
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            @Nullable final Identifier<Podcast> podcastIdentifier2 =
                    ((Callable<Identifier<Podcast>>) () -> {
                        final Podcast podcast2 = new Podcast(
                                new URL("http://example.com/artwork2"),
                                "author2",
                                new URL("http://example.com/feed"),
                                "name2"
                        );
                        try {
                            return getTestObject().insertPodcast(podcast2);
                        } catch (Throwable t) {
                            return null;
                        }
                    }).call();

            assertNull(podcastIdentifier2);

            final TestObserver<Set<Identified<Podcast>>> allPodcastsTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(allPodcastsTestObserver);

            allPodcastsTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier1,
                                    podcast1
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertNewPodcastInsertsPodcast() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getTestObject().upsertPodcast(podcast);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier,
                                    podcast
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingPodcastWithSameFeedUpdatesPodcast() throws Exception {
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getTestObject().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork"),
                                "author",
                                new URL("http://example.com/feed"),
                                "name"
                        )
                );
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Podcast podcast2 = new Podcast(
                    new URL("http://example.com/artwork2"),
                    "author2",
                    new URL("http://example.com/feed"),
                    "name2"
            );
            @Nullable final Identifier<Podcast> podcastIdentifier2 =
                    getTestObject().upsertPodcast(podcast2);
            assertEquals(
                    podcastIdentifier1,
                    podcastIdentifier2
            );

            final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier1,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingPodcastWithSameFeedTwiceTogetherUpdatesPodcastWithFirstPodcast() throws Exception {
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getTestObject().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork"),
                                "author",
                                new URL("http://example.com/feed"),
                                "name"
                        )
                );
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Podcast podcast2 = new Podcast(
                    new URL("http://example.com/artwork2"),
                    "author2",
                    new URL("http://example.com/feed"),
                    "name2"
            );
            final Podcast podcast3 = new Podcast(
                    new URL("http://example.com/artwork3"),
                    "author3",
                    new URL("http://example.com/feed"),
                    "name3"
            );
            final List<Optional<Identifier<Podcast>>> upsertedPodcastIdentifiers =
                    getTestObject().upsertPodcasts(
                            Arrays.asList(
                                    podcast2,
                                    podcast3
                            )
                    );
            assertEquals(
                    Arrays.asList(
                            Optional.of(podcastIdentifier1),
                            Optional.of(podcastIdentifier1)
                    ),
                    upsertedPodcastIdentifiers
            );

            final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier1,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertNewPodcastWithSameFeedTwiceTogetherInsertsFirstPodcastAndIgnoresSecond() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed"),
                "name2"
        );
        final List<Optional<Identifier<Podcast>>> upsertedPodcastIdentifiers =
                getTestObject().upsertPodcasts(
                        Arrays.asList(
                                podcast1,
                                podcast2
                        )
                );
        if (upsertedPodcastIdentifiers.size() == 2) {
            @Nullable final Identifier<Podcast> podcastIdentifier =
                    upsertedPodcastIdentifiers.get(0).orElse(null);
            if (podcastIdentifier != null) {
                assertEquals(
                        Arrays.asList(
                                Optional.of(podcastIdentifier),
                                Optional.of(podcastIdentifier)
                        ),
                        upsertedPodcastIdentifiers
                );

                final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllPodcasts()
                        .subscribe(podcastTestObserver);

                podcastTestObserver.assertValue(
                        Collections.singleton(
                                new Identified<>(
                                        podcastIdentifier,
                                        podcast1
                                )
                        )
                );
            } else {
                fail("Expected 2 identifiers, but got: " + upsertedPodcastIdentifiers);
            }
        } else {
            fail("Expected 2 identifiers, but got: " + upsertedPodcastIdentifiers);
        }
    }

    @Test
    public void testUpsertNewPodcastWithSameFeedTwiceTogetherAndExistingPodcastWithSameFeedTwiceTogetherInsertsFirstOfNewPairAndUpdatesFirstOfExistingPair() throws Exception {
        @Nullable final Identifier<Podcast> existingPodcastIdentifier =
                getTestObject().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork1"),
                                "author1",
                                new URL("http://example.com/feedB"),
                                "name1"
                        )
                );
        if (existingPodcastIdentifier == null) {
            fail();
        } else {
            final Podcast podcast2 = new Podcast(
                    new URL("http://example.com/artwork2"),
                    "author2",
                    new URL("http://example.com/feedA"),
                    "name2"
            );
            final Podcast podcast3 = new Podcast(
                    new URL("http://example.com/artwork3"),
                    "author3",
                    new URL("http://example.com/feedA"),
                    "name3"
            );
            final Podcast podcast4 = new Podcast(
                    new URL("http://example.com/artwork4"),
                    "author4",
                    new URL("http://example.com/feedB"),
                    "name4"
            );
            final Podcast podcast5 = new Podcast(
                    new URL("http://example.com/artwork5"),
                    "author5",
                    new URL("http://example.com/feedB"),
                    "name5"
            );
            final List<Optional<Identifier<Podcast>>> upsertedPodcastIdentifiers =
                    getTestObject().upsertPodcasts(
                            Arrays.asList(
                                    podcast2,
                                    podcast3,
                                    podcast4,
                                    podcast5
                            )
                    );
            if (!upsertedPodcastIdentifiers.isEmpty()) {
                @Nullable final Identifier<Podcast> insertedPodcastIdentifier =
                        upsertedPodcastIdentifiers.get(0).orElse(null);
                if (insertedPodcastIdentifier != null) {
                    assertEquals(
                            Arrays.asList(
                                    Optional.of(insertedPodcastIdentifier),
                                    Optional.of(insertedPodcastIdentifier),
                                    Optional.of(existingPodcastIdentifier),
                                    Optional.of(existingPodcastIdentifier)
                            ),
                            upsertedPodcastIdentifiers
                    );

                    final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForAllPodcasts()
                            .subscribe(podcastTestObserver);

                    podcastTestObserver.assertValue(
                            new HashSet<>(
                                    Arrays.asList(
                                            new Identified<>(
                                                    insertedPodcastIdentifier,
                                                    podcast2
                                            ),
                                            new Identified<>(
                                                    existingPodcastIdentifier,
                                                    podcast4
                                            )
                                    )
                            )
                    );
                } else {
                    fail("Expected 4 podcast identifiers, but got: " + upsertedPodcastIdentifiers);
                }
            } else {
                fail("Expected 4 podcast identifiers, but got: " + upsertedPodcastIdentifiers);
            }
        }
    }

    public void testUpdateExistingPodcast() throws Exception {
        final Podcast podcast11 = new Podcast(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11);
        if (podcastIdentifier11 == null) {
            fail();
        } else {

            final Podcast podcast12 = new Podcast(
                    new URL("http://example.com/artwork12"),
                    "author12",
                    new URL("http://example.com/feed11"),
                    "name12"
            );
            final int updatedRowCount = getTestObject().updateIdentifiedPodcast(new Identified<>(
                            podcastIdentifier11,
                            podcast12
                    )
            );
            assertEquals(
                    1,
                    updatedRowCount
            );

            final TestObserver<Optional<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcast(podcastIdentifier11)
                    .subscribe(podcastTestObserver);
            podcastTestObserver.assertValueSequence(
                    Collections.singleton(
                            Optional.of(
                                    new Identified<>(
                                            podcastIdentifier11,
                                            podcast12
                                    )
                            )
                    )
            );
        }
    }

    public void testUpdateMissingPodcast() throws Exception {
        final Podcast podcast11 = new Podcast(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11);
        if (podcastIdentifier11 == null) {
            fail();
        } else {
            getTestObject().deletePodcast(podcastIdentifier11);

            final Podcast podcast12 = new Podcast(
                    new URL("http://example.com/artwork12"),
                    "author12",
                    new URL("http://example.com/feed11"),
                    "name12"
            );
            final int updatedRowCount = getTestObject().updateIdentifiedPodcast(new Identified<>(
                            podcastIdentifier11,
                            podcast12
                    )
            );
            assertEquals(
                    0,
                    updatedRowCount
            );
        }
    }

    @Test
    public void testObserveQueryForPodcast() throws Exception {
        final Podcast podcast11 = new Podcast(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2);
        if (podcastIdentifier11 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final TestObserver<Optional<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcast(podcastIdentifier11)
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastIdentifier11,
                                    podcast11
                            )
                    )
            );

            final Podcast podcast12 = new Podcast(
                    new URL("http://example.com/artwork12"),
                    "author12",
                    new URL("http://example.com/feed11"),
                    "name12"
            );
            getTestObject().updateIdentifiedPodcast(new Identified<>(
                            podcastIdentifier11,
                            podcast12
                    )
            );

            podcastTestObserver.assertValueSequence(
                    Arrays.asList(
                            Optional.of(
                                    new Identified<>(
                                            podcastIdentifier11,
                                            podcast11
                                    )
                            ),
                            Optional.of(
                                    new Identified<>(
                                            podcastIdentifier11,
                                            podcast12
                                    )
                            )
                    )
            );

            getTestObject().deletePodcast(podcastIdentifier11);

            podcastTestObserver.assertValueSequence(
                    Arrays.asList(
                            Optional.of(
                                    new Identified<>(
                                            podcastIdentifier11,
                                            podcast11
                                    )
                            ),
                            Optional.of(
                                    new Identified<>(
                                            podcastIdentifier11,
                                            podcast12
                                    )
                            ),
                            Optional.empty()
                    )
            );
        }
    }

    @Test
    public void testDeletePodcast() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2);
        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcast(podcastIdentifier1);
            assertEquals(
                    1,
                    deleteCount
            );

            final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier2,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testDeletePodcasts() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getTestObject().insertPodcast(podcast3);
        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcasts(
                    Arrays.asList(
                            podcastIdentifier1,
                            podcastIdentifier2
                    )
            );
            assertEquals(
                    2,
                    deleteCount
            );

            final TestObserver<Set<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcasts()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastIdentifier3,
                                    podcast3
                            )
                    )
            );
        }
    }
}
