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
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class DatabaseTest {
    private Database testObject;

    @Before
    public void setup() {
        testObject = new Database(
                ApplicationProvider.getApplicationContext(),
                null // in-memory
        );
    }

    @Test
    public void testInsertPodcastSearch() {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier =
                testObject.insertPodcastSearch(new PodcastSearch("Planet Money"));
        if (podcastSearchIdentifier == null) {
            fail();
        } else {
            final TestObserver podcastSearchTestObserver = new TestObserver();
            testObject
                    .observeQueryForPodcastSearch(podcastSearchIdentifier)
                    .observeOn(new TestScheduler())
                    .subscribeWith(podcastSearchTestObserver);

            podcastSearchTestObserver.assertValue(
                    Optional.of(
                            new Identified<PodcastSearch>(
                                    podcastSearchIdentifier,
                                    new PodcastSearch("Planet Money")
                            )
                    )
            );
        }
    }
}
