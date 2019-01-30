package com.github.hborders.heathcast.models;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;

import javax.annotation.Nullable;

public final class Episode {
    @Nullable
    public final URL mArtworkURL;

    @Nullable
    public final Duration mDuration;

    @Nullable
    public final String mSummary;

    public final String mTitle;

    public final URL mURL;

    public Episode(
            @Nullable URL artworkURL,
            @Nullable Duration duration,
            @Nullable String summary,
            String title,
            URL url
    ) {
        this.mArtworkURL = artworkURL;
        this.mDuration = duration;
        this.mSummary = summary;
        this.mTitle = title;
        this.mURL = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(mArtworkURL, episode.mArtworkURL) &&
                Objects.equals(mDuration, episode.mDuration) &&
                Objects.equals(mSummary, episode.mSummary) &&
                mTitle.equals(episode.mTitle) &&
                mURL.equals(episode.mURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mArtworkURL, mDuration, mSummary, mTitle, mURL);
    }

    @Override
    public String toString() {
        return "Episode{" +
                "mArtworkURL=" + mArtworkURL +
                ", mDuration=" + mDuration +
                ", mSummary='" + mSummary + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mURL=" + mURL +
                '}';
    }
}
