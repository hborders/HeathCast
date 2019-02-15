package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.PodcastSearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastSearchTableTest extends AbstractDatabaseTest {
    private PodcastSearchTable getTestObject() {
        return getDatabase().podcastSearchTable;
    }

    @Test
    public void testInsertPodcastSearch() {
        final PodcastSearch podcastSearch = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().insertPodcastSearch(podcastSearch);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else {
            final TestObserver<Set<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearches()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Collections.singleton(
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
                getTestObject().insertPodcastSearch(podcastSearch);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else {
            @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                    getTestObject().insertPodcastSearch(podcastSearch);
            assertEquals(
                    podcastSearchIdentifier1,
                    podcastSearchIdentifier2
            );

            final TestObserver<Set<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearches()
                    .subscribe(podcastSearchTestObserver);
            podcastSearchTestObserver.assertValue(
                    Collections.singleton(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch
                            )
                    )
            );
        }
    }

    @Test
    public void testObserveQueryForPodcastSearch() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().insertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().insertPodcastSearch(podcastSearch2);

        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final TestObserver<Optional<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastSearch(podcastSearchIdentifier1)
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            )
                    )
            );
        }
    }
}
