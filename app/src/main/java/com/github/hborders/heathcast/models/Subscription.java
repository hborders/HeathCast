package com.github.hborders.heathcast.models;

import java.util.Objects;

import javax.annotation.Nullable;

public final class Subscription {
    public final PodcastIdentified podcastIdentified;

    public Subscription(PodcastIdentified podcastIdentified) {
        this.podcastIdentified = podcastIdentified;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return podcastIdentified.equals(that.podcastIdentified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(podcastIdentified);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "podcastIdentified=" + podcastIdentified +
                '}';
    }
}
