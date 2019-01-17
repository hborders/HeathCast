package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.Podcast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

final class PodcastJson {
    @Nullable
    public String artistName;

    @Nullable
    public String artworkUrl30;

    @Nullable
    public String artworkUrl60;

    @Nullable
    public String artworkUrl100;

    @Nullable
    public String artworkUrl600;

    @Nullable
    public String collectionName;

    @Nullable
    public String feedUrl;

    @Nullable
    public List<String> genres;

    @Nullable
    public Podcast toPodcast() {
        @Nullable final String artistName = this.artistName;
        @Nullable final String artworkUrl30 = this.artworkUrl30;
        @Nullable final String artworkUrl60 = this.artworkUrl60;
        @Nullable final String artworkUrl100 = this.artworkUrl100;
        @Nullable final String artworkUrl600 = this.artworkUrl600;
        @Nullable final String collectionName = this.collectionName;
        @Nullable final String feedUrl = this.feedUrl;
        @Nullable final List<String> genres = this.genres;

        if (artistName == null ||
                collectionName == null ||
                feedUrl == null) {
            return null;
        } else {
            Optional<URL> artworkURLOptional =
                    Stream.of(
                            artworkUrl600,
                            artworkUrl100,
                            artworkUrl60,
                            artworkUrl30
                    )
                            .map(artworkUrl -> {
                                if (artworkUrl == null) {
                                    return null;
                                } else {
                                    try {
                                        return new URL(artworkUrl);
                                    } catch (MalformedURLException e) {
                                        return null;
                                    }
                                }
                            })
                            .filter(Objects::nonNull)
                            .findFirst();
            try {
                final URL feedURL = new URL(feedUrl);
                return new Podcast(
                        artworkURLOptional.orElse(null),
                        artistName,
                        feedURL,
                        Optional.ofNullable(genres).orElse(Collections.emptyList()),
                        collectionName
                );
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }
}
