package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import io.reactivex.observers.TestObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class EpisodeTableTest extends AbstractDatabaseTest {

    private EpisodeTable getTestObject() {
        return getDatabase().episodeTable;
    }

    private PodcastTable getPodcastTable() {
        return getDatabase().podcastTable;
    }

    @Test
    public void testUpsertNewEpisodeWithSameURLThriceTogetherInsertsFirstEpisodeAndOthers() throws Exception {
        final Podcast podcast1 = new Podcast(
                new URL("http://example.com/artwork"),
                "author",
                new URL("http://example.com/feed"),
                "name"
        );
        @Nullable final Identifier<Podcast> podcastIdentifier1 =
                getPodcastTable().insertPodcast(podcast1).orElse(null);
        if (podcastIdentifier1 == null) {
            fail();
        } else {
            final Episode episode1 = new Episode(
                    new URL("http://example.com/episode11/artwork"),
                    Duration.ofSeconds(1),
                    Date.from(
                            ZonedDateTime
                                    .of(
                                            2019,
                                            1,
                                            1,
                                            0,
                                            0,
                                            0,
                                            0,
                                            ZoneOffset
                                                    .ofHours(0)
                                                    .normalized()
                                    )
                                    .toInstant()
                    ),
                    "summary1",
                    "title1",
                    new URL("http://example.com/episode")
            );
            final Episode episode2 = new Episode(
                    new URL("http://example.com/episode2/artwork"),
                    Duration.ofSeconds(2),
                    Date.from(
                            ZonedDateTime
                                    .of(
                                            2019,
                                            2,
                                            2,
                                            0,
                                            0,
                                            0,
                                            0,
                                            ZoneOffset
                                                    .ofHours(0)
                                                    .normalized()
                                    )
                                    .toInstant()
                    ),
                    "summary2",
                    "title12",
                    new URL("http://example.com/episode")
            );
            final Episode episode3 = new Episode(
                    new URL("http://example.com/episode3/artwork"),
                    Duration.ofSeconds(3),
                    Date.from(
                            ZonedDateTime
                                    .of(
                                            2019,
                                            3,
                                            3,
                                            0,
                                            0,
                                            0,
                                            0,
                                            ZoneOffset
                                                    .ofHours(0)
                                                    .normalized()
                                    )
                                    .toInstant()
                    ),
                    "summary3",
                    "title3",
                    new URL("http://example.com/episode")
            );

            final List<Episode> upsertingEpisodes = Arrays.asList(
                    episode1,
                    episode2,
                    episode3
            );
            final List<Optional<Identifier<Episode>>> episodeIdentifierOptionals =
                    getTestObject().upsertEpisodes(
                            podcastIdentifier1,
                            upsertingEpisodes
                    );
            if (episodeIdentifierOptionals.size() != upsertingEpisodes.size()) {
                fail("Expected " + upsertingEpisodes.size() + ", got episodeIdentifiers: " + episodeIdentifierOptionals);
            } else {
                @Nullable final Identifier<Episode> episodeIdentifier1 =
                        episodeIdentifierOptionals.get(0).orElse(null);
                if (episodeIdentifier1 == null) {
                    fail();
                } else {
                    assertThat(
                            episodeIdentifierOptionals,
                            is(
                                    Collections.nCopies(
                                            upsertingEpisodes.size(),
                                            Optional.of(episodeIdentifier1)
                                    )
                            )
                    );

                    final TestObserver<Set<Identified<Episode>>> episodeTestObserver = new TestObserver<>();
                    getTestObject()
                            .observeQueryForAllEpisodeIdentifieds()
                            .subscribe(episodeTestObserver);

                    episodeTestObserver.assertValue(
                            Collections.singleton(
                                    new Identified<>(
                                            episodeIdentifier1,
                                            episode1
                                    )
                            )
                    );
                }
            }
        }
    }
}
