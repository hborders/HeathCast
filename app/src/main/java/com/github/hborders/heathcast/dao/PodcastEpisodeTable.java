package com.github.hborders.heathcast.dao;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.stealthmountain.sqldim.DimDatabase;

final class PodcastEpisodeTable<
        MarkerType,
        PodcastIdentifierType extends Identifier
        > extends Table<MarkerType> {
    PodcastEpisodeTable(DimDatabase<MarkerType> dimDatabase) {
        super(dimDatabase);
    }

    static final String TABLE_PODCAST_EPISODE = "podcast_episode";
    static final String FOREIGN_KEY_PODCAST_EPISODE = TABLE_PODCAST_EPISODE + "_id";

    static final String COLUMN_ID = "_id";
    private static final String COLUMN_PODCAST_ID = PodcastTable.FOREIGN_KEY_PODCAST;
    static final String COLUMN_VERSION = "version";

    static final String CREATE_FOREIGN_KEY_PODCAST_EPISODE =
            "FOREIGN KEY(" + FOREIGN_KEY_PODCAST_EPISODE + ") REFERENCES "
                    + TABLE_PODCAST_EPISODE + "(" + COLUMN_ID + ")";

    static void createPodcastEpisodeTable(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PODCAST_EPISODE + " ("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PODCAST_ID + " INTEGER NOT NULL, "
                + COLUMN_VERSION + " INTEGER NOT NULL, "
                + PodcastTable.CREATE_FOREIGN_KEY_PODCAST + " ON DELETE CASCADE "
                + ")"
        );

        db.execSQL("CREATE INDEX " + TABLE_PODCAST_EPISODE + "__" + COLUMN_PODCAST_ID
                + " ON " + TABLE_PODCAST_EPISODE + "(" + COLUMN_PODCAST_ID + ")");
    }

    long upsertPodcastEpisodeForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        
    }
}
