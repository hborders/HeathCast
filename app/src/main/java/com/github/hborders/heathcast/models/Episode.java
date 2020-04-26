package com.github.hborders.heathcast.models;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Opt;

import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface Episode {
    interface EpisodeFactory2<EpisodeType extends Episode> {
        EpisodeType newEpisode(
                @Nullable URL artworkURL,
                @Nullable Duration duration,
                @Nullable Date publishDate,
                @Nullable String summary,
                String title,
                URL url
        );
    }

    interface EpisodeIdentified<
            EpisodeIdentifierType extends EpisodeIdentifier,
            EpisodeType extends Episode
            > extends Identified<
            EpisodeIdentifierType,
            EpisodeType
            >, Episode {
        interface EpisodeIdentifiedList2<
                EpisodeIdentifiedType extends EpisodeIdentified<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
                EpisodeIdentifierType extends EpisodeIdentifier,
                EpisodeType extends Episode
                > extends List<EpisodeIdentifiedType> {
            interface EpisodeIdentifiedListVersioned<
                    EpisodeIdentifiedListType extends EpisodeIdentifiedList2<
                            EpisodeIdentifiedType,
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifiedType extends EpisodeIdentified<
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifierType extends EpisodeIdentifier,
                    EpisodeType extends Episode
                    > extends Versioned<EpisodeIdentifiedListType> {
            }
        }

        interface EpisodeIdentifiedOpt<
                EpisodeIdentifiedType extends EpisodeIdentified<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
                EpisodeIdentifierType extends EpisodeIdentifier,
                EpisodeType extends Episode
                > extends Opt<EpisodeIdentifiedType> {
            interface EpisodeIdentifiedOptList2<
                    EpisodeIdentifiedOptType extends EpisodeIdentifiedOpt<
                            EpisodeIdentifiedType,
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifiedType extends EpisodeIdentified<
                            EpisodeIdentifierType,
                            EpisodeType
                            >,
                    EpisodeIdentifierType extends EpisodeIdentifier,
                    EpisodeType extends Episode
                    > extends List<EpisodeIdentifiedOptType> {
            }
        }

        interface EpisodeIdentifiedSet2<
                EpisodeIdentifiedType extends EpisodeIdentified<
                        EpisodeIdentifierType,
                        EpisodeType
                        >,
                EpisodeIdentifierType extends EpisodeIdentifier,
                EpisodeType extends Episode
                > extends Set<EpisodeIdentifiedType> {
        }
    }

    interface EpisodeIdentifier extends Identifier {
        interface EpisodeIdentifierOpt<
                EpisodeIdentifierType extends EpisodeIdentifier
                > extends Opt<EpisodeIdentifierType> {
            interface EpisodeIdentifierOptList2<
                    EpisodeIdentifierOptType extends EpisodeIdentifierOpt<
                            EpisodeIdentifierType
                            >,
                    EpisodeIdentifierType extends EpisodeIdentifier
                    > extends List<EpisodeIdentifierOptType> {
            }
        }
    }

    interface EpisodeList2<
            EpisodeType extends Episode
            > extends List<EpisodeType> {
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
