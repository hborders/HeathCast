package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.features.model.IdentifiedImpl;
import com.github.hborders.heathcast.features.model.IdentifierImpl;
import com.github.hborders.heathcast.models.Identifier;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.Objects;
import java.util.Optional;

final class PodcastEpisodeListHeaderTable<
        MarkerType,
        PodcastIdentifierType extends Identifier
        > extends Table<MarkerType> {
    static final String TABLE_PODCAST_EPISODE_LIST_HEADER = "podcast_episode_list_header";
    static final String FOREIGN_KEY_PODCAST_EPISODE_LIST = TABLE_PODCAST_EPISODE_LIST_HEADER + "_id";

    static final String COLUMN_ID = "_id";
    private static final String COLUMN_PODCAST_ID = PodcastTable.FOREIGN_KEY_PODCAST;
    static final String COLUMN_VERSION = "version";

    static final String CREATE_FOREIGN_KEY_PODCAST_EPISODE_LIST_HEADER =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST_EPISODE_LIST + ") REFERENCES "
                    + TABLE_PODCAST_EPISODE_LIST_HEADER + "(" + COLUMN_ID + ")";

    private static final UpsertAdapter<Long> upsertAdapter = new SingleColumnSecondaryKeyUpsertAdapter<>(
            TABLE_PODCAST_EPISODE_LIST_HEADER,
            COLUMN_ID,
            COLUMN_PODCAST_ID,
            cursor -> CursorUtil.getNonnullLong(cursor, COLUMN_PODCAST_ID)
    );

    private static final class PodcastEpisodeListHeader {
        final long podcastId;
        final long version;

        private PodcastEpisodeListHeader(
                long podcastId,
                long version
        ) {
            this.podcastId = podcastId;
            this.version = version;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PodcastEpisodeListHeader that = (PodcastEpisodeListHeader) o;
            return version == that.version &&
                    podcastId == that.podcastId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(podcastId, version);
        }

        @Override
        public String toString() {
            return "PodcastEpisodeListHeader{" +
                    "podcastId=" + podcastId +
                    ", version=" + version +
                    '}';
        }
    }

    private static final class PodcastEpisodeListHeaderIdentifier extends IdentifierImpl {
        PodcastEpisodeListHeaderIdentifier(long id) {
            super(id);
        }
    }

    private static final class PodcastEpisodeListHeaderIdentified<
            > extends IdentifiedImpl<
            PodcastEpisodeListHeaderIdentifier,
            PodcastEpisodeListHeader
            > {

        PodcastEpisodeListHeaderIdentified(
                PodcastEpisodeListHeaderIdentifier identifier,
                PodcastEpisodeListHeader model
        ) {
            super(
                    identifier,
                    model
            );
        }
    }

    static void createPodcastEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST_EPISODE_LIST_HEADER + " ("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PODCAST_ID + " INTEGER NOT NULL, "
                + COLUMN_VERSION + " INTEGER NOT NULL, "
                + PodcastTable.CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                + ")"
        );

        db.execSQL("CREATE INDEX " + TABLE_PODCAST_EPISODE_LIST_HEADER + "__" + COLUMN_PODCAST_ID
                + " ON " + TABLE_PODCAST_EPISODE_LIST_HEADER + "(" + COLUMN_PODCAST_ID + ")");
    }

    PodcastEpisodeListHeaderTable(DimDatabase<MarkerType> dimDatabase) {
        super(dimDatabase);
    }

    long upsertPodcastEpisodeForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        return <> upsertModel2(
                upsertAdapter,
                Long.class,
                podcastIdentifier,
                PodcastIdentifierType::getId,
                PodcastEpisodeListHeaderIdentifier::new,
                PodcastEpisodeListHeaderIdentified::new,
                this::insertPodcastEpisodeListHeader,
                this::updatePodcastEpisodeListHeader
        );
    }

    Optional<PodcastEpisodeListHeaderIdentifier> insertPodcastEpisodeListHeader(
            PodcastEpisodeListHeader podcastEpisodeListHeader
    ) {

    }

    int updatePodcastEpisodeListHeader(PodcastEpisodeListHeader podcastEpisodeListHeader) {

    }
}
