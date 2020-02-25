package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.PodcastSearch;

import java.util.Objects;

import javax.annotation.Nullable;

public final class UpsertPodcastSearchException extends Exception {
    public final PodcastSearch podcastSearch;

    public UpsertPodcastSearchException(PodcastSearch podcastSearch) {
        super("Failed to upsert PodcastSearch: " + podcastSearch);
        this.podcastSearch = podcastSearch;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpsertPodcastSearchException that = (UpsertPodcastSearchException) o;
        return podcastSearch.equals(that.podcastSearch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(podcastSearch);
    }

    @Override
    public String toString() {
        return "UpsertPodcastSearchException{" +
                "podcastSearch=" + podcastSearch +
                '}';
    }
}
