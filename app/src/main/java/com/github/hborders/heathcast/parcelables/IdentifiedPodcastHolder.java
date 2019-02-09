package com.github.hborders.heathcast.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;

import java.net.URL;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.utils.ParcelUtil.readURL;
import static com.github.hborders.heathcast.utils.ParcelUtil.writeURL;

public final class IdentifiedPodcastHolder implements Parcelable, UnparcelableHolder<Identified<Podcast>> {
    @Nullable
    public final Identified<Podcast> mIdentifiedPodcast;

    public IdentifiedPodcastHolder(Identified<Podcast> identifiedPodcast) {
        this.mIdentifiedPodcast = identifiedPodcast;
    }

    private IdentifiedPodcastHolder(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            mIdentifiedPodcast = null;
        } else {
            @Nullable final URL artworkURL = readURL(in);
            @Nullable final String author = in.readString();
            @Nullable final URL feedURL = readURL(in);
            final long id = in.readLong();
            @Nullable final String name = in.readString();

            if (feedURL != null && name != null) {
                mIdentifiedPodcast = new Identified<>(
                        new Identifier<>(
                                Podcast.class,
                                id
                        ),
                        new Podcast(
                                artworkURL,
                                author,
                                feedURL,
                                name
                        )
                );
            } else {
                mIdentifiedPodcast = null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifiedPodcastHolder that = (IdentifiedPodcastHolder) o;
        return Objects.equals(mIdentifiedPodcast, that.mIdentifiedPodcast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mIdentifiedPodcast);
    }

    @Override
    public String toString() {
        return "IdentifiedPodcastHolder{" +
                "mIdentifiedPodcast=" + mIdentifiedPodcast +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mIdentifiedPodcast == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);

            writeURL(dest, mIdentifiedPodcast.mModel.mArtworkURL);
            dest.writeString(mIdentifiedPodcast.mModel.mAuthor);
            dest.writeLong(mIdentifiedPodcast.mIdentifier.mId);
            writeURL(dest, mIdentifiedPodcast.mModel.mFeedURL);
            dest.writeString(mIdentifiedPodcast.mModel.mName);
        }
    }

    @Nullable
    @Override
    public Identified<Podcast> getUnparcelable() {
        return mIdentifiedPodcast;
    }

    public static final Creator<IdentifiedPodcastHolder> CREATOR = new Creator<IdentifiedPodcastHolder>() {
        @Override
        public IdentifiedPodcastHolder createFromParcel(Parcel in) {
            return new IdentifiedPodcastHolder(in);
        }

        @Override
        public IdentifiedPodcastHolder[] newArray(int size) {
            return new IdentifiedPodcastHolder[size];
        }
    };
}
