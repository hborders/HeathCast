package com.github.hborders.heathcast.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.utils.URLUtil;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.utils.ParcelUtil.readDuration;
import static com.github.hborders.heathcast.utils.ParcelUtil.readURL;
import static com.github.hborders.heathcast.utils.ParcelUtil.writeDuration;
import static com.github.hborders.heathcast.utils.ParcelUtil.writeURL;

public final class EpisodeHolder implements Parcelable, UnparcelableHolder<Episode> {
    @Nullable
    public final Episode mEpisode;

    public EpisodeHolder(Episode episode) {
        this.mEpisode = episode;
    }

    private EpisodeHolder(Parcel in) {
        byte zeroIsNull = in.readByte();
        if (zeroIsNull == (byte) 0) {
            mEpisode = null;
        } else {
            @Nullable final URL artworkURL = readURL(in);
            @Nullable final Duration duration = readDuration(in);
            @Nullable final String summary = in.readString();
            @Nullable final String title = in.readString();
            @Nullable final URL url = readURL(in);
            if (title != null && url != null) {
                mEpisode = new Episode(
                        artworkURL,
                        duration,
                        summary,
                        title,
                        url
                );
            } else {
                mEpisode = null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodeHolder that = (EpisodeHolder) o;
        return Objects.equals(mEpisode, that.mEpisode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mEpisode);
    }

    @Override
    public String toString() {
        return "EpisodeHolder{" +
                "mEpisode=" + mEpisode +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mEpisode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);

            writeURL(dest, mEpisode.mArtworkURL);
            writeDuration(dest, mEpisode.mDuration);
            dest.writeString(mEpisode.mSummary);
            dest.writeString(mEpisode.mTitle);
            writeURL(dest, mEpisode.mURL);
        }
    }

    @Nullable
    @Override
    public Episode getUnparcelable() {
        return mEpisode;
    }

    public static final Creator<EpisodeHolder> CREATOR = new Creator<EpisodeHolder>() {
        @Override
        public EpisodeHolder createFromParcel(Parcel in) {
            return new EpisodeHolder(in);
        }

        @Override
        public EpisodeHolder[] newArray(int size) {
            return new EpisodeHolder[size];
        }
    };
}
