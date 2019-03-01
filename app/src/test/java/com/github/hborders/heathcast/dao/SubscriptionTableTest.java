package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.Subscription;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class SubscriptionTableTest extends AbstractDatabaseTest {

    private SubscriptionTable getTestObject() {
        return getDatabase().subscriptionTable;
    }

    private PodcastTable getPodcastTable() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testInsertSubscription() throws Exception {
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
            final Subscription subscription = new Subscription(
                    new Identified<>(
                            podcastIdentifier,
                            podcast
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier =
                    getTestObject().insertSubscription(subscription).orElse(null);
            assertThat(
                    subscriptionIdentifier,
                    notNullValue()
            );
        }
    }

    @Test
    public void testDeleteSubscription() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        final Podcast podcast4 = new Podcast(
                new URL("http://example.com/artwork4"),
                "author4",
                new URL("http://example.com/feed4"),
                "name4"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier4 =
                getPodcastTable().upsertPodcast(podcast4);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else if (podcastIdentifier4 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            final Subscription subscription4 = new Subscription(
                    new Identified<>(
                            podcastIdentifier4,
                            podcast4
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier4 =
                    getTestObject().insertSubscription(subscription4).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else if (subscriptionIdentifier4 == null) {
                fail();
            } else {
                final int deleteCount = getTestObject().deleteSubscription(subscriptionIdentifier2);
                assertThat(
                        deleteCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier1,
                                subscriptionIdentifier3,
                                subscriptionIdentifier4
                        )
                );
            }
        }
    }

    @Test
    public void testInsertedSubscriptionInitiallyOrderedAtEnd() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier1,
                                subscriptionIdentifier2,
                                subscriptionIdentifier3
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionToBeginningWhileAtBeginning() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier1,
                        subscriptionIdentifier1
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier1,
                                subscriptionIdentifier2,
                                subscriptionIdentifier3
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionFromBeginningToEndWith2() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier1,
                        subscriptionIdentifier2
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier2,
                                subscriptionIdentifier1
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionFromEndToBeginningWith2() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier2,
                        subscriptionIdentifier1
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier2,
                                subscriptionIdentifier1
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionFromMiddleToBeginning() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier2,
                        subscriptionIdentifier1
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier2,
                                subscriptionIdentifier1,
                                subscriptionIdentifier3
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionToMiddleWithBefore() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier3,
                        subscriptionIdentifier2
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier1,
                                subscriptionIdentifier3,
                                subscriptionIdentifier2
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionToMiddleWithAfter() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier1,
                        subscriptionIdentifier2
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier2,
                                subscriptionIdentifier1,
                                subscriptionIdentifier3
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionToEnd() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier2,
                        subscriptionIdentifier3
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier1,
                                subscriptionIdentifier3,
                                subscriptionIdentifier2
                        )
                );
            }
        }
    }

    @Test
    public void testMoveSubscriptionToEndWhileAtEnd() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().upsertPodcast(podcast1);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            final Subscription subscription1 = new Subscription(
                    new Identified<>(
                            podcastIdentifier1,
                            podcast1
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(subscription1).orElse(null);

            final Subscription subscription2 = new Subscription(
                    new Identified<>(
                            podcastIdentifier2,
                            podcast2
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(subscription2).orElse(null);

            final Subscription subscription3 = new Subscription(
                    new Identified<>(
                            podcastIdentifier3,
                            podcast3
                    )
            );
            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(subscription3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier3,
                        subscriptionIdentifier3
                );
                assertThat(
                        movedSubscriptionCount,
                        is(1)
                );

                final TestObserver<List<Identifier<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForAllSubscriptionIdentifiers()
                        .subscribe(subscriptionTestObserver);

                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                subscriptionIdentifier1,
                                subscriptionIdentifier2,
                                subscriptionIdentifier3
                        )
                );
            }
        }
    }
}
