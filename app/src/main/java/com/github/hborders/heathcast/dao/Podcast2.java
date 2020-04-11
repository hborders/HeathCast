package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import java.net.URL;

public interface Podcast2 {
    @Nullable
    URL getArtworkURL();

    @Nullable
    String getAuthor();

    URL getFeedURL();

    String getName();
}
