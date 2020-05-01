PRAGMA foreign_keys = off;

-- Table: episode
CREATE TABLE episode (
    id                             INTEGER PRIMARY KEY AUTOINCREMENT
                                           NOT NULL,
    artwork_url                    TEXT,
    duration                       INTEGER,
    publish_time_millis            INTEGER,
    sort                           INTEGER UNIQUE
                                           NOT NULL
                                           DEFAULT (0),
    summary                        TEXT,
    title                          TEXT    NOT NULL,
    url                            TEXT    NOT NULL,
    podcast_episode_list_header_id INTEGER REFERENCES podcast_episode_list_header (_id) ON DELETE CASCADE
                                           NOT NULL
);


-- Table: podcast
CREATE TABLE podcast (
    artwork_url TEXT,
    author      TEXT,
    feed_url    TEXT    NOT NULL
                        UNIQUE,
    _id         INTEGER NOT NULL
                        PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL
);


-- Table: podcast_episode_list_header
CREATE TABLE podcast_episode_list_header (
    _id        INTEGER PRIMARY KEY AUTOINCREMENT
                       NOT NULL,
    version    INTEGER NOT NULL
                       DEFAULT (0),
    podcast_id INTEGER NOT NULL
                       REFERENCES podcast (_id) ON DELETE CASCADE
);


-- Table: search
CREATE TABLE search (
    _id    INTEGER PRIMARY KEY AUTOINCREMENT
                   NOT NULL,
    search TEXT    NOT NULL
                   UNIQUE,
    sort   INTEGER NOT NULL
                   UNIQUE
                   DEFAULT (0) 
);


-- Table: search_podcast_result
CREATE TABLE search_podcast_result (
    _id        INTEGER PRIMARY KEY AUTOINCREMENT
                       NOT NULL,
    podcast_id INTEGER REFERENCES podcast (_id) ON DELETE CASCADE
                       NOT NULL,
    search_id  INTEGER REFERENCES search (_id) ON DELETE CASCADE
                       NOT NULL,
    sort       INTEGER NOT NULL,
    UNIQUE (
        podcast_id,
        search_id,
        sort
    )
);


-- Table: subscription
CREATE TABLE subscription (
    _id                         INTEGER PRIMARY KEY AUTOINCREMENT
                                        NOT NULL,
    podcast_id                  INTEGER REFERENCES podcast (_id) ON DELETE CASCADE
                                        NOT NULL
                                        UNIQUE,
    sort                        REAL    NOT NULL
                                        UNIQUE
                                        DEFAULT (0),
    subscription_list_header_id INTEGER REFERENCES subscription_list_header (_id) ON DELETE CASCADE
                                        NOT NULL
);


-- Table: subscription_list_header
CREATE TABLE subscription_list_header (
    _id     INTEGER PRIMARY KEY AUTOINCREMENT
                    NOT NULL,
    version INTEGER NOT NULL
                    DEFAULT (0) 
);


-- Index: episode__podcast_episode_list_header_id_index
CREATE INDEX episode__podcast_episode_list_header_id_index ON episode (
    podcast_episode_list_header_id
);


-- Index: episode__publish_time_millis_index
CREATE INDEX episode__publish_time_millis_index ON episode (
    publish_time_millis
);


-- Index: episode__sort_index
CREATE INDEX episode__sort_index ON episode (
    sort
);


-- Index: episode__url_index
CREATE INDEX episode__url_index ON episode (
    url
);


-- Index: podcast__feed_url_index
CREATE UNIQUE INDEX podcast__feed_url_index ON podcast (
    feed_url
);


-- Index: podcast_episode_list_header__podcast_id_index
CREATE UNIQUE INDEX podcast_episode_list_header__podcast_id_index ON podcast_episode_list_header (
    podcast_id
);


-- Index: search__search_index
CREATE UNIQUE INDEX search__search_index ON search (
    search
);


-- Index: search__sort_index
CREATE UNIQUE INDEX search__sort_index ON search (
    sort
);


-- Index: search_podcast_result__podcast_id_index
CREATE INDEX search_podcast_result__podcast_id_index ON search_podcast_result (
    podcast_id
);


-- Index: search_podcast_result__search_id_index
CREATE INDEX search_podcast_result__search_id_index ON search_podcast_result (
    search_id
);


-- Index: search_podcast_result__sort_index
CREATE INDEX search_podcast_result__sort_index ON search_podcast_result (
    sort
);


-- Index: subscription__podcast_id_index
CREATE UNIQUE INDEX subscription__podcast_id_index ON subscription (
    podcast_id
);


-- Index: subscription__sort_index
CREATE UNIQUE INDEX subscription__sort_index ON subscription (
    sort
);


-- Trigger: episode__update_podcast_episode_list_header_version_after_update_trigger
CREATE TRIGGER episode__update_podcast_episode_list_header_version_after_update_trigger
        BEFORE UPDATE
            ON episode
      FOR EACH ROW
BEGIN
    UPDATE podcast_episode_list_header
       SET version = (
               SELECT MAX(version) + 1
                 FROM podcast_episode_list_header
           )
     WHERE podcast_episode_list_header._id = old.podcast_episode_list_header_id;
END;


-- Trigger: episode__update_sort_after_insert_trigger
CREATE TRIGGER episode__update_sort_after_insert_trigger
         AFTER INSERT
            ON Episode
      FOR EACH ROW
BEGIN
    UPDATE episode
       SET sort = (
               SELECT MAX(sort) + 1
                 FROM episode
           )
     WHERE _id = NEW._id;
END;


-- Trigger: search__update_sort_after_insert_trigger
CREATE TRIGGER search__update_sort_after_insert_trigger
         AFTER INSERT
            ON search
      FOR EACH ROW
BEGIN
    UPDATE search
       SET sort = (
               SELECT MAX(SORT) + 1
                 FROM search
           )
     WHERE _id = NEW._id;
END;


-- Trigger: search__update_sort_after_update_trigger
CREATE TRIGGER search__update_sort_after_update_trigger
         AFTER UPDATE
            ON search
      FOR EACH ROW
BEGIN
    UPDATE search
       SET sort = (
               SELECT MAX(SORT) + 1
                 FROM search
           )
     WHERE _id = OLD._id AND 
           sort != (
                       SELECT MAX(sort) 
                         FROM search
                   );
END;


-- Trigger: subscription__update_sort_after_insert_trigger
CREATE TRIGGER subscription__update_sort_after_insert_trigger
         AFTER INSERT
            ON subscription
      FOR EACH ROW
BEGIN
    UPDATE subscription
       SET sort = (
               SELECT IFNULL(MAX(sort), 0) + 1
                 FROM subscription
           )
     WHERE _id = NEW._id;
END;


-- Trigger: subscription__update_subscription_list_header_version_after_update_trigger
CREATE TRIGGER subscription__update_subscription_list_header_version_after_update_trigger
         AFTER UPDATE
            ON subscription
      FOR EACH ROW
BEGIN
    UPDATE subscription_list_header
       SET version = (
               SELECT MAX(version) + 1
                 FROM subscription_list_header
           )
     WHERE _id = OLD.subscription_list_header_id;
END;

PRAGMA foreign_keys = on;
