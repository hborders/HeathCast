package com.github.hborders.heathcast.dao;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Opt2;

import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface Episode2 {
    interface EpisodeFactory2<EpisodeType extends Episode2> {
        EpisodeType newEpisode(
                @Nullable URL artworkURL,
                @Nullable Duration duration,
                @Nullable Date publishDate,
                @Nullable String summary,
                String title,
                URL url
        );
    }

    interface EpisodeIdentified2<
            EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
            EpisodeType extends Episode2
            > extends Identified2<
            EpisodeIdentifierType,
            EpisodeType
            >, Episode2 {
        interface EpisodeIdentifiedList2<
                EpisodeIdentifiedType extends EpisodeIdentified2<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
                EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
                EpisodeType extends Episode2
                > extends List<EpisodeIdentifiedType> {
        }

        interface EpisodeIdentifiedOpt2<
                EpisodeIdentifiedType extends EpisodeIdentified2<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
                EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
                EpisodeType extends Episode2
                > extends Opt2<EpisodeIdentifiedType> {
            interface EpisodeIdentifiedOptList2<
                    EpisodeIdentifiedOptType extends EpisodeIdentifiedOpt2<
                            EpisodeIdentifiedType,
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifiedType extends EpisodeIdentified2<
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
                    EpisodeType extends Episode2
                    > extends List<EpisodeIdentifiedOptType> {
            }
        }

        interface EpisodeIdentifiedSet2<
                EpisodeIdentifiedType extends EpisodeIdentified2<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
                EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
                EpisodeType extends Episode2
                > extends Set<EpisodeIdentifiedType> {
        }
    }

    interface EpisodeIdentifier2 extends Identifier2 {
        interface EpisodeIdentifierOpt2<
                EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
                EpisodeType extends Episode2
                > extends Opt2<EpisodeIdentifierType> {
            interface EpisodeIdentifierOptList2<
                    EpisodeIdentifierOptType extends EpisodeIdentifierOpt2<
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifierType extends EpisodeIdentified2.EpisodeIdentifier2,
                    EpisodeType extends Episode2
                    > extends List<EpisodeIdentifierOptType> {
            }
        }
    }

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
