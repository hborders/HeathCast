package com.github.hborders.heathcast.service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

final class PodcastSearchResultsJson {
    @Nullable
    public List<PodcastJson> results;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastSearchResultsJson that = (PodcastSearchResultsJson) o;
        return Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public String toString() {
        return "PodcastSearchResultsJson{" +
                "results=" + results +
                '}';
    }
}
