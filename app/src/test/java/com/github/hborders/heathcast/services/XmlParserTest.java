package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.URLUtil;
import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.EpisodeIdentifier;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public final class XmlParserTest {
    @Test
    public void parseEpisodeList() throws Exception {
        final String xml =
                "<rss " +
                        "xmlns:npr=\"https://www.npr.org/rss/\" " +
                        "xmlns:nprml=\"https://api.npr.org/nprml\" " +
                        "xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" " +
                        "xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" " +
                        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
                        "version=\"2.0\"" +
                        ">" +
                        "  <channel>" +
                        "    <title>Planet Money</title>" +
                        "    <link>https://www.npr.org/planetmoney</link>" +
                        "    <description><![CDATA[" +
                        "\nThe economy explained. Imagine you could call up a friend and say, " +
                        "\"Meet me at the bar and tell me what's going on with the economy.\" " +
                        "Now imagine that's actually a fun evening.\n" +
                        "]]></description>" +
                        "    <copyright>Copyright 2015-2018 NPR - For Personal Use Only</copyright>" +
                        "    <generator>NPR API RSS Generator 0.94</generator>" +
                        "    <language>en</language>" +
                        "    <itunes:new-feed-url>https://www.npr.org/rss/podcast.php?id=510289</itunes:new-feed-url>" +
                        "    <itunes:summary><![CDATA[" +
                        "\nThe economy explained. Imagine you could call up a friend and say, " +
                        "\"Meet me at the bar and tell me what's going on with the economy.\" " +
                        "Now imagine that's actually a fun evening.\n" +
                        "]]></itunes:summary>" +
                        "    <itunes:author>NPR</itunes:author>" +
                        "    <itunes:block>no</itunes:block>" +
                        "    <itunes:owner>" +
                        "      <itunes:email>podcasts@npr.org</itunes:email>" +
                        "      <itunes:name>NPR</itunes:name>" +
                        "    </itunes:owner>" +
                        "    <itunes:category text=\"Business\"/>" +
                        "    <itunes:category text=\"News &amp; Politics\"/>" +
                        "    <itunes:category text=\"Business News\"/>" +
                        "    <itunes:image href=\"https://media.npr.org/assets/img/2018/08/02/npr_planetmoney_podcasttile_sq-7b7fab0b52fd72826936c3dbe51cff94889797a0.jpg?s=1400\"/>" +
                        "    <itunes:type>episodic</itunes:type>" +
                        "    <image>" +
                        "      <url>https://media.npr.org/assets/img/2018/08/02/npr_planetmoney_podcasttile_sq-7b7fab0b52fd72826936c3dbe51cff94889797a0.jpg?s=1400</url>" +
                        "      <title>Planet Money</title>" +
                        "      <link>https://www.npr.org/planetmoney</link>" +
                        "    </image>" +
                        "    <lastBuildDate>Fri, 25 Jan 2019 18:13:00 -0500</lastBuildDate>" +
                        "    <item>" +
                        "      <title>#890: The Division Problem</title>" +
                        "      <description><![CDATA[" +
                        "\nToday on the show, we take on one of life's most vexing problems: Sharing.\n" +
                        "]]></description>" +
                        "      <pubDate>Fri, 25 Jan 2019 18:13:00 -0500</pubDate>" +
                        "      <copyright>Copyright 2015-2018 NPR - For Personal Use Only</copyright>" +
                        "      <guid>3d197872-71e2-4f20-975a-49b3b39bef6f</guid>" +
                        "      <itunes:title>#890: The Division Problem</itunes:title>" +
                        "      <itunes:author>NPR</itunes:author>" +
                        "      <itunes:summary><![CDATA[" +
                        "\nToday on the show, we take on one of life's most vexing problems: Sharing.\n" +
                        "]]></itunes:summary>" +
                        "      <itunes:image href=\"https://media.npr.org/assets/img/2019/01/25/harbor-podcast_wide-2df74dc7ff65b4a6351bbe48f20e68a8da7ec3f2.jpg?s=1400\"/>" +
                        "      <itunes:duration>1272</itunes:duration>" +
                        "      <itunes:explicit>no</itunes:explicit>" +
                        "      <itunes:episodeType>full</itunes:episodeType>" +
                        "      <content:encoded><![CDATA[" +
                        "\nToday on the show, we take on one of life's most vexing problems: Sharing.\n" +
                        "]]></content:encoded>" +
                        "      <enclosure url=\"https://play.podtrac.com/npr-510289/npr.mc.tritondigital.com/NPR_510289/media/anon.npr-mp3/npr/pmoney/2019/01/20190125_pmoney_pmpod890.mp3?orgId=1&amp;d=1272&amp;p=510289&amp;story=688849249&amp;t=podcast&amp;e=688849249&amp;ft=pod&amp;f=510289\" length=\"20318742\" type=\"audio/mpeg\"/>" +
                        "    </item>" +
                        "    <item>" +
                        "      <title>#688: Brilliant Vs. Boring</title>" +
                        "      <description><![CDATA[" +
                        "\nJohn Bogle died last week. His creation — the index fund — " +
                        "changed investing. Today, how his invention set off a million dollar " +
                        "bet between some of the biggest brains on Wall Street, including Warren Buffett.\n" +
                        "]]></description>" +
                        "      <pubDate>Wed, 23 Jan 2019 18:50:00 -0500</pubDate>" +
                        "      <copyright>Copyright 2015-2018 NPR - For Personal Use Only</copyright>" +
                        "      <guid>cd897298-5e00-47be-b006-592674729305</guid>" +
                        "      <itunes:title>#688: Brilliant Vs. Boring</itunes:title>" +
                        "      <itunes:author>NPR</itunes:author>" +
                        "      <itunes:summary><![CDATA[" +
                        "\nJohn Bogle died last week. His creation — the index fund — " +
                        "changed investing. Today, how his invention set off a million dollar " +
                        "bet between some of the biggest brains on Wall Street, including Warren Buffett.\n" +
                        "]]></itunes:summary>" +
                        "      <itunes:image href=\"https://media.npr.org/assets/img/2019/01/23/ap_931123921749_wide-0ea7c0a9c9dd1ee294bb65ba1b26c5154c0012e6.jpg?s=1400\"/>" +
                        "      <itunes:duration>1358</itunes:duration>" +
                        "      <itunes:explicit>no</itunes:explicit>" +
                        "      <itunes:episodeType>full</itunes:episodeType>" +
                        "      <content:encoded><![CDATA[" +
                        "\nJohn Bogle died last week. His creation — the index fund — " +
                        "changed investing. Today, how his invention set off a million dollar " +
                        "bet between some of the biggest brains on Wall Street, including Warren Buffett.\n" +
                        "]]></content:encoded>" +
                        "      <enclosure url=\"https://play.podtrac.com/npr-510289/npr.mc.tritondigital.com/NPR_510289/media/anon.npr-mp3/npr/pmoney/2019/01/20190123_pmoney_pmpod688rerun1.mp3?orgId=1&amp;d=1358&amp;p=510289&amp;story=688018436&amp;t=podcast&amp;e=688018436&amp;ft=pod&amp;f=510289\" length=\"21678996\" type=\"audio/mpeg\"/>" +
                        "    </item>" +
                        "  </channel>" +
                        "</rss>";
        final InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        final EpisodeImpl.EpisodeListImpl actual = new XmlParser<
                EpisodeImpl.EpisodeListImpl,
                EpisodeImpl
                >(
                EpisodeImpl.EpisodeListImpl::new,
                EpisodeImpl.EpisodeListImpl::new,
                EpisodeImpl::new
        ).parseEpisodeList(xmlInputStream);

        assertThat(
                actual,
                equalTo(
                        Arrays.asList(
                                new EpisodeIdentified(
                                        new EpisodeIdentifier(0),
                                        new Episode(
                                                Objects.requireNonNull(URLUtil.fromString("https://media.npr.org/assets/img/2019/01/25/harbor-podcast_wide-2df74dc7ff65b4a6351bbe48f20e68a8da7ec3f2.jpg?s=1400")),
                                                Duration.ofSeconds(20318742),
                                                Date.from(
                                                        ZonedDateTime.of(
                                                                2019,
                                                                1,
                                                                25,
                                                                18,
                                                                13,
                                                                0,
                                                                0,
                                                                ZoneOffset
                                                                        .ofHours(-5)
                                                                        .normalized()
                                                        ).toInstant()
                                                ),
                                                "\nToday on the show, we take on one of life's most vexing problems: Sharing.\n",
                                                "#890: The Division Problem",
                                                Objects.requireNonNull(URLUtil.fromString("https://play.podtrac.com/npr-510289/npr.mc.tritondigital.com/NPR_510289/media/anon.npr-mp3/npr/pmoney/2019/01/20190125_pmoney_pmpod890.mp3?orgId=1&d=1272&p=510289&story=688849249&t=podcast&e=688849249&ft=pod&f=510289"))
                                        )
                                ),
                                new EpisodeIdentified(
                                        new EpisodeIdentifier(0),
                                        new Episode(
                                                Objects.requireNonNull(URLUtil.fromString("https://media.npr.org/assets/img/2019/01/23/ap_931123921749_wide-0ea7c0a9c9dd1ee294bb65ba1b26c5154c0012e6.jpg?s=1400")),
                                                Duration.ofSeconds(21678996),
                                                Date.from(
                                                        ZonedDateTime.of(
                                                                2019,
                                                                1,
                                                                23,
                                                                18,
                                                                50,
                                                                0,
                                                                0,
                                                                ZoneOffset
                                                                        .ofHours(-5)
                                                                        .normalized()
                                                        ).toInstant()
                                                ),
                                                "\nJohn Bogle died last week. His creation — the index fund — " +
                                                        "changed investing. Today, how his invention set off a million dollar " +
                                                        "bet between some of the biggest brains on Wall Street, including Warren Buffett.\n",
                                                "#688: Brilliant Vs. Boring",
                                                Objects.requireNonNull(URLUtil.fromString("https://play.podtrac.com/npr-510289/npr.mc.tritondigital.com/NPR_510289/media/anon.npr-mp3/npr/pmoney/2019/01/20190123_pmoney_pmpod688rerun1.mp3?orgId=1&d=1358&p=510289&story=688018436&t=podcast&e=688018436&ft=pod&f=510289"))
                                        )
                                )
                        )
                )
        );
    }
}
