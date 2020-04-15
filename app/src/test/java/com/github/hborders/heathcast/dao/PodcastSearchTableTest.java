package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.PodcastSearchIdentified;
import com.github.hborders.heathcast.models.PodcastSearchIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearchIdentifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Optional;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastSearchTableTest extends AbstractDatabaseTest<Object> {

    private final PodcastSearch podcastSearch1 = new PodcastSearch("Search1");
    private final PodcastSearch podcastSearch2 = new PodcastSearch("Search2");
    private final PodcastSearch podcastSearch3 = new PodcastSearch("Search3");

    private PodcastSearchTable<Object> getTestObject() {
        return getDatabase().podcastSearchTable;
    }

    @Test
    public void testUpsertPodcastSearch() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else {
            final TestObserver<PodcastSearchIdentifiedList> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    new PodcastSearchIdentifiedList(
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertNewPodcastSearchSortsItFirst() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            final TestObserver<PodcastSearchIdentifiedList> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    new PodcastSearchIdentifiedList(
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            ),
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            ),
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingPodcastSearchReturnsExistingIdentifierAndSortsIfFirst() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else if (podcastSearchIdentifier3 == null) {
            fail();
        } else {
            @Nullable final PodcastSearchIdentifier existingPodcastSearchIdentifier =
                    getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
            assertThat(
                    existingPodcastSearchIdentifier,
                    equalTo(podcastSearchIdentifier1)
            );

            final TestObserver<PodcastSearchIdentifiedList> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    new PodcastSearchIdentifiedList(
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            ),
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            ),
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            )
                    )
            );
        }
    }

    @Test
    public void testUpsertExistingFirstPodcastSearchKeepsPreviousSortValue() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
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
                        equalTo(initialPodcastSearch3Sort)
                );
            }
        }
    }

    @Test
    public void testDeleteExistingPodcastSearchById() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
            assertThat(
                    deleteCount,
                    equalTo(1)
            );

            final TestObserver<PodcastSearchIdentifiedList> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastSearchIdentifiedList(
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier2,
                                    podcastSearch2
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteMissingPodcastSearchById() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

            final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
            assertThat(
                    deleteCount,
                    equalTo(0)
            );
        }
    }

    @Test
    public void testDeleteExistingPodcastSearchesByIds() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
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
                    equalTo(2)
            );

            final TestObserver<PodcastSearchIdentifiedList> podcastTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForAllPodcastSearchIdentifieds()
                    .subscribe(podcastTestObserver);

            podcastTestObserver.assertValue(
                    new PodcastSearchIdentifiedList(
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier3,
                                    podcastSearch3
                            )
                    )
            );
        }
    }

    @Test
    public void testDeleteMissingPodcastSearchesByIds() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
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
                    equalTo(0)
            );
        }
    }

    @Test
    public void testObserveQueryForPodcastSearchIdentified() {
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchIdentifier podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);

        if (podcastSearchIdentifier1 == null) {
            fail();
        } else if (podcastSearchIdentifier2 == null) {
            fail();
        } else {
            final TestObserver<Optional<PodcastSearchIdentified>> podcastSearchTestObserver = new TestObserver<>();
            getTestObject()
                    .observeQueryForPodcastSearchIdentified(podcastSearchIdentifier1)
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new PodcastSearchIdentified(
                                    podcastSearchIdentifier1,
                                    podcastSearch1
                            )
                    )
            );

            getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

            podcastSearchTestObserver.assertValueSequence(
                    Arrays.asList(
                            Optional.of(
                                    new PodcastSearchIdentified(
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
