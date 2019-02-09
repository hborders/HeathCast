package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.utils.URLUtil;

import java.net.URL;
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
    Identified<Podcast> toIdentifiedPodcast() {
        @Nullable final String artistName = this.artistName;
        @Nullable final String artwork30URLString = this.artworkUrl30;
        @Nullable final String artwork60URLString = this.artworkUrl60;
        @Nullable final String artwork100URLString = this.artworkUrl100;
        @Nullable final String artwork600URLString = this.artworkUrl600;
        @Nullable final String collectionName = this.collectionName;
        @Nullable final String feedURLString = this.feedUrl;

        if (artistName == null ||
                collectionName == null ||
                feedURLString == null) {
            return null;
        } else {
            final Optional<URL> artworkURLOptional =
                    Stream.of(
                            artwork600URLString,
                            artwork100URLString,
                            artwork60URLString,
                            artwork30URLString
                    )
                            .map(URLUtil::fromString)
                            .filter(Objects::nonNull)
                            .findFirst();
            final @Nullable URL feedURL = URLUtil.fromString(feedURLString);
            if (feedURL == null) {
                return null;
            } else {
                return new Identified<>(
                        new Identifier<>(
                                Podcast.class,
                                0
                        ),
                        new Podcast(
                                artworkURLOptional.orElse(null),
                                artistName,
                                feedURL,
                                collectionName
                        )
                );
            }
        }
    }
}
