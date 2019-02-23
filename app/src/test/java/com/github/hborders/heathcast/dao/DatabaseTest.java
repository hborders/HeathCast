package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static com.github.hborders.heathcast.util.IdentifiedObservableUtil.modelsObservable;
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
            final TestObserver<List<Podcast>> podcastTestObserver1 =
                    new TestObserver<>();
            modelsObservable(
                    getTestObject()
                            .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
            ).subscribe(podcastTestObserver1);
            final TestObserver<List<Identified<Podcast>>> podcastTestObserver2 =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
                    .subscribe(podcastTestObserver2);

            podcastTestObserver1.assertValue(Collections.emptyList());
            podcastTestObserver2.assertValue(Collections.emptyList());

            getTestObject().outerReplacePodcastSearchResults(
                    podcastSearchIdentifier1,
                    Arrays.asList(
                            podcast1,
                            podcast2,
                            podcast3
                    )
            );

            podcastTestObserver1.assertValueSequence(
                    Arrays.asList(
                            Collections.emptyList(),
                            Arrays.asList(
                                    podcast1,
                                    podcast2,
                                    podcast3
                            )
                    )
            );
            podcastTestObserver2.assertValueSequence(
                    Arrays.asList(
                            Collections.emptyList(),
                            Collections.emptyList()
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

            final TestObserver<List<Podcast>> podcastTestObserver1 =
                    new TestObserver<>();
            modelsObservable(
                    getTestObject()
                            .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
            ).subscribe(podcastTestObserver1);
            final TestObserver<List<Identified<Podcast>>> podcastTestObserver2 =
                    new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
                    .subscribe(podcastTestObserver2);

            podcastTestObserver1.assertValue(
                    Arrays.asList(
                            podcast12,
                            podcast2,
                            podcast4
                    )
            );
            podcastTestObserver2.assertValue(
                    Collections.emptyList()
            );
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

            final TestObserver<List<Podcast>> podcastTestObserver1 =
                    new TestObserver<>();
            modelsObservable(
                    getTestObject()
                            .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
            ).subscribe(podcastTestObserver1);
            final TestObserver<List<Podcast>> podcastTestObserver2 =
                    new TestObserver<>();
            modelsObservable(
                    getTestObject()
                            .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
            ).subscribe(podcastTestObserver2);

            podcastTestObserver1.assertValue(
                    Arrays.asList(
                            podcast1,
                            podcast2,
                            podcast3,
                            podcast4
                    )
            );
            podcastTestObserver2.assertValue(
                    Arrays.asList(
                            podcast5,
                            podcast4,
                            podcast3,
                            podcast2
                    )
            );
        }
    }

    @Test
    public void testDeletePodcastSearchKeepsOtherSearchResultsWithSamePodcasts(){
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
            final int deleteCount = getTestObject().deletePodcastSearch(podcastSearchIdentifier2);
            assertThat(
                    deleteCount,
                    is(1)
            );

            final TestObserver<List<Podcast>> podcastTestObserver1 =
                    new TestObserver<>();
            modelsObservable(
                    getTestObject()
                            .observeQueryForPodcastIdentifieds(podcastSearchIdentifier1)
            ).subscribe(podcastTestObserver1);
            final TestObserver<List<Podcast>> podcastTestObserver2 =
                    new TestObserver<>();
            modelsObservable(
                    getTestObject()
                            .observeQueryForPodcastIdentifieds(podcastSearchIdentifier2)
            ).subscribe(podcastTestObserver2);

            podcastTestObserver1.assertValue(
                    Arrays.asList(
                            podcast1,
                            podcast2,
                            podcast3,
                            podcast4
                    )
            );
            podcastTestObserver2.assertValue(
                    Collections.emptyList()
            );
        }
    }
}