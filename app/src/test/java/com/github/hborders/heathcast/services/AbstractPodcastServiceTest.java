package com.github.hborders.heathcast.services;

import androidx.test.core.app.ApplicationProvider;

import com.github.hborders.heathcast.dao.Database;

import javax.annotation.Nullable;

import io.reactivex.schedulers.Schedulers;

import static com.github.hborders.heathcast.dao.Database.TEST_AUTOINCREMENT_SEED;

public abstract class AbstractPodcastServiceTest {
    @Nullable
    private PodcastService podcastService;

    protected final PodcastService getPodcastService() {
        @Nullable final PodcastService initialPodcastService = this.podcastService;
        if (initialPodcastService == null) {
            final Database database = new Database(
                    TEST_AUTOINCREMENT_SEED,
                    ApplicationProvider.getApplicationContext(),
                    null, // in-memory
                    Schedulers.trampoline()
            );
            final PodcastService newPodcastService = new PodcastService(
                    database,
                    Schedulers.trampoline()
            );

            this.podcastService = newPodcastService;
            return newPodcastService;
        } else {
            return initialPodcastService;
        }
    }
}
