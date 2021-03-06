package com.github.hborders.heathcast.features.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.parcelables.UnparcelableHolder;

import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import static com.github.hborders.heathcast.android.ParcelUtil.readDate;
import static com.github.hborders.heathcast.android.ParcelUtil.readDuration;
import static com.github.hborders.heathcast.android.ParcelUtil.readURL;
import static com.github.hborders.heathcast.android.ParcelUtil.writeDate;
import static com.github.hborders.heathcast.android.ParcelUtil.writeDuration;
import static com.github.hborders.heathcast.android.ParcelUtil.writeURL;

public final class EpisodeIdentifiedHolder
        implements
        Parcelable,
        UnparcelableHolder<EpisodeImpl.EpisodeIdentifiedImpl> {
    public static final Creator<EpisodeIdentifiedHolder> CREATOR = new Creator<EpisodeIdentifiedHolder>() {
        @Override
        public EpisodeIdentifiedHolder createFromParcel(Parcel in) {
            return new EpisodeIdentifiedHolder(in);
        }

        @Override
        public EpisodeIdentifiedHolder[] newArray(int size) {
            return new EpisodeIdentifiedHolder[size];
        }
    };

    @Nullable
    public final EpisodeImpl.EpisodeIdentifiedImpl episodeIdentified;

    public EpisodeIdentifiedHolder(EpisodeImpl.EpisodeIdentifiedImpl episodeIdentified) {
        this.episodeIdentified = episodeIdentified;
    }

    private EpisodeIdentifiedHolder(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            episodeIdentified = null;
        } else {
            @Nullable final URL artworkURL = readURL(in);
            @Nullable final Duration duration = readDuration(in);
            final long id = in.readLong();
            @Nullable final Date pubishDate = readDate(in);
            @Nullable final String summary = in.readString();
            @Nullable final String title = in.readString();
            @Nullable final URL url = readURL(in);
            if (title != null && url != null) {
                episodeIdentified = new EpisodeImpl.EpisodeIdentifiedImpl(
                        new EpisodeImpl.EpisodeIdentifierImpl(id),
                        new EpisodeImpl(
                                artworkURL,
                                duration,
                                pubishDate,
                                summary,
                                title,
                                url
                        )
                );
            } else {
                episodeIdentified = null;
            }
        }
    }

    // Object

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodeIdentifiedHolder that = (EpisodeIdentifiedHolder) o;
        return Objects.equals(episodeIdentified, that.episodeIdentified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeIdentified);
    }

    @Override
    public String toString() {
        return "EpisodeIdentifiedHolder{" +
                "episodeIdentified=" + episodeIdentified +
                '}';
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (episodeIdentified == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);

            writeURL(dest, episodeIdentified.model.artworkURL);
            writeDuration(dest, episodeIdentified.model.duration);
            dest.writeLong(episodeIdentified.identifier.id);
            writeDate(dest, episodeIdentified.model.publishDate);
            dest.writeString(episodeIdentified.model.summary);
            dest.writeString(episodeIdentified.model.title);
            writeURL(dest, episodeIdentified.model.url);
        }
    }

    @Nullable
    @Override
    public EpisodeImpl.EpisodeIdentifiedImpl getUnparcelable() {
        return episodeIdentified;
    }
}
