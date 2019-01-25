package com.github.hborders.heathcast.models;

import java.net.URL;
import java.util.Objects;

import javax.annotation.Nullable;

public final class Podcast {
    @Nullable
    public final URL mArtworkURL;
    @Nullable
    public final String mAuthor;

    public final URL mFeedURL;

    public final String mName;

    public Podcast(
            @Nullable URL artworkURL,
            @Nullable String author,
            URL feedURL,
            String name
    ) {
        this.mArtworkURL = artworkURL;
        this.mAuthor = author;
        this.mFeedURL = feedURL;
        this.mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Podcast podcast = (Podcast) o;
        return Objects.equals(mArtworkURL, podcast.mArtworkURL) &&
                Objects.equals(mAuthor, podcast.mAuthor) &&
                mFeedURL.equals(podcast.mFeedURL) &&
                mName.equals(podcast.mName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mArtworkURL, mAuthor, mFeedURL, mName);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "mArtworkURL=" + mArtworkURL +
                ", mAuthor='" + mAuthor + '\'' +
                ", mFeedURL=" + mFeedURL +
                ", mName='" + mName + '\'' +
                '}';
    }
}
