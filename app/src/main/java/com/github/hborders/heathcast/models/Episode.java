package com.github.hborders.heathcast.models;

import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nullable;

public final class Episode {
    @Nullable
    public final URL artworkURL;

    @Nullable
    public final Duration duration;

    @Nullable
    public final Date publishDate;

    @Nullable
    public final String summary;

    public final String title;

    public final URL url;

    public Episode(
            @Nullable URL artworkURL,
            @Nullable Duration duration,
            @Nullable Date publishDate,
            @Nullable String summary,
            String title,
            URL url
    ) {
        this.artworkURL = artworkURL;
        this.duration = duration;
        this.publishDate = publishDate;
        this.summary = summary;
        this.title = title;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(artworkURL, episode.artworkURL) &&
                Objects.equals(duration, episode.duration) &&
                Objects.equals(publishDate, episode.publishDate) &&
                Objects.equals(summary, episode.summary) &&
                title.equals(episode.title) &&
                url.equals(episode.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artworkURL, duration, publishDate, summary, title, url);
    }

    @Override
    public String toString() {
        return "Episode{" +
                "artworkURL=" + artworkURL +
                ", duration=" + duration +
                ", publishDate=" + publishDate +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", url=" + url +
                '}';
    }
}
