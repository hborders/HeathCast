package com.github.hborders.heathcast.models;

import androidx.annotation.Nullable;

import java.util.Objects;

public final class PodcastSearch {
    public final String search;

    public PodcastSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastSearch that = (PodcastSearch) o;
        return search.equals(that.search);
    }

    @Override
    public int hashCode() {
        return Objects.hash(search);
    }

    @Override
    public String toString() {
        return "PodcastSearch{" +
                "search='" + search + '\'' +
                '}';
    }
}
