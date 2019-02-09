package com.github.hborders.heathcast.models;

import java.util.Objects;

public class PodcastSearch {
    public final String mSearch;

    public PodcastSearch(String search) {
        this.mSearch = search;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastSearch that = (PodcastSearch) o;
        return mSearch.equals(that.mSearch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mSearch);
    }

    @Override
    public String toString() {
        return "PodcastSearch{" +
                "mSearch='" + mSearch + '\'' +
                '}';
    }
}
