package com.github.hborders.heathcast.dao;

import androidx.test.core.app.ApplicationProvider;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.PodcastSearch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
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
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier =
                testObject.insertPodcastSearch(new PodcastSearch("Planet Money"));
        if (podcastSearchIdentifier == null) {
            fail();
        } else {
            @Nullable final Identified<PodcastSearch> actual =
                    testObject.readPodcastSearch(podcastSearchIdentifier);
            assertEquals(
                    new Identified<>(
                            podcastSearchIdentifier,
                            new PodcastSearch("Planet Money")
                    ),
                    actual
            );


            final TestObserver<Optional<Identified<PodcastSearch>>> podcastSearchTestObserver = new TestObserver<>();
            testObject
                    .observeQueryForPodcastSearch(podcastSearchIdentifier)
                    .subscribe(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new Identified<>(
                                    podcastSearchIdentifier,
                                    new PodcastSearch("Planet Money")
                            )
                    )
            );
        }
    }
}
