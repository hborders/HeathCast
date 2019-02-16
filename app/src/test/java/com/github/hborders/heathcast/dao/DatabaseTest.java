package com.github.hborders.heathcast.dao;

import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.Arrays;

import javax.annotation.Nullable;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest extends AbstractDatabaseTest {

    private Database getTestObject() {
        return getDatabase();
    }

    @Test
    public void testReplacePodcastSearchResultsForFirstPodcastSearch() throws Exception {
        @Nullable final Identifier<PodcastSearch> podcastSearchIdentifier =
                getTestObject().insertPodcastSearch(new PodcastSearch("Planet Money"));
        if (podcastSearchIdentifier == null) {
            fail();
        } else {
            getTestObject().replacePodcastSearchResults(
                    podcastSearchIdentifier,
                    Arrays.asList(
                            new Podcast(
                                    new URL("http://npr.org/planetmoney/artwork"),
                                    "Alex Goldman",
                                    new URL("http://npr.com/planetmoney/feed"),
                                    "Planet Money"
                            ),
                            new Podcast(
                                    new URL("http://npr.org/theindicator/artwork"),
                                    "Stacey Vanek-Smith",
                                    new URL("http://npr.com/theindicator/feed"),
                                    "The Indicator"
                            )
                    )
            );
        }
    }
}