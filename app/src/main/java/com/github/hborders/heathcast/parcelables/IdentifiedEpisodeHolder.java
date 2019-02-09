package com.github.hborders.heathcast.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.utils.ParcelUtil.readDuration;
import static com.github.hborders.heathcast.utils.ParcelUtil.readURL;
import static com.github.hborders.heathcast.utils.ParcelUtil.writeDuration;
import static com.github.hborders.heathcast.utils.ParcelUtil.writeURL;

public final class IdentifiedEpisodeHolder implements Parcelable, UnparcelableHolder<Identified<Episode>> {
    @Nullable
    public final Identified<Episode> mIdentifiedEpisode;

    public IdentifiedEpisodeHolder(Identified<Episode> identifiedEpisode) {
        this.mIdentifiedEpisode = identifiedEpisode;
    }

    private IdentifiedEpisodeHolder(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            mIdentifiedEpisode = null;
        } else {
            @Nullable final URL artworkURL = readURL(in);
            @Nullable final Duration duration = readDuration(in);
            final long id = in.readLong();
            @Nullable final String summary = in.readString();
            @Nullable final String title = in.readString();
            @Nullable final URL url = readURL(in);
            if (title != null && url != null) {
                mIdentifiedEpisode = new Identified<>(
                        new Identifier<>(
                                Episode.class,
                                id
                        ),
                        new Episode(
                                artworkURL,
                                duration,
                                summary,
                                title,
                                url
                        )
                );
            } else {
                mIdentifiedEpisode = null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifiedEpisodeHolder that = (IdentifiedEpisodeHolder) o;
        return Objects.equals(mIdentifiedEpisode, that.mIdentifiedEpisode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mIdentifiedEpisode);
    }

    @Override
    public String toString() {
        return "IdentifiedEpisodeHolder{" +
                "mIdentifiedEpisode=" + mIdentifiedEpisode +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mIdentifiedEpisode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);

            writeURL(dest, mIdentifiedEpisode.mModel.mArtworkURL);
            writeDuration(dest, mIdentifiedEpisode.mModel.mDuration);
            dest.writeLong(mIdentifiedEpisode.mIdentifier.mId);
            dest.writeString(mIdentifiedEpisode.mModel.mSummary);
            dest.writeString(mIdentifiedEpisode.mModel.mTitle);
            writeURL(dest, mIdentifiedEpisode.mModel.mURL);
        }
    }

    @Nullable
    @Override
    public Identified<Episode> getUnparcelable() {
        return mIdentifiedEpisode;
    }

    public static final Creator<IdentifiedEpisodeHolder> CREATOR = new Creator<IdentifiedEpisodeHolder>() {
        @Override
        public IdentifiedEpisodeHolder createFromParcel(Parcel in) {
            return new IdentifiedEpisodeHolder(in);
        }

        @Override
        public IdentifiedEpisodeHolder[] newArray(int size) {
            return new IdentifiedEpisodeHolder[size];
        }
    };
}
