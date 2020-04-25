package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.SubscriptionImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import io.reactivex.rxjava3.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class SubscriptionTableTest extends AbstractDatabaseTest<Object> {

    private SubscriptionTable<
            Object,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifierImpl,
            SubscriptionImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl
            > getTestObject() {
        return getDatabase().subscriptionTable;
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
    public void testInsertSubscription() throws Exception {
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
            final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptions()
                    .subscribe(subscriptionTestObserver);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier,
                                                new SubscriptionImpl(
                                                        new PodcastImpl.PodcastIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        final PodcastImpl podcast4 = new PodcastImpl(
                new URL("http://example.com/artwork4"),
                "author4",
                new URL("http://example.com/feed4"),
                "name4"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier4 =
                getPodcastTable().upsertPodcast(podcast4).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else if (podcastIdentifier4 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier4 =
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
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final int deleteCount = getTestObject().deleteSubscription(subscriptionIdentifier2);
                assertThat(
                        deleteCount,
                        equalTo(1)
                );

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                final SubscriptionImpl subscription4 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier4,
                                podcast4
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier4,
                                                subscription4
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValue(
                        new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                        subscriptionIdentifier1,
                                        subscription1
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                        subscriptionIdentifier2,
                                        subscription2
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
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

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getPodcastTable().upsertPodcast(podcast3).orElseNull();

        if (podcastIdentifier1 == null) {
            fail();
        } else if (podcastIdentifier2 == null) {
            fail();
        } else if (podcastIdentifier3 == null) {
            fail();
        } else {
            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier1 =
                    getTestObject().insertSubscription(podcastIdentifier1).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier2 =
                    getTestObject().insertSubscription(podcastIdentifier2).orElse(null);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier3 =
                    getTestObject().insertSubscription(podcastIdentifier3).orElse(null);

            if (subscriptionIdentifier1 == null) {
                fail();
            } else if (subscriptionIdentifier2 == null) {
                fail();
            } else if (subscriptionIdentifier3 == null) {
                fail();
            } else {
                final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                        new TestObserver<>();
                getTestObject()
                        .observeQueryForSubscriptions()
                        .subscribe(subscriptionTestObserver);

                final SubscriptionImpl subscription1 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                );
                final SubscriptionImpl subscription2 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                );
                final SubscriptionImpl subscription3 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                );
                subscriptionTestObserver.assertValue(
                        new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                        subscriptionIdentifier1,
                                        subscription1
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                        subscriptionIdentifier2,
                                        subscription2
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl(
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
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier3,
                                                subscription3
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier1,
                                                subscription1
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier2,
                                                subscription2
                                        ),
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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
        final PodcastImpl podcast11 = new PodcastImpl(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed1"),
                "name11"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getPodcastTable().upsertPodcast(podcast11).orElseNull();
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptions()
                    .subscribe(subscriptionTestObserver);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                final PodcastImpl podcast12 = new PodcastImpl(
                        new URL("http://example.com/artwork12"),
                        "author12",
                        new URL("http://example.com/feed1"),
                        "name12"
                );
                final PodcastImpl.PodcastIdentifiedImpl podcastIdentified12 = new PodcastImpl.PodcastIdentifiedImpl(
                        podcastIdentifier,
                        podcast12
                );
                getPodcastTable().updatePodcastIdentified(
                        podcastIdentified12
                );

                final SubscriptionImpl subscription11 = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast11
                        )
                );
                final SubscriptionImpl subscription12 = new SubscriptionImpl(
                        podcastIdentified12
                );

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier,
                                                subscription11
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
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
            final TestObserver<SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl> subscriptionTestObserver =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptions()
                    .subscribe(subscriptionTestObserver);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier =
                    getTestObject().insertSubscription(podcastIdentifier).orElse(null);

            if (subscriptionIdentifier == null) {
                fail();
            } else {
                getPodcastTable().deletePodcast(podcastIdentifier);

                final SubscriptionImpl subscription = new SubscriptionImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast
                        )
                );

                subscriptionTestObserver.assertValueSequence(
                        Arrays.asList(
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl(
                                        new SubscriptionImpl.SubscriptionIdentifiedImpl(
                                                subscriptionIdentifier,
                                                subscription
                                        )
                                ),
                                new SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl()
                        )
                );
            }
        }
    }

    @Test
    public void testInsertingAndDeletingSubscriptionUpdatesSubscriptionIdentifierQuery() throws Exception {
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
            final TestObserver<
                    Optional<
                            SubscriptionImpl.SubscriptionIdentifierImpl
                            >
                    > subscriptionTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptionIdentifier(podcastIdentifier)
                    .subscribe(subscriptionTestObserver);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier =
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
            final TestObserver<
                    Optional<
                            SubscriptionImpl.SubscriptionIdentifierImpl
                            >
                    > subscriptionTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForSubscriptionIdentifier(podcastIdentifier)
                    .subscribe(subscriptionTestObserver);

            @Nullable final SubscriptionImpl.SubscriptionIdentifierImpl subscriptionIdentifier =
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
