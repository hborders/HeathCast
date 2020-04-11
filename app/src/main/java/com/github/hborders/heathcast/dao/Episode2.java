package com.github.hborders.heathcast.dao;

import java.net.URL;
import java.time.Duration;

import javax.annotation.Nullable;

public interface Episode2 {
    @Nullable
    URL getArtworkURL();

    @Nullable
    Duration getDuration();

    @Nullable
    String getSummary();

    String getTitle();

    URL getURL();
}
