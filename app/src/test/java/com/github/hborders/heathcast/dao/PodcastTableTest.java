package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.features.model.PodcastImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class PodcastTableTest extends AbstractDatabaseTest<Object> {
    private PodcastTable<
            Object,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl
            > getTestObject() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testInsertPodcastWithAllNonnulls() throws Exception {
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getTestObject().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        }

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast
                        )
                )
        );
    }

    @Test
    public void testInsertPodcastWithAllNullables() throws Exception {
        final PodcastImpl podcast = new PodcastImpl(
                null,
                null,
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getTestObject().insertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        }

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast
                        )
                )
        );
    }

    @Test
    public void testUpdatePodcastFromAllNonnullsToAllNullables() throws Exception {
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getTestObject().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        }

        final PodcastImpl podcast2 = new PodcastImpl(
                null,
                null,
                new URL("http://example.com/feed"),
                "name2"
        );
        @Nullable final int updatedRowCount =
                getTestObject().updatePodcastIdentified(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast2
                        )
                );
        assertThat(
                updatedRowCount,
                equalTo(1)
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast2
                        )
                )
        );
    }

    @Test
    public void updatePodcastFromAllNullablesToNonnulls() throws Exception {
        final PodcastImpl podcast1 = new PodcastImpl(
                null,
                null,
                new URL("http://example.com/feed"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getTestObject().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        }

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed"),
                "name2"
        );
        @Nullable final int updatedRowCount =
                getTestObject().updatePodcastIdentified(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast2
                        )
                );
        assertThat(
                updatedRowCount,
                equalTo(1)
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast2
                        )
                )
        );
    }

    @Test
    public void testInsertPodcastTwiceThrowsAndDoesntModifyPodcasts() throws Exception {
        PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        }

        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier2 =
                ((Callable<PodcastImpl.PodcastIdentifierImpl>) () -> {
                    final PodcastImpl podcast2 = new PodcastImpl(
                            new URL("http://example.com/artwork2"),
                            "author2",
                            new URL("http://example.com/feed"),
                            "name2"
                    );
                    try {
                        return getTestObject().insertPodcast(podcast2).orElse(null);
                    } catch (Throwable t) {
                        return null;
                    }
                }).call();

        assertNull(podcastIdentifier2);

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> allPodcastsTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(allPodcastsTestObserver);

        allPodcastsTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast1
                        )
                )
        );
    }

    @Test
    public void testUpsertNewPodcastInsertsPodcast() throws Exception {
        final PodcastImpl podcast = new PodcastImpl(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getTestObject().upsertPodcast(podcast).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        }

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast
                        )
                )
        );
    }

    @Test
    public void testUpsertExistingPodcastWithSameFeedUpdatesPodcast() throws Exception {
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getTestObject().insertPodcast(
                        new PodcastImpl(
                                new URL("http://example.com/artwork"),
                                "author",
                                new URL("http://example.com/feed"),
                                "name"
                        )
                ).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        }

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed"),
                "name2"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier2 =
                getTestObject().upsertPodcast(podcast2).orElse(null);
        assertThat(
                podcastIdentifier2,
                equalTo(podcastIdentifier1)
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast2
                        )
                )
        );
    }

    @Test
    public void testUpsertExistingPodcastWithSameFeedTwiceTogetherUpdatesPodcastWithFirstPodcast() throws Exception {
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getTestObject().insertPodcast(
                        new PodcastImpl(
                                new URL("http://example.com/artwork"),
                                "author",
                                new URL("http://example.com/feed"),
                                "name"
                        )
                ).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        }

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed"),
                "name2"
        );
        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed"),
                "name3"
        );
        final List<Optional<PodcastImpl.PodcastIdentifierImpl>> upsertedPodcastIdentifierOptionals =
                getTestObject().upsertPodcasts(
                        Arrays.asList(
                                podcast2,
                                podcast3
                        )
                );
        assertThat(
                upsertedPodcastIdentifierOptionals,
                equalTo(
                        new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl(
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(podcastIdentifier1),
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(podcastIdentifier1)
                        )
                )
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier1,
                                podcast2
                        )
                )
        );
    }

    @Test
    public void testUpsertNewPodcastWithSameFeedThriceTogetherInsertsFirstPodcastAndOthers() throws Exception {
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed"),
                "name1"
        );
        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed"),
                "name2"
        );
        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed"),
                "name3"
        );
        final List<PodcastImpl> upsertingPodcasts = Arrays.asList(
                podcast1,
                podcast2,
                podcast3
        );
        final List<Optional<PodcastImpl.PodcastIdentifierImpl>> upsertedPodcastIdentifierOptionals =
                getTestObject().upsertPodcasts(
                        upsertingPodcasts
                );
        if (upsertedPodcastIdentifierOptionals.size() != upsertingPodcasts.size()) {
            fail("Expected " + upsertingPodcasts.size() + " identifiers, but got: " + upsertedPodcastIdentifierOptionals);
        }

        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                upsertedPodcastIdentifierOptionals.get(0).orElse(null);
        if (podcastIdentifier == null) {
            fail("Expected " + upsertingPodcasts.size() + " identifiers, but got: " + upsertedPodcastIdentifierOptionals);
        }

        assertThat(
                upsertedPodcastIdentifierOptionals,
                equalTo(
                        new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl(
                                Collections.nCopies(
                                        upsertingPodcasts.size(),
                                        new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(podcastIdentifier)
                                )
                        )
                )
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier,
                                podcast1
                        )
                )
        );
    }

    @Test
    public void testUpsertNewPodcastWithSameFeedThriceTogetherAndExistingPodcastWithSameFeedThriceTogetherInsertsFirstOfNewAndUpdatesFirstOfExisting() throws Exception {
        @Nullable final PodcastImpl.PodcastIdentifierImpl existingPodcastIdentifier =
                getTestObject().insertPodcast(
                        new PodcastImpl(
                                new URL("http://example.com/artwork1"),
                                "author1",
                                new URL("http://example.com/feedB"),
                                "name1"
                        )
                ).orElse(null);
        if (existingPodcastIdentifier == null) {
            fail();
        }
        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feedA"),
                "name2"
        );
        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feedA"),
                "name3"
        );
        final PodcastImpl podcast4 = new PodcastImpl(
                new URL("http://example.com/artwork4"),
                "author4",
                new URL("http://example.com/feedA"),
                "name4"
        );
        final PodcastImpl podcast5 = new PodcastImpl(
                new URL("http://example.com/artwork5"),
                "author5",
                new URL("http://example.com/feedB"),
                "name5"
        );
        final PodcastImpl podcast6 = new PodcastImpl(
                new URL("http://example.com/artwork4"),
                "author4",
                new URL("http://example.com/feedB"),
                "name4"
        );
        final PodcastImpl podcast7 = new PodcastImpl(
                new URL("http://example.com/artwork5"),
                "author5",
                new URL("http://example.com/feedB"),
                "name5"
        );
        final List<PodcastImpl> upsertingPodcasts = Arrays.asList(
                podcast2,
                podcast3,
                podcast4,
                podcast5,
                podcast6,
                podcast7
        );
        final List<Optional<PodcastImpl.PodcastIdentifierImpl>> upsertedPodcastIdentifierOptionals =
                getTestObject().upsertPodcasts(
                        upsertingPodcasts
                );
        if (upsertedPodcastIdentifierOptionals.size() != upsertingPodcasts.size()) {
            fail("Expected " + upsertingPodcasts.size() + " podcast identifiers, but got: " + upsertedPodcastIdentifierOptionals);
        }

        @Nullable final PodcastImpl.PodcastIdentifierImpl insertedPodcastIdentifier =
                upsertedPodcastIdentifierOptionals.get(0).orElse(null);
        if (insertedPodcastIdentifier == null) {
            fail("Expected " + upsertingPodcasts.size() + " podcast identifiers, but got: " + upsertedPodcastIdentifierOptionals);
        }

        assertThat(
                upsertedPodcastIdentifierOptionals,
                equalTo(
                        new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl(
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(insertedPodcastIdentifier),
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(insertedPodcastIdentifier),
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(insertedPodcastIdentifier),
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(existingPodcastIdentifier),
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(existingPodcastIdentifier),
                                new PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl(existingPodcastIdentifier)
                        )
                )
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                insertedPodcastIdentifier,
                                podcast2
                        ),
                        new PodcastImpl.PodcastIdentifiedImpl(
                                existingPodcastIdentifier,
                                podcast5
                        )
                )
        );
    }

    public void testUpdateExistingPodcast() throws Exception {
        final PodcastImpl podcast11 = new PodcastImpl(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11).orElse(null);
        if (podcastIdentifier11 == null) {
            fail();
        }

        final PodcastImpl podcast12 = new PodcastImpl(
                new URL("http://example.com/artwork12"),
                "author12",
                new URL("http://example.com/feed11"),
                "name12"
        );
        final int updatedRowCount = getTestObject().updatePodcastIdentified(
                new PodcastImpl.PodcastIdentifiedImpl(
                        podcastIdentifier11,
                        podcast12
                )
        );
        assertThat(
                updatedRowCount,
                equalTo(1)
        );

        final TestObserver<Optional<PodcastImpl.PodcastIdentifiedImpl>> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentified(podcastIdentifier11)
                .subscribe(podcastTestObserver);
        podcastTestObserver.assertValueSequence(
                Collections.singleton(
                        Optional.of(
                                new PodcastImpl.PodcastIdentifiedImpl(
                                        podcastIdentifier11,
                                        podcast12
                                )
                        )
                )
        );
    }

    public void testUpdateMissingPodcast() throws Exception {
        final PodcastImpl podcast11 = new PodcastImpl(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier =
                getTestObject().insertPodcast(podcast11).orElse(null);
        if (podcastIdentifier == null) {
            fail();
        }

        getTestObject().deletePodcast(podcastIdentifier);

        final PodcastImpl podcast12 = new PodcastImpl(
                new URL("http://example.com/artwork12"),
                "author12",
                new URL("http://example.com/feed11"),
                "name12"
        );
        final int updatedRowCount = getTestObject().updatePodcastIdentified(
                new PodcastImpl.PodcastIdentifiedImpl(
                        podcastIdentifier,
                        podcast12
                )
        );
        assertThat(
                updatedRowCount,
                equalTo(0)
        );

        final TestObserver<Optional<PodcastImpl.PodcastIdentifiedImpl>> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentified(podcastIdentifier)
                .subscribe(podcastTestObserver);
        podcastTestObserver.assertValue(Optional.empty());
    }

    @Test
    public void testObserveQueryForPodcastIdentified() throws Exception {
        final PodcastImpl podcast11 = new PodcastImpl(
                new URL("http://example.com/artwork11"),
                "author11",
                new URL("http://example.com/feed11"),
                "name11"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier11 =
                getTestObject().insertPodcast(podcast11).orElse(null);

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2).orElse(null);
        if (podcastIdentifier11 == null) {
            fail();
        }
        if (podcastIdentifier2 == null) {
            fail();
        }

        final TestObserver<Optional<PodcastImpl.PodcastIdentifiedImpl>> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForPodcastIdentified(podcastIdentifier11)
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                Optional.of(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier11,
                                podcast11
                        )
                )
        );

        final PodcastImpl podcast12 = new PodcastImpl(
                new URL("http://example.com/artwork12"),
                "author12",
                new URL("http://example.com/feed11"),
                "name12"
        );
        getTestObject().updatePodcastIdentified(
                new PodcastImpl.PodcastIdentifiedImpl(
                        podcastIdentifier11,
                        podcast12
                )
        );

        podcastTestObserver.assertValueSequence(
                Arrays.asList(
                        Optional.of(
                                new PodcastImpl.PodcastIdentifiedImpl(
                                        podcastIdentifier11,
                                        podcast11
                                )
                        ),
                        Optional.of(
                                new PodcastImpl.PodcastIdentifiedImpl(
                                        podcastIdentifier11,
                                        podcast12
                                )
                        )
                )
        );

        getTestObject().deletePodcast(podcastIdentifier11);

        podcastTestObserver.assertValueSequence(
                Arrays.asList(
                        Optional.of(
                                new PodcastImpl.PodcastIdentifiedImpl(
                                        podcastIdentifier11,
                                        podcast11
                                )
                        ),
                        Optional.of((
                                        new PodcastImpl.PodcastIdentifiedImpl(
                                                podcastIdentifier11,
                                                podcast12
                                        )
                                )
                        ),
                        Optional.empty()
                )
        );
    }

    @Test
    public void testDeletePodcast() throws Exception {
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1).orElse(null);

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        }
        if (podcastIdentifier2 == null) {
            fail();
        }

        final int deleteCount = getTestObject().deletePodcast(podcastIdentifier1);
        assertThat(
                deleteCount,
                equalTo(1)
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier2,
                                podcast2
                        )
                )
        );
    }

    @Test
    public void testDeletePodcasts() throws Exception {
        final PodcastImpl podcast1 = new PodcastImpl(
                new URL("http://example.com/artwork1"),
                "author1",
                new URL("http://example.com/feed1"),
                "name1"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier1 =
                getTestObject().insertPodcast(podcast1).orElse(null);

        final PodcastImpl podcast2 = new PodcastImpl(
                new URL("http://example.com/artwork2"),
                "author2",
                new URL("http://example.com/feed2"),
                "name2"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier2 =
                getTestObject().insertPodcast(podcast2).orElse(null);

        final PodcastImpl podcast3 = new PodcastImpl(
                new URL("http://example.com/artwork3"),
                "author3",
                new URL("http://example.com/feed3"),
                "name3"
        );
        @Nullable final PodcastImpl.PodcastIdentifierImpl podcastIdentifier3 =
                getTestObject().insertPodcast(podcast3).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        }
        if (podcastIdentifier2 == null) {
            fail();
        }
        if (podcastIdentifier3 == null) {
            fail();
        }

        final int deleteCount = getTestObject().deletePodcasts(
                Arrays.asList(
                        podcastIdentifier1,
                        podcastIdentifier2
                )
        );
        assertThat(
                deleteCount,
                equalTo(2)
        );

        final TestObserver<PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl> podcastTestObserver = new TestObserver<>();
        getTestObject()
                .observeQueryForAllPodcastIdentifieds()
                .subscribe(podcastTestObserver);

        podcastTestObserver.assertValue(
                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl(
                        new PodcastImpl.PodcastIdentifiedImpl(
                                podcastIdentifier3,
                                podcast3
                        )
                )
        );
    }
}
