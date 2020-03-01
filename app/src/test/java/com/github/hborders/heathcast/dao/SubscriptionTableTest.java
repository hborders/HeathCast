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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class SubscriptionTableTest extends AbstractDatabaseTest<Object> {

    private SubscriptionTable<Object> getTestObject() {
        return getDatabase().subscriptionTable;
    }

    private PodcastTable<Object> getPodcastTable() {
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
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptions()
                    .subscribe(subscriptionTestObserver);

            @Nullable final Identifier<Subscription> subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Collections.emptyList(),
                                Collections.singletonList(
                                        new Identified<>(
                                                subscriptionIdentifier,
                                                new Subscription(
                                                        new Identified<>(
                                                                podcastIdentifier,
                                                                podcast
                                                        )
                                                )
                                        )
                                )
                        )
                );
            }
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        final Podcast podcast4 = new Podcast(
                new URL("http://example.com/artwork4"),
                "author4",
                new URL("http://example.com/feed4"),
                "name4"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier4 =
                getPodcastTable().upsertPodcast(podcast4).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else if (podcastIdentifier4 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier4 =
                    getTestObject().insertSubscription(podcastIdentifier4).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else if (subscriptionIdentifier4 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int deleteCount = getTestObject().deleteSubscription(subscriptionIdentifier2);
                assertThat(
                        deleteCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                final Subscription subscription4 = new Subscription(
                        new Identified<>(
                                podcastIdentifier4,
                                podcast4
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier4,
                                                subscription4
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier4,
                                                subscription4
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                new Identified<>(
                                        subscriptionIdentifier1,
                                        subscription1
                                ),
                                new Identified<>(
                                        subscriptionIdentifier2,
                                        subscription2
                                ),
                                new Identified<>(
                                        subscriptionIdentifier3,
                                        subscription3
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier1,
                        subscriptionIdentifier1
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier1,
                        subscriptionIdentifier2
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier2,
                        subscriptionIdentifier1
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier2,
                        subscriptionIdentifier1
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedBefore(
                        subscriptionIdentifier3,
                        subscriptionIdentifier2
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        )

                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier1,
                        subscriptionIdentifier2
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();

                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier2,
                        subscriptionIdentifier3
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        )
                                )
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
                getPodcastTable().upsertPodcast(podcast1).orElse(null);

        final Podcast podcast2 = new Podcast(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier2 =
                getPodcastTable().upsertPodcast(podcast2).orElse(null);

        final Podcast podcast3 = new Podcast(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElse(null);

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<Subscription> subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final Identifier<Subscription> subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final Subscription subscription1 = new Subscription(
                        new Identified<>(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final Subscription subscription2 = new Subscription(
                        new Identified<>(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final Subscription subscription3 = new Subscription(
                        new Identified<>(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValue(
                        Arrays.asList(
                                new Identified<>(
                                        subscriptionIdentifier1,
                                        subscription1
                                ),
                                new Identified<>(
                                        subscriptionIdentifier2,
                                        subscription2
                                ),
                                new Identified<>(
                                        subscriptionIdentifier3,
                                        subscription3
                                )
                        )
                );

                final int movedSubscriptionCount = getTestObject().moveSubscriptionIdentifiedAfter(
                        subscriptionIdentifier3,
                        subscriptionIdentifier3
                );
                assertThat(
                        movedSubscriptionCount,
                        equalTo(1)
                );

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                Arrays.asList(
                                        new Identified<>(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new Identified<>(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testUpdatingPodcastUpdatesSubscriptionQuery() throws Exception {
        final Podcast podcast11 = new Podcast(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed1"),
                "name11"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast11).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptions()
                    .subscribe(subscriptionTestObserver);

            @Nullable final Identifier<Subscription> subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                final Podcast podcast12 = new Podcast(
                        new URL("http://example.com/artwork12"),
                        "author12",
                        new URL("http://example.com/feed1"),
                        "name12"
                );
                final Identified<Podcast> podcastIdentified12 = new Identified<>(
                        podcastIdentifier,
                        podcast12
                );
                getPodcastTable().updatePodcastIdentified(
                        podcastIdentified12
                );

                final Subscription subscription11 = new Subscription(
                        new Identified<>(
                                podcastIdentifier,
                                podcast11
                        )
                );
                final Subscription subscription12 = new Subscription(
                        podcastIdentified12
                );

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Collections.emptyList(),
                                Collections.singletonList(
                                        new Identified<>(
                                                subscriptionIdentifier,
                                                subscription11
                                        )
                                ),
                                Collections.singletonList(
                                        new Identified<>(
                                                subscriptionIdentifier,
                                                subscription12
                                        )
                                )
                        )
                );
            }
        }
    }

    @Test
    public void testDeletingPodcastUpdatesSubscriptionQuery() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<List<Identified<Subscription>>> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptions()
                    .subscribe(subscriptionTestObserver);

            @Nullable final Identifier<Subscription> subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                getPodcastTable().deletePodcast(podcastIdentifier);

                final Subscription subscription = new Subscription(
                        new Identified<>(
                                podcastIdentifier,
                                podcast
                        )
                );

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Collections.emptyList(),
                                Collections.singletonList(
                                        new Identified<>(
                                                subscriptionIdentifier,
                                                subscription
                                        )
                                ),
                                Collections.emptyList()
                        )
                );
            }
        }
    }

    @Test
    public void testInsertingAndDeletingSubscriptionUpdatesSubscriptionIdentifierQuery() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<Optional<Identifier<Subscription>>> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptionIdentifier(podcastIdentifier)
                    .subscribe(subscriptionTestObserver);

            @Nullable final Identifier<Subscription> subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                getTestObject().deleteSubscription(subscriptionIdentifier);

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Optional.empty(),
                                Optional.of(subscriptionIdentifier),
                                Optional.empty()
                        )
                );
            }
        }
    }

    @Test
    public void testInsertingSubscriptionAndDeletingPodcastUpdatesSubscriptionIdentifierQuery() throws Exception {
        final Podcast podcast = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<Optional<Identifier<Subscription>>> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptionIdentifier(podcastIdentifier)
                    .subscribe(subscriptionTestObserver);

            @Nullable final Identifier<Subscription> subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                getPodcastTable().deletePodcast(podcastIdentifier);

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                Optional.empty(),
                                Optional.of(subscriptionIdentifier),
                                Optional.empty()
                        )
                );
            }
        }
    }
}
