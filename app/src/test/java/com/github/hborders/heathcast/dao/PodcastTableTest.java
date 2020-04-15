package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedOpt;
import com.github.hborders.heathcast.models.PodcastIdentifiedSet;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.models.PodcastIdentifierOpt;
import com.github.hborders.heathcast.models.PodcastIdentifierOptList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastTableTest extends AbstractDatabaseTest<Object> {

    private PodcastTable<Object> getTestObject() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testInsertPodcastWithAllNonnulls() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getTestObject().upsertPodcast(podcast).orNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast
                            )
                    )
            );
        }
    }

    @Test
    public void testInsertPodcastWithAllNullables() throws Exception {
        final Podcast podcast = new Podcast(
                null,
                null,
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getTestObject().insertPodcast(podcast).orNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast
                            )
                    )
            );
        }
    }

    @Test
    public void testUpdatePodcastFromAllNonnullsToAllNullables() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getTestObject().insertPodcast(podcast1).orNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Podcast podcast2 = new Podcast(
                    null,
                    null,
                    new URL("http://example.com/feed"),
                    "name2"
            );
            @Nullable final int updatedRowCount =
                    getTestObject().updatePodcastIdentified(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast2
                            )
                    );
            assertThat(
                    updatedRowCount,
                    equalTo(1)
            );

            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void updatePodcastFromAllNullablesToNonnulls() throws Exception {
        final Podcast podcast1 = new Podcast(
                null,
                null,
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastIdentifier podcastIdentifier =
                getTestObject().insertPodcast(podcast1).orNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final Podcast podcast2 = new Podcast(
                    new URL("http://example.com/artwork2"),
                    "author2",
                    new URL("http://example.com/feed"),
                    "name2"
            );
            @Nullable final int updatedRowCount =
                    getTestObject().updatePodcastIdentified(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast2
                            )
                    );
            assertThat(
                    updatedRowCount,
                    equalTo(1)
            );

            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast2
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
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1).orNull();
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            @Nullable final PodcastIdentifier podcastIdentifier2 =
                    ((Callable<PodcastIdentifier>) () -> {
                        final Podcast podcast2 = new Podcast(
                                new URL("http://example.com/artwork2"),
                                "author2",
                                new URL("http://example.com/feed"),
                                "name2"
                        );
                        try {
                            return getTestObject().insertPodcast(podcast2).orNull();
                        } catch (Throwable t) {
                            return null;
                        }
                    }).call();

            assertNull(podcastIdentifier2);

            final TestObserver<PodcastIdentifiedSet> allPodcastsTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(allPodcastsTestObserver);

            allPodcastsTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
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
        @Nullable final PodcastIdentifier podcastIdentifier =
                getTestObject().upsertPodcast(podcast).orNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier,
                                    podcast
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingPodcastWithSameFeedUpdatesPodcast() throws Exception {
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getTestObject().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork"),
                                "author",
                                new URL("http://example.com/feed"),
                                "name"
                        )
                ).orNull();
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Podcast podcast2 = new Podcast(
                    new URL("http://example.com/artwork2"),
                    "author2",
                    new URL("http://example.com/feed"),
                    "name2"
            );
            @Nullable final PodcastIdentifier podcastIdentifier2 =
                    getTestObject().upsertPodcast(podcast2).orNull();
            assertThat(
                    podcastIdentifier2,
                    equalTo(podcastIdentifier1)
            );

            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier1,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingPodcastWithSameFeedTwiceTogetherUpdatesPodcastWithFirstPodcast() throws Exception {
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getTestObject().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork"),
                                "author",
                                new URL("http://example.com/feed"),
                                "name"
                        )
                ).orNull();
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
            final PodcastIdentifierOptList upsertedPodcastIdentifierOpts =
                    getTestObject().upsertPodcasts(
                            Arrays.asList(
                                    podcast2,
                                    podcast3
                            )
                    );
            assertThat(
                    upsertedPodcastIdentifierOpts,
                    equalTo(
                            new PodcastIdentifierOptList(
                                    new PodcastIdentifierOpt(podcastIdentifier1),
                                    new PodcastIdentifierOpt(podcastIdentifier1)
                            )
                    )
            );

            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier1,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertNewPodcastWithSameFeedThriceTogetherInsertsFirstPodcastAndOthers() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed"),
                "name1"
        );
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
        final List<Podcast> upsertingPodcasts = Arrays.asList(
                podcast1,
                podcast2,
                podcast3
        );
        final PodcastIdentifierOptList upsertedPodcastIdentifierOpts =
                getTestObject().upsertPodcasts(
                        upsertingPodcasts
                );
        if (upsertedPodcastIdentifierOpts.size() == upsertingPodcasts.size()) {
            @Nullable final PodcastIdentifier podcastIdentifier =
                    upsertedPodcastIdentifierOpts.get(0).orNull();
            if (podcastIdentifier != null) {
                assertThat(
                        upsertedPodcastIdentifierOpts,
                        equalTo(
                                new PodcastIdentifierOptList(
                                        Collections.nCopies(
                                                upsertingPodcasts.size(),
                                                new PodcastIdentifierOpt(podcastIdentifier)
                                        )
                                )
                        )
                );

                final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
                getTestObject()
                        .observeQueryForAllPodcastIdentifieds()
                        .subscribe(podcastTestObserver);

                podcastTestObserver.assertValue(
                        new PodcastIdentifiedSet(
                                new PodcastIdentified(
                                        podcastIdentifier,
                                        podcast1
                                )
                        )
                );
            } else {
                fail("Expected " + upsertingPodcasts.size() + " identifiers, but got: " + upsertedPodcastIdentifierOpts);
            }
        } else {
            fail("Expected " + upsertingPodcasts.size() + " identifiers, but got: " + upsertedPodcastIdentifierOpts);
        }
    }

    @Test
    public void testUpsertNewPodcastWithSameFeedThriceTogetherAndExistingPodcastWithSameFeedThriceTogetherInsertsFirstOfNewAndUpdatesFirstOfExisting() throws Exception {
        @Nullable final PodcastIdentifier existingPodcastIdentifier =
                getTestObject().insertPodcast(
                        new Podcast(
                                new URL("http://example.com/artwork1"),
                                "author1",
                                new URL("http://example.com/feedB"),
                                "name1"
                        )
                ).orNull();
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
                    new URL("http://example.com/feedA"),
                    "name4"
            );
            final Podcast podcast5 = new Podcast(
                    new URL("http://example.com/artwork5"),
                    "author5",
                    new URL("http://example.com/feedB"),
                    "name5"
            );
            final Podcast podcast6 = new Podcast(
                    new URL("http://example.com/artwork4"),
                    "author4",
                    new URL("http://example.com/feedB"),
                    "name4"
            );
            final Podcast podcast7 = new Podcast(
                    new URL("http://example.com/artwork5"),
                    "author5",
                    new URL("http://example.com/feedB"),
                    "name5"
            );
            final List<Podcast> upsertingPodcasts = Arrays.asList(
                    podcast2,
                    podcast3,
                    podcast4,
                    podcast5,
                    podcast6,
                    podcast7
            );
            final PodcastIdentifierOptList upsertedPodcastIdentifierOpts =
                    getTestObject().upsertPodcasts(
                            upsertingPodcasts
                    );
            if (upsertedPodcastIdentifierOpts.size() == upsertingPodcasts.size()) {
                @Nullable final PodcastIdentifier insertedPodcastIdentifier =
                        upsertedPodcastIdentifierOpts.get(0).orNull();
                if (insertedPodcastIdentifier != null) {
                    assertThat(
                            upsertedPodcastIdentifierOpts,
                            equalTo(
                                    new PodcastIdentifierOptList(
                                            new PodcastIdentifierOpt(insertedPodcastIdentifier),
                                            new PodcastIdentifierOpt(insertedPodcastIdentifier),
                                            new PodcastIdentifierOpt(insertedPodcastIdentifier),
                                            new PodcastIdentifierOpt(existingPodcastIdentifier),
                                            new PodcastIdentifierOpt(existingPodcastIdentifier),
                                            new PodcastIdentifierOpt(existingPodcastIdentifier)
                                    )
                            )
                    );

                    final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForAllPodcastIdentifieds()
                            .subscribe(podcastTestObserver);

                    podcastTestObserver.assertValue(
                            new PodcastIdentifiedSet(
                                    new PodcastIdentified(
                                            insertedPodcastIdentifier,
                                            podcast2
                                    ),
                                    new PodcastIdentified(
                                            existingPodcastIdentifier,
                                            podcast5
                                    )
                            )
                    );
                } else {
                    fail("Expected " + upsertingPodcasts.size() + " podcast identifiers, but got: " + upsertedPodcastIdentifierOpts);
                }
            } else {
                fail("Expected " + upsertingPodcasts.size() + " podcast identifiers, but got: " + upsertedPodcastIdentifierOpts);
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
        @Nullable final PodcastIdentifier podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11).orNull();
        if (podcastIdentifier11 == null) {
            fail();
        } else {

            final Podcast podcast12 = new Podcast(
                    new URL("http://example.com/artwork12"),
                    "author12",
                    new URL("http://example.com/feed11"),
                    "name12"
            );
            final int updatedRowCount = getTestObject().updatePodcastIdentified(new PodcastIdentified(
                            podcastIdentifier11,
                            podcast12
                    )
            );
            assertThat(
                    updatedRowCount,
                    equalTo(1)
            );

            final TestObserver<PodcastIdentifiedOpt> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentified(podcastIdentifier11)
                    .subscribe(podcastTestObserver);
            podcastTestObserver.assertValueSequence(
                    Collections.singleton(
                            new PodcastIdentifiedOpt(
                                    new PodcastIdentified(
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
        @Nullable final PodcastIdentifier podcastIdentifier =
                getTestObject().insertPodcast(podcast11).orNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            getTestObject().deletePodcast(podcastIdentifier);

            final Podcast podcast12 = new Podcast(
                    new URL("http://example.com/artwork12"),
                    "author12",
                    new URL("http://example.com/feed11"),
                    "name12"
            );
            final int updatedRowCount = getTestObject().updatePodcastIdentified(
                    new PodcastIdentified(
                            podcastIdentifier,
                            podcast12
                    )
            );
            assertThat(
                    updatedRowCount,
                    equalTo(0)
            );

            final TestObserver<PodcastIdentifiedOpt> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentified(podcastIdentifier)
                    .subscribe(podcastTestObserver);
            podcastTestObserver.assertValue(
                    PodcastIdentifiedOpt.EMPTY
            );
        }
    }

    @Test
    public void testObserveQueryForPodcastIdentified() throws Exception {
        final Podcast podcast11 = new Podcast(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final PodcastIdentifier podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11).orNull();

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastIdentifier podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2).orNull();
        if (podcastIdentifier11 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final TestObserver<PodcastIdentifiedOpt> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentified(podcastIdentifier11)
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedOpt(
                            new PodcastIdentified(
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
            getTestObject().updatePodcastIdentified(new PodcastIdentified(
                            podcastIdentifier11,
                            podcast12
                    )
            );

            podcastTestObserver.assertValueSequence(
                    Arrays.asList(
                            new PodcastIdentifiedOpt(
                                    new PodcastIdentified(
                                            podcastIdentifier11,
                                            podcast11
                                    )
                            ),
                            new PodcastIdentifiedOpt(
                                    new PodcastIdentified(
                                            podcastIdentifier11,
                                            podcast12
                                    )
                            )
                    )
            );

            getTestObject().deletePodcast(podcastIdentifier11);

            podcastTestObserver.assertValueSequence(
                    Arrays.asList(
                            new PodcastIdentifiedOpt(
                                    new PodcastIdentified(
                                            podcastIdentifier11,
                                            podcast11
                                    )
                            ),
                            new PodcastIdentifiedOpt(
                                    new PodcastIdentified(
                                            podcastIdentifier11,
                                            podcast12
                                    )
                            ),
                            PodcastIdentifiedOpt.EMPTY
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
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1).orNull();

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastIdentifier podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2).orNull();
        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcast(podcastIdentifier1);
            assertThat(
                    deleteCount,
                    equalTo(1)
            );

            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
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
        @Nullable final PodcastIdentifier podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1).orNull();

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastIdentifier podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2).orNull();

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastIdentifier podcastIdentifier3 =
                getTestObject().insertPodcast(podcast3).orNull();
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
            assertThat(
                    deleteCount,
                    equalTo(2)
            );

            final TestObserver<PodcastIdentifiedSet> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastIdentifiedSet(
                            new PodcastIdentified(
                                    podcastIdentifier3,
                                    podcast3
                            )
                    )
            );
        }
    }
}
