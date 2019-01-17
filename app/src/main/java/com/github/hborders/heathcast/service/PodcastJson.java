package com.github.hborders.heathcast.service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public final class PodcastJson {
    @Nullable
    public String artistName;

    @Nullable
    public String collectionName;

    @Nullable
    public String trackName;

    @Nullable
    public String feedUrl;

    @Nullable
    public String artworkUrl30;

    @Nullable
    public String artworkUrl60;

    @Nullable
    public String artworkUrl100;

    @Nullable
    public String artworkUrl600;

    @Nullable
    public List<String> genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastJson that = (PodcastJson) o;
        return Objects.equals(artistName, that.artistName) &&
                Objects.equals(collectionName, that.collectionName) &&
                Objects.equals(trackName, that.trackName) &&
                Objects.equals(feedUrl, that.feedUrl) &&
                Objects.equals(artworkUrl30, that.artworkUrl30) &&
                Objects.equals(artworkUrl60, that.artworkUrl60) &&
                Objects.equals(artworkUrl100, that.artworkUrl100) &&
                Objects.equals(artworkUrl600, that.artworkUrl600) &&
                Objects.equals(genres, that.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistName, collectionName, trackName, feedUrl, artworkUrl30, artworkUrl60, artworkUrl100, artworkUrl600, genres);
    }

    @Override
    public String toString() {
        return "PodcastJson{" +
                "artistName='" + artistName + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", trackName='" + trackName + '\'' +
                ", feedUrl='" + feedUrl + '\'' +
                ", artworkUrl30='" + artworkUrl30 + '\'' +
                ", artworkUrl60='" + artworkUrl60 + '\'' +
                ", artworkUrl100='" + artworkUrl100 + '\'' +
                ", artworkUrl600='" + artworkUrl600 + '\'' +
                ", genres=" + genres +
                '}';
    }
}
