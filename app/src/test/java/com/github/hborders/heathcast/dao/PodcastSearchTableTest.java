package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.PodcastSearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void testUpsertPodcastSearch() {
        final PodcastSearch podcastSearch = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else {
            final TestObserver<List<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertNewPodcastSearchSortsItFirst() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            final TestObserver<List<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Arrays.asList(
                            new Identified<>(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            ),
                            new Identified<>(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            ),
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingPodcastSearchReturnsExistingIdentifierAndSortsIfFirst() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            @Nullable final Identifier<PodcastSearch> existingPodcastSearchIdentifier =
                    getTestObject().upsertPodcastSearch(podcastSearch1);
            assertEquals(
                    podcastSearchIdentifier1,
                    existingPodcastSearchIdentifier
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Arrays.asList(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            ),
                            new Identified<>(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            ),
                            new Identified<>(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingFirstPodcastSearchKeepsPreviousSortValue() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            final @Nullable Long initialPodcastSearch3Sort =
                    getTestObject().sortForPodcastSearch(podcastSearchIdentifier3);
            if (initialPodcastSearch3Sort == null) {
                fail();
            } else {
                getTestObject().upsertPodcastSearch(podcastSearch3);
                final @Nullable Long updatedPodcastSearch3Sort =
                        getTestObject().sortForPodcastSearch(podcastSearchIdentifier3);

                assertEquals(
                        initialPodcastSearch3Sort,
                        updatedPodcastSearch3Sort
                );
            }
        }
    }

    @Test
    public void testDeleteExistingPodcastSearchById() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
            assertEquals(
                    1,
                    deleteCount
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteMissingPodcastSearchById() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

            final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
            assertEquals(
                    0,
                    deleteCount
            );
        }
    }

    @Test
    public void testDeleteExistingPodcastSearch() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcastSearch(podcastSearch1);
            assertEquals(
                    1,
                    deleteCount
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteMissingPodcastSearch() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().deletePodcastSearch(podcastSearch1);
            final int deleteCount = getTestObject().deletePodcastSearch(podcastSearch1);
            assertEquals(
                    0,
                    deleteCount
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteExistingPodcastSearchesByIds() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);

        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcastSearchesByIds(
                    Arrays.asList(
                            podcastSearchIdentifier1,
                            podcastSearchIdentifier2
                    )
            );
            assertEquals(
                    2,
                    deleteCount
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteMissingPodcastSearchesByIds() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);

        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            getTestObject().deletePodcastSearchesByIds(
                    Arrays.asList(
                            podcastSearchIdentifier1,
                            podcastSearchIdentifier2
                    )
            );
            final int deleteCount = getTestObject().deletePodcastSearchesByIds(
                    Arrays.asList(
                            podcastSearchIdentifier1,
                            podcastSearchIdentifier2
                    )
            );
            assertEquals(
                    0,
                    deleteCount
            );
        }
    }

    @Test
    public void testDeleteExistingPodcastSearches() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);

        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcastSearches(
                    Arrays.asList(
                            podcastSearch1,
                            podcastSearch2
                    )
            );
            assertEquals(
                    2,
                    deleteCount
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteMissingPodcastSearches() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);

        final PodcastSearch podcastSearch3 = new PodcastSearch("Modern Love");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            getTestObject().deletePodcastSearches(
                    Arrays.asList(
                            podcastSearch1,
                            podcastSearch2
                    )
            );
            final int deleteCount = getTestObject().deletePodcastSearches(
                    Arrays.asList(
                            podcastSearch1,
                            podcastSearch2
                    )
            );
            assertEquals(
                    0,
                    deleteCount
            );

            final TestObserver<List<Identified<PodcastSearch>>> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    Collections.singletonList(
                            new Identified<>(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            )
                    )
            );
        }
    }

    @Test
    public void testObserveQueryForPodcastSearchIdentified() {
        final PodcastSearch podcastSearch1 = new PodcastSearch("Planet Money");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);

        final PodcastSearch podcastSearch2 = new PodcastSearch("The Indicator");
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);

        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final TestObserver<Optional<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastSearchIdentified(podcastSearchIdentifier1)
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            )
                    )
            );

            getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

            podcastSearchTestObserver.assertValueSequence(
                    Arrays.asList(
                            Optional.of(
                                    new Identified<>(
                                            podcastSearchIdentifier1,
                                            podcastSearch1
                                    )
                            ),
                            Optional.empty()
                    )
            );
        }
    }
}
