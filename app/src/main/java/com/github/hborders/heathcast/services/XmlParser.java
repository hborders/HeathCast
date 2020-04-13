package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.android.DurationUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.URLUtil;
import com.github.hborders.heathcast.dao.Episode2;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.hborders.heathcast.core.StringUtil.isEmpty;

final class XmlParser<
        EpisodeListType extends Episode2.EpisodeList2<EpisodeType>,
        EpisodeType extends Episode2
        > {
    private static final class Enclosure {
        @Nullable
        final Duration duration;
        @Nullable
        final URL url;

        public Enclosure(
                @Nullable Duration duration,
                @Nullable URL url
        ) {
            this.duration = duration;
            this.url = url;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Enclosure enclosure = (Enclosure) o;
            return Objects.equals(duration, enclosure.duration) &&
                    Objects.equals(url, enclosure.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(duration, url);
        }

        @Override
        public String toString() {
            return "Enclosure{" +
                    "duration=" + duration +
                    ", url=" + url +
                    '}';
        }
    }

    private static final String ITUNES_NAMESPACE = "http://www.itunes.com/dtds/podcast-1.0.dtd";
    private static final DateFormat PUB_DATE_DATE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            Locale.US
    );

    private final CollectionFactory.Capacity<
            EpisodeListType,
            EpisodeType
            > episodeListCapacityFactory;
    private final CollectionFactory.Empty<
            EpisodeListType,
            EpisodeType
            > episodeListEmptyFactory;
    private final Episode2.EpisodeFactory2<EpisodeType> episodeFactory;

    XmlParser(
            CollectionFactory.Capacity<
                    EpisodeListType,
                    EpisodeType
                    > episodeListCapacityFactory,
            CollectionFactory.Empty<
                    EpisodeListType,
                    EpisodeType
                    > episodeListEmptyFactory,
            Episode2.EpisodeFactory2<EpisodeType> episodeFactory
    ) {
        this.episodeListCapacityFactory = episodeListCapacityFactory;
        this.episodeListEmptyFactory = episodeListEmptyFactory;
        this.episodeFactory = episodeFactory;
    }

    EpisodeListType parseEpisodeList(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            xmlPullParserFactory.setValidating(false);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            return readEpisodesFromRss(parser);
        } finally {
            in.close();
        }
    }

    private EpisodeListType readEpisodesFromRss(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(
                XmlPullParser.START_TAG,
                "",
                "rss"
        );
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            @Nullable final String tagName = parser.getName();
            if ("channel".equals(tagName)) {
                return readEpisodesFromChannel(parser);
            } else {
                skip(parser);
            }
        }

        return episodeListEmptyFactory.newCollection();
    }

    private EpisodeListType readEpisodesFromChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(
                XmlPullParser.START_TAG,
                "",
                "channel"
        );

        final EpisodeListType episodes =
                episodeListCapacityFactory.newCollection(0);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            @Nullable final String tagName = parser.getName();
            if ("item".equals(tagName)) {
                @Nullable final EpisodeType episode = readEpisode(parser);
                if (episode != null) {
                    episodes.add(episode);
                }
            } else {
                skip(parser);
            }
        }

        return episodes;
    }

    @Nullable
    private EpisodeType readEpisode(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(
                XmlPullParser.START_TAG,
                "",
                "item"
        );

        @Nullable URL artworkURL = null;
        @Nullable Duration duration = null;
        @Nullable Date publishDate = null;
        @Nullable String summary = null;
        @Nullable String title = null;
        @Nullable URL url = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            @Nullable final String tagName = parser.getName();
            @Nullable final String tagNamespace = parser.getNamespace();
            if ("image".equals(tagName) &&
                    ITUNES_NAMESPACE.equals(tagNamespace)) {
                artworkURL = readITunesImageURL(parser);
            } else if ("enclosure".equals(tagName) &&
                    "".equals(tagNamespace)) {
                @Nonnull final Enclosure enclosure = readEnclosure(parser);
                duration = enclosure.duration;
                url = enclosure.url;
            } else if ("description".equals(tagName) &&
                    "".equals(tagNamespace)) {
                summary = readText(parser);
            } else if ("title".equals(tagName) &&
                    "".equals(tagNamespace)) {
                title = readText(parser);
            } else if ("pubDate".equals(tagName) &&
                    "".equals(tagNamespace)) {
                @Nullable final String dateString = readText(parser);
                if (dateString != null) {
                    try {
                        publishDate = PUB_DATE_DATE_FORMAT.parse(dateString);
                    } catch (ParseException e) {
                        // ignore
                    }
                }
            } else {
                skip(parser);
            }
        }

        if (!isEmpty(title) &&
                url != null) {
            return episodeFactory.newEpisode(
                    artworkURL,
                    duration,
                    publishDate,
                    summary,
                    title,
                    url
            );
        } else {
            return null;
        }
    }

    @Nullable
    private static URL readITunesImageURL(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(
                XmlPullParser.START_TAG,
                ITUNES_NAMESPACE,
                "image"
        );

        @Nullable final String href = parser.getAttributeValue(
                "",
                "href"
        );
        skip(parser);
        return URLUtil.fromString(href);
    }

    @Nonnull
    private static Enclosure readEnclosure(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(
                XmlPullParser.START_TAG,
                "",
                "enclosure"
        );

        @Nullable final String urlString = parser.getAttributeValue(
                "",
                "url"
        );
        @Nullable final URL url = URLUtil.fromString(urlString);
        @Nullable final String length = parser.getAttributeValue(
                "",
                "length"
        );
        @Nullable final Duration duration = DurationUtil.ofSecondsString(length);
        skip(parser);
        return new Enclosure(
                duration,
                url
        );
    }

    @Nullable
    private static String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        @Nullable String firstText = null;
        @Nullable StringBuilder accumulatingStringBuilder = null;
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
                case XmlPullParser.TEXT:
                    @Nullable final String text = parser.getText();
                    if (text != null) {
                        if (firstText == null) {
                            firstText = text;
                        } else {
                            @Nullable final StringBuilder capturedStringBuilder = accumulatingStringBuilder;
                            if (capturedStringBuilder == null) {
                                accumulatingStringBuilder = new StringBuilder(
                                        firstText.length() + text.length()
                                )
                                        .append(firstText)
                                        .append(text);
                            } else {
                                capturedStringBuilder.append(text);
                            }
                        }
                    }
                    break;
            }
        }

        @Nullable final StringBuilder capturedStringBuilder = accumulatingStringBuilder;
        if (capturedStringBuilder != null) {
            return capturedStringBuilder.toString();
        } else {
            return firstText;
        }
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
