package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

public interface Episode2 {
    @Nullable
    URL getArtworkURL();

    @Nullable
    Duration getDuration();

    @Nullable
    Date getPublishDate();

    @Nullable
    String getSummary();

    String getTitle();

    URL getURL();
}
