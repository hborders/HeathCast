package com.github.hborders.heathcast.models;

import java.net.URL;
import java.util.Objects;

import javax.annotation.Nullable;

public final class Podcast {
    @Nullable
    public final URL artworkURL;
    @Nullable
    public final String author;

    public final URL feedURL;

    public final String name;

    public Podcast(
            @Nullable URL artworkURL,
            @Nullable String author,
            URL feedURL,
            String name
    ) {
        this.artworkURL = artworkURL;
        this.author = author;
        this.feedURL = feedURL;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Podcast podcast = (Podcast) o;
        return Objects.equals(artworkURL, podcast.artworkURL) &&
                Objects.equals(author, podcast.author) &&
                feedURL.equals(podcast.feedURL) &&
                name.equals(podcast.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artworkURL, author, feedURL, name);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "artworkURL=" + artworkURL +
                ", author='" + author + '\'' +
                ", feedURL=" + feedURL +
                ", name='" + name + '\'' +
                '}';
    }
}
