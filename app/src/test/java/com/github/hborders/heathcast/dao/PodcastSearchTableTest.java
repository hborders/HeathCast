package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.features.model.PodcastSearchImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Optional;

import io.reactivex.rxjava3.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastSearchTableTest extends AbstractDatabaseTest<Object> {

    private final PodcastSearchImpl podcastSearch1 = new PodcastSearchImpl("Search1");
    private final PodcastSearchImpl podcastSearch2 = new PodcastSearchImpl("Search2");
    private final PodcastSearchImpl podcastSearch3 = new PodcastSearchImpl("Search3");

    private PodcastSearchTable<
            Object,
            PodcastSearchImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl
            > getTestObject() {
        return getDatabase().podcastSearchTable;
    }

    @Test
    public void testUpsertPodcastSearch() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }

        final TestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastSearchTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchTestObserver);

        podcastSearchTestObserver.assertValue(
                new PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl(
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier1,
                                podcastSearch1
                        )
                )
        );
    }

    @Test
    public void testUpsertNewPodcastSearchSortsItFirst() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }
        if (podcastSearchIdentifier3 == null) {
            fail();
        }

        final TestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastSearchTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchTestObserver);

        podcastSearchTestObserver.assertValue(
                new PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl(
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier3,
                                podcastSearch3
                        ),
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier2,
                                podcastSearch2
                        ),
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier1,
                                podcastSearch1
                        )
                )
        );
    }

    @Test
    public void testUpsertExistingPodcastSearchReturnsExistingIdentifierAndSortsIfFirst() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }
        if (podcastSearchIdentifier3 == null) {
            fail();
        }

        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl existingPodcastSearchIdentifier =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        assertThat(
                existingPodcastSearchIdentifier,
                equalTo(podcastSearchIdentifier1)
        );

        final TestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastSearchTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastSearchTestObserver);

        podcastSearchTestObserver.assertValue(
                new PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl(
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier1,
                                podcastSearch1
                        ),
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier3,
                                podcastSearch3
                        ),
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier2,
                                podcastSearch2
                        )
                )
        );
    }

    @Test
    public void testUpsertExistingFirstPodcastSearchKeepsPreviousSortValue() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }
        if (podcastSearchIdentifier3 == null) {
            fail();
        }

        final @Nullable Long initialPodcastSearch3Sort =
                getTestObject().sortForPodcastSearch(podcastSearchIdentifier3);
        if (initialPodcastSearch3Sort == null) {
            fail();
        }

        getTestObject().upsertPodcastSearch(podcastSearch3);
        final @Nullable Long updatedPodcastSearch3Sort =
                getTestObject().sortForPodcastSearch(podcastSearchIdentifier3);

        assertThat(
                updatedPodcastSearch3Sort,
                equalTo(initialPodcastSearch3Sort)
        );
    }

    @Test
    public void testDeleteExistingPodcastSearchById() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }

        final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
        assertThat(
                deleteCount,
                equalTo(1)
        );

        final TestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl(
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier2,
                                podcastSearch2
                        )
                )
        );
    }

    @Test
    public void testDeleteMissingPodcastSearchById() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }

        getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

        final int deleteCount = getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);
        assertThat(
                deleteCount,
                equalTo(0)
        );
    }

    @Test
    public void testDeleteExistingPodcastSearchesByIds() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }
        if (podcastSearchIdentifier3 == null) {
            fail();
        }

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

        final TestObserver<PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastSearchIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl(
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier3,
                                podcastSearch3
                        )
                )
        );
    }

    @Test
    public void testDeleteMissingPodcastSearchesByIds() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier3 =
                getTestObject().upsertPodcastSearch(podcastSearch3).orElse(null);
        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }
        if (podcastSearchIdentifier3 == null) {
            fail();
        }

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

    @Test
    public void testObserveQueryForPodcastSearchIdentified() {
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier1 =
                getTestObject().upsertPodcastSearch(podcastSearch1).orElse(null);
        @Nullable final PodcastSearchImpl.PodcastSearchIdentifierImpl podcastSearchIdentifier2 =
                getTestObject().upsertPodcastSearch(podcastSearch2).orElse(null);

        if (podcastSearchIdentifier1 == null) {
            fail();
        }
        if (podcastSearchIdentifier2 == null) {
            fail();
        }

        final TestObserver<Optional<PodcastSearchImpl.PodcastSearchIdentifiedImpl>> podcastSearchTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForPodcastSearchIdentified(podcastSearchIdentifier1)
                .subscribe(podcastSearchTestObserver);

        podcastSearchTestObserver.assertValue(
                Optional.of(
                        new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                podcastSearchIdentifier1,
                                podcastSearch1
                        )
                )
        );

        getTestObject().deletePodcastSearchById(podcastSearchIdentifier1);

        podcastSearchTestObserver.assertValueSequence(
                Arrays.asList(
                        Optional.of(
                                new PodcastSearchImpl.PodcastSearchIdentifiedImpl(
                                        podcastSearchIdentifier1,
                                        podcastSearch1
                                )
                        ),
                        Optional.empty()
                )
        );
    }
}
