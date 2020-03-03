package com.github.hborders.heathcast.services;

import androidx.test.core.app.ApplicationProvider;

import com.github.hborders.heathcast.dao.Database;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.annotation.Nullable;

import io.reactivex.schedulers.Schedulers;

public abstract class AbstractPodcastServiceTest {
    @Nullable
    private PodcastService podcastService;

    protected final PodcastService getPodcastService() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException {
        @Nullable final PodcastService initialPodcastService = this.podcastService;
        if (initialPodcastService == null) {
            final Database<Object> database = new Database<>(
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
