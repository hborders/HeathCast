package com.github.hborders.heathcast.parcelables;

import android.os.Parcel;

import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifier;

import java.net.URL;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.android.ParcelUtil.readURL;
import static com.github.hborders.heathcast.android.ParcelUtil.writeURL;

public final class PodcastIdentifiedHolder implements UnparcelableHolder<PodcastIdentified> {
    @Nullable
    public final PodcastIdentified podcastIdentified;

    public PodcastIdentifiedHolder(PodcastIdentified podcastIdentified) {
        this.podcastIdentified = podcastIdentified;
    }

    private PodcastIdentifiedHolder(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            podcastIdentified = null;
        } else {
            @Nullable final URL artworkURL = readURL(in);
            @Nullable final String author = in.readString();
            @Nullable final URL feedURL = readURL(in);
            final long id = in.readLong();
            @Nullable final String name = in.readString();

            if (feedURL != null && name != null) {
                podcastIdentified = new PodcastIdentified(
                        new PodcastIdentifier(id),
                        new Podcast(
                                artworkURL,
                                author,
                                feedURL,
                                name
                        )
                );
            } else {
                podcastIdentified = null;
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastIdentifiedHolder that = (PodcastIdentifiedHolder) o;
        return Objects.equals(podcastIdentified, that.podcastIdentified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(podcastIdentified);
    }

    @Override
    public String toString() {
        return "PodcastIdentifiedHolder{" +
                "podcastIdentified=" + podcastIdentified +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (podcastIdentified == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);

            writeURL(dest, podcastIdentified.model.artworkURL);
            dest.writeString(podcastIdentified.model.author);
            dest.writeLong(podcastIdentified.identifier.id);
            writeURL(dest, podcastIdentified.model.feedURL);
            dest.writeString(podcastIdentified.model.name);
        }
    }

    @Nullable
    @Override
    public PodcastIdentified getUnparcelable() {
        return podcastIdentified;
    }

    public static final Creator<PodcastIdentifiedHolder> CREATOR = new Creator<PodcastIdentifiedHolder>() {
        @Override
        public PodcastIdentifiedHolder createFromParcel(Parcel in) {
            return new PodcastIdentifiedHolder(in);
        }

        @Override
        public PodcastIdentifiedHolder[] newArray(int size) {
            return new PodcastIdentifiedHolder[size];
        }
    };
}
