package com.github.hborders.heathcast.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.utils.ArrayUtil;
import com.github.hborders.heathcast.utils.URLUtil;

import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PodcastParcelable implements Parcelable {
    @Nullable
    public final Podcast mPodcast;

    public PodcastParcelable(@Nullable Podcast podcast) {
        this.mPodcast = podcast;
    }

    private PodcastParcelable(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            mPodcast = null;
        } else {
            @Nullable final URL artworkURL = URLUtil.fromString(in.readString());
            @Nullable final String author = in.readString();
            @Nullable final URL feedURL = URLUtil.fromString(in.readString());
            @Nonnull final List<String> genres = ArrayUtil.asList(in.createStringArray());
            @Nullable final String name = in.readString();

            if (feedURL != null && name != null) {
                mPodcast = new Podcast(
                        artworkURL,
                        author,
                        feedURL,
                        genres,
                        name
                );
            } else {
                mPodcast = null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastParcelable that = (PodcastParcelable) o;
        return Objects.equals(mPodcast, that.mPodcast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mPodcast);
    }

    @Override
    public String toString() {
        return "PodcastParcelable{" +
                "mPodcast=" + mPodcast +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mPodcast == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            @Nullable final URL artworkURL = mPodcast.artworkURL;
            if (artworkURL == null) {
                dest.writeString(null);
            } else {
                dest.writeString(artworkURL.toExternalForm());
            }
            dest.writeString(mPodcast.author);
            dest.writeString(mPodcast.feedURL.toExternalForm());
            dest.writeStringArray(mPodcast.genres.toArray(new String[0]));
            dest.writeString(mPodcast.name);
        }
    }

    @Nullable
    public Podcast getPodcast() {
        return mPodcast;
    }

    public static final Creator<PodcastParcelable> CREATOR = new Creator<PodcastParcelable>() {
        @Override
        public PodcastParcelable createFromParcel(Parcel in) {
            return new PodcastParcelable(in);
        }

        @Override
        public PodcastParcelable[] newArray(int size) {
            return new PodcastParcelable[size];
        }
    };
}
