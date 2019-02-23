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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastSearchTableTest extends AbstractDatabaseTest {

    private final PodcastSearch podcastSearch1 = new PodcastSearch("Search1");
    private final PodcastSearch podcastSearch2 = new PodcastSearch("Search2");
    private final PodcastSearch podcastSearch3 = new PodcastSearch("Search3");

    private PodcastSearchTable getTestObject() {
        return getDatabase().podcastSearchTable;
    }

    @Test
    public void testUpsertPodcastSearch() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
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
                                    podcastSearch1
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertNewPodcastSearchSortsItFirst() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
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
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
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
            assertThat(
                    existingPodcastSearchIdentifier,
                    is(podcastSearchIdentifier1)
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
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
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

                assertThat(
                        updatedPodcastSearch3Sort,
                        is(initialPodcastSearch3Sort)
                );
            }
        }
    }

    @Test
    public void testDeleteExistingPodcastSearchById() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
            assertThat(
                    deleteCount,
                    is(1)
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
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

            final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
            assertThat(
                    deleteCount,
                    is(0)
            );
        }
    }

    @Test
    public void testDeleteExistingPodcastSearchesByIds() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
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
            assertThat(
                    deleteCount,
                    is(2)
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
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2);
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
            assertThat(
                    deleteCount,
                    is(0)
            );
        }
    }

    @Test
    public void testObserveQueryForPodcastSearchIdentified() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1);
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
