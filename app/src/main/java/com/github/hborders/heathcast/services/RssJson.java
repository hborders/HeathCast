package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.utils.URLUtil;

import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import simplexml.annotations.XmlAttribute;

final class RssJson {
    @Nullable
    public Channel channel;

    @Nullable
    Channel getChannel() {
        return channel;
    }

    static final class Channel {
        @Nullable
        public List<Item> items;

        @Nullable
        List<Item> getItems() {
            return items;
        }

        static final class Item {
            @Nullable
            public String title;
            @Nullable
            public String description;
            @Nullable
            public Enclosure enclosure;

            @Nullable
            Episode toEpisode() {
                @Nullable final String title = this.title;
                @Nullable final Enclosure enclosure = this.enclosure;
                @Nullable final URL url;
                if (enclosure == null) {
                    url = null;
                } else {
                    url = URLUtil.fromString(enclosure.url);
                }
                if (title != null && url != null) {
                    return new Episode(
                            null,
                            null,
                            null,
                            title,
                            url
                    );
                } else {
                    return null;
                }
            }

            static final class Enclosure {
                @XmlAttribute
                @Nullable
                public String url;
                @XmlAttribute
                @Nullable
                public Long length;
                @XmlAttribute
                @Nullable
                public String type;
            }
        }
    }
}