package com.github.hborders.heathcast.dao;

import androidx.test.core.app.ApplicationProvider;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class DatabaseTest {
    private Database testObject;

    @Before
    public void setup() {
        testObject = new Database(
                ApplicationProvider.getApplicationContext(),
                null, // in-memory
                Schedulers.trampoline()
        );
    }

    @Test
    public void testInsertPodcastSearch() {
        final PodcastSearch podcastSearch = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                testObject.podcastSearchTable.insertPodcastSearch(podcastSearch);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else {
            final TestObserver<Optional<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            testObject
                    .podcastSearchTable
                    .observeQueryForPodcastSearch(podcastSearchIdentifier1)
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch
                            )
                    )
            );
        }
    }

    @Test
    public void testInsertPodcastSearchTwiceIgnoresSecondInsertAndReturnsFirstIdentifier() {
        final PodcastSearch podcastSearch = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                testObject.podcastSearchTable.insertPodcastSearch(podcastSearch);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else {
            @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                    testObject.podcastSearchTable.insertPodcastSearch(podcastSearch);
            assertEquals(
                    podcastSearchIdentifier1,
                    podcastSearchIdentifier2
            );

            final TestObserver<Optional<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            testObject
                    .podcastSearchTable
                    .observeQueryForPodcastSearch(podcastSearchIdentifier1)
                    .subscribe(podcastSearchTestObserver);
            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch
                            )
                    )
            );
        }
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
                testObject.podcastTable.insertPodcast(
                        podcast
                );
        if (podcastIdentifier == null) {
            fail();
        } else {
            final TestObserver<Optional<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            testObject
                    .podcastTable
                    .observeQueryForPodcast(podcastIdentifier)
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastIdentifier,
                                    podcast
                            )
                    )
            );
        }
    }

    @Test
    public void testInsertPodcasTwiceThrowsAndDoesntModifyPodcasts() throws Exception {
        Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                testObject.podcastTable.insertPodcast(
                        podcast1
                );
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
                            return testObject.podcastTable.insertPodcast(podcast2);
                        } catch (Throwable t) {
                            return null;
                        }
                    }).call();

            assertNull(podcastIdentifier2);

            final TestObserver<List<Identified<Podcast>>> allPodcastsTestObserver = new TestObserver<>();
            testObject
                    .podcastTable
                    .observeQueryForAllPodcasts()
                    .subscribe(allPodcastsTestObserver);

            allPodcastsTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastIdentifier1,
                                    podcast1
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertPodcastWithSameFeedUpdatesFirstPodcast() throws Exception {
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                testObject.podcastTable.insertPodcast(
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
                    testObject.podcastTable.upsertPodcast(podcast2);
            assertEquals(
                    podcastIdentifier1,
                    podcastIdentifier2
            );

            final TestObserver<Optional<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            testObject
                    .podcastTable
                    .observeQueryForPodcast(podcastIdentifier1)
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastIdentifier1,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertPodcastWithSameFeedTwiceTogetherUpdatesFirstPodcastWithFirstUpsert() throws Exception {
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                testObject.podcastTable.insertPodcast(
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
                    testObject.podcastTable.upsertPodcasts(
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

            final TestObserver<Optional<Identified<Podcast>>> podcastTestObserver = new TestObserver<>();
            testObject
                    .podcastTable
                    .observeQueryForPodcast(podcastIdentifier1)
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastIdentifier1,
                                    podcast2
                            )
                    )
            );
        }
    }

    @Test
    public void testReplacePodcastSearchResults() throws Exception {
        testObject.replacePodcastSearchResults(
                new PodcastSearch("Planet Money"),
                Arrays.asList(
                        new Podcast(
                                new URL("http://npr.org/planetmoney/artwork"),
                                "Alex Goldman",
                                new URL("http://npr.com/planetmoney/feed"),
                                "Planet Money"
                        ),
                        new Podcast(
                                new URL("http://npr.org/theindicator/artwork"),
                                "Stacey Vanek-Smith",
                                new URL("http://npr.com/theindicator/feed"),
                                "The Indicator"
                        )
                )
        );
    }
}
