package com.github.hborders.heathcast.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.utils.URLUtil;

import java.net.URL;
import java.util.Objects;

import javax.annotation.Nullable;

public final class PodcastHolder implements Parcelable, UnparcelableHolder<Podcast> {
    @Nullable
    public final Podcast mPodcast;

    public PodcastHolder(Podcast podcast) {
        this.mPodcast = podcast;
    }

    private PodcastHolder(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            mPodcast = null;
        } else {
            @Nullable final URL artworkURL = URLUtil.fromString(in.readString());
            @Nullable final String author = in.readString();
            @Nullable final URL feedURL = URLUtil.fromString(in.readString());
            @Nullable final String name = in.readString();

            if (feedURL != null && name != null) {
                mPodcast = new Podcast(
                        artworkURL,
                        author,
                        feedURL,
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
        PodcastHolder that = (PodcastHolder) o;
        return Objects.equals(mPodcast, that.mPodcast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mPodcast);
    }

    @Override
    public String toString() {
        return "PodcastHolder{" +
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
            @Nullable final URL artworkURL = mPodcast.mArtworkURL;
            if (artworkURL == null) {
                dest.writeString(null);
            } else {
                dest.writeString(artworkURL.toExternalForm());
            }
            dest.writeString(mPodcast.mAuthor);
            dest.writeString(mPodcast.mFeedURL.toExternalForm());
            dest.writeString(mPodcast.mName);
        }
    }

    @Nullable
    @Override
    public Podcast getUnparcelable() {
        return mPodcast;
    }

    public static final Creator<PodcastHolder> CREATOR = new Creator<PodcastHolder>() {
        @Override
        public PodcastHolder createFromParcel(Parcel in) {
            return new PodcastHolder(in);
        }

        @Override
        public PodcastHolder[] newArray(int size) {
            return new PodcastHolder[size];
        }
    };
}
