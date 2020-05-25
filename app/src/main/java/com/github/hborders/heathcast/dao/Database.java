package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.android.ResourcesUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Function2;
import com.github.hborders.heathcast.core.ListUtil;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.core.SortedSetUtil;
import com.github.hborders.heathcast.core.Tuple;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.Subscription;
import com.stealthmountain.sqldim.DimDatabase;
import com.stealthmountain.sqldim.SqlDim;
import com.stealthmountain.sqldim.SqlDim.MarkedQuery.MarkedValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_ABORT;
import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;
import static com.github.hborders.heathcast.core.ListUtil.indexedStream;

public final class Database<
        MarkerType,
        EpisodeType extends Episode,
        EpisodeIdentifiedType extends Episode.EpisodeIdentified<
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedListType extends Episode.EpisodeIdentified.EpisodeIdentifiedList2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifiedSetType extends Episode.EpisodeIdentified.EpisodeIdentifiedSet2<
                EpisodeIdentifiedType,
                EpisodeIdentifierType,
                EpisodeType
                >,
        EpisodeIdentifierType extends Episode.EpisodeIdentifier,
        EpisodeListType extends Episode.EpisodeList2<
                EpisodeType
                >,
        PodcastType extends Podcast,
        PodcastIdentifiedType extends Podcast.PodcastIdentified<
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedListType extends Podcast.PodcastIdentified.PodcastIdentifiedList2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifiedSetType extends Podcast.PodcastIdentified.PodcastIdentifiedSet2<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        PodcastIdentifierType extends Podcast.PodcastIdentifier,
        PodcastSearchType extends PodcastSearch,
        PodcastSearchIdentifiedType extends PodcastSearch.PodcastSearchIdentified<
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifiedListType extends PodcastSearch.PodcastSearchIdentified.PodcastSearchIdentifiedList2<
                PodcastSearchIdentifiedType,
                PodcastSearchIdentifierType,
                PodcastSearchType
                >,
        PodcastSearchIdentifierType extends PodcastSearch.PodcastSearchIdentifier,
        SubscriptionType extends Subscription<
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifiedType extends Subscription.SubscriptionIdentified<
                SubscriptionIdentifierType,
                SubscriptionType,
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifiedListType extends Subscription.SubscriptionIdentified.SubscriptionIdentifiedList2<
                SubscriptionIdentifiedType,
                SubscriptionIdentifierType,
                SubscriptionType,
                PodcastIdentifiedType,
                PodcastIdentifierType,
                PodcastType
                >,
        SubscriptionIdentifierType extends Subscription.SubscriptionIdentifier
        > {
    static final class Schema extends Callback {
        private Context context;

        Schema(Context context) {
            super(1);

            this.context = context;
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            final String sql;
            try {
                sql = ResourcesUtil.readRawUtf8Resource(
                        context.getResources(),
                        R.raw.heathcast
                );
            } catch (IOException rethrown) {
                throw new IllegalStateException(rethrown);
            }

            db.execSQL(sql);
        }

        public void onConfigure(SupportSQLiteDatabase db) {
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onUpgrade(
                SupportSQLiteDatabase db,
                int oldVersion,
                int newVersion
        ) {
            throw new AssertionError();
        }
    }

    private static <
            IdentifierType extends Identifier
            > String[] idStrings2(Collection<IdentifierType> identifiers) {
        final String[] idStrings = new String[identifiers.size()];
        int i = 0;
        for (IdentifierType identifier : identifiers) {
            idStrings[i] = Long.toString(identifier.getId());
            i++;
        }

        return idStrings;
    }

    private interface UpsertAdapter<SecondaryKeyType> {
        SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<SecondaryKeyType> secondaryKeys);

        long getPrimaryKey(Cursor primaryAndSecondaryKeyCursor);

        SecondaryKeyType getSecondaryKey(Cursor primaryAndSecondaryKeyCursor);
    }

    private static class SingleColumnSecondaryKeyUpsertAdapter<SecondaryKeyType> implements UpsertAdapter<SecondaryKeyType> {
        private final String tableName;
        private final String primaryKeyColumnName;
        private final String secondaryKeyColumnName;
        private final Function2<Cursor, Integer, SecondaryKeyType> cursorSecondaryKeyGetter;

        public SingleColumnSecondaryKeyUpsertAdapter(
                String tableName,
                String primaryKeyColumnName,
                String secondaryKeyColumnName,
                Function2<Cursor, Integer, SecondaryKeyType> cursorSecondaryKeyGetter
        ) {
            this.tableName = tableName;
            this.primaryKeyColumnName = primaryKeyColumnName;
            this.secondaryKeyColumnName = secondaryKeyColumnName;
            this.cursorSecondaryKeyGetter = cursorSecondaryKeyGetter;
        }

        @Override
        public SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<SecondaryKeyType> secondaryKeys) {
            final String selection =
                    secondaryKeyColumnName + inPlaceholderClause(secondaryKeys.size());
            return SupportSQLiteQueryBuilder
                    .builder(tableName)
                    .columns(
                            new String[]{
                                    primaryKeyColumnName,
                                    secondaryKeyColumnName
                            }
                    )
                    .selection(
                            selection,
                            secondaryKeys.toArray()
                    )
                    .create();
        }

        @Override
        public long getPrimaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return CursorUtil.getNonnullLong(
                    primaryAndSecondaryKeyCursor,
                    0
            );
        }

        @Override
        public SecondaryKeyType getSecondaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return cursorSecondaryKeyGetter.apply(
                    primaryAndSecondaryKeyCursor,
                    1
            );
        }
    }

    private static final UpsertAdapter<String> upsertAdapter = new SingleColumnSecondaryKeyUpsertAdapter<>(
            "podcast_search",
            "_id",
            "search",
            CursorUtil::getNonnullString
    );

    private static <
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            IdentifierType extends Identifier,
            ModelType
            > void putIdentifier2(
            ContentValues contentValues,
            String key,
            IdentifiedType identified
    ) {
        putIdentifier2(contentValues, key, identified.getIdentifier());
    }

    private static <
            IdentifierType extends Identifier
            > void putIdentifier2(
            ContentValues contentValues,
            String key,
            IdentifierType identifier
    ) {
        contentValues.put(key, identifier.getId());
    }

    private final Identified.IdentifiedFactory2<
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > podcastIdentifiedFactory;
    private final CollectionFactory.Capacity<
            PodcastIdentifiedListType,
            PodcastIdentifiedType
            > podcastIdentifiedListCapacityFactory;
    private final Identified.IdentifiedFactory2<
            PodcastSearchIdentifiedType,
            PodcastSearchIdentifierType,
            PodcastSearchType
            > podcastSearchIdentifiedFactory;
    private final Identifier.IdentifierFactory2<PodcastSearchIdentifierType> podcastSearchIdentifierFactory;
    private final Subscription.SubscriptionFactory2<
            SubscriptionType,
            PodcastIdentifiedType,
            PodcastIdentifierType,
            PodcastType
            > subscriptionFactory;
    private final Identified.IdentifiedFactory2<
            SubscriptionIdentifiedType,
            SubscriptionIdentifierType,
            SubscriptionType
            > subscriptionIdentifiedFactory;

    private final DimDatabase<MarkerType> dimDatabase;

    public Database(
            Context context,
            @Nullable String name,
            Scheduler scheduler,
            Episode.EpisodeFactory2<EpisodeType> episodeFactory,
            Identifier.IdentifierFactory2<
                    EpisodeIdentifierType
                    > episodeIdentifierFactory,
            Identified.IdentifiedFactory2<
                    EpisodeIdentifiedType,
                    EpisodeIdentifierType,
                    EpisodeType
                    > episodeIdentifiedFactory,
            CollectionFactory.Capacity<
                    EpisodeIdentifiedListType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedListCapacityFactory,
            CollectionFactory.Collection<
                    EpisodeIdentifiedSetType,
                    EpisodeIdentifiedType
                    > episodeIdentifiedSetCollectionFactory,
            Podcast.PodcastFactory2<PodcastType> podcastFactory,
            Identifier.IdentifierFactory2<
                    PodcastIdentifierType
                    > podcastIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > podcastIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastIdentifiedListType,
                    PodcastIdentifiedType
                    > podcastIdentifiedListCapacityFactory,
            CollectionFactory.Collection<
                    PodcastIdentifiedSetType,
                    PodcastIdentifiedType
                    > podcastIdentifiedSetCollectionFactory,
            PodcastSearch.PodcastSearchFactory2<PodcastSearchType> podcastSearchFactory,
            Identifier.IdentifierFactory2<PodcastSearchIdentifierType> podcastSearchIdentifierFactory,
            Identified.IdentifiedFactory2<
                    PodcastSearchIdentifiedType,
                    PodcastSearchIdentifierType,
                    PodcastSearchType
                    > podcastSearchIdentifiedFactory,
            CollectionFactory.Capacity<
                    PodcastSearchIdentifiedListType,
                    PodcastSearchIdentifiedType
                    > podcastSearchIdentifiedListCapacityFactory,
            Subscription.SubscriptionFactory2<
                    SubscriptionType,
                    PodcastIdentifiedType,
                    PodcastIdentifierType,
                    PodcastType
                    > subscriptionFactory,
            Identifier.IdentifierFactory2<
                    SubscriptionIdentifierType
                    > subscriptionIdentifierFactory,
            Identified.IdentifiedFactory2<
                    SubscriptionIdentifiedType,
                    SubscriptionIdentifierType,
                    SubscriptionType
                    > subscriptionIdentifiedFactory,
            CollectionFactory.Capacity<
                    SubscriptionIdentifiedListType,
                    SubscriptionIdentifiedType
                    > subscriptionIdentifiedListCapacityFactory
    ) {
        this.podcastIdentifiedFactory = podcastIdentifiedFactory;
        this.podcastIdentifiedListCapacityFactory = podcastIdentifiedListCapacityFactory;
        this.podcastSearchIdentifiedFactory = podcastSearchIdentifiedFactory;
        this.podcastSearchIdentifierFactory = podcastSearchIdentifierFactory;
        this.subscriptionFactory = subscriptionFactory;
        this.subscriptionIdentifiedFactory = subscriptionIdentifiedFactory;

        final Configuration configuration = Configuration
                .builder(context)
                .callback(new Schema(context))
                .name(name)
                .build();
        final Factory factory = new FrameworkSQLiteOpenHelperFactory();
        final SupportSQLiteOpenHelper supportSQLiteOpenHelper = factory.create(configuration);
        final SqlDim<MarkerType> sqlBrite = new SqlDim.Builder<MarkerType>().build();
        dimDatabase = sqlBrite.wrapDatabaseHelper(
                supportSQLiteOpenHelper,
                scheduler
        );
    }

    public Optional<PodcastSearchIdentifiedType> upsertPodcastSearch(PodcastSearchType podcastSearch) {
        return upsertModel2(
                upsertAdapter,
                String.class,
                podcastSearch,
                PodcastSearch::getSearch,
                podcastSearchIdentifierFactory,
                podcastSearchIdentifiedFactory,
                this::insertPodcastSearch,
                this::updatePodcastSearchIdentified
        )
                .map(
                        podcastSearchIdentifier -> podcastSearchIdentifiedFactory.newIdentified(
                                podcastSearchIdentifier,
                                podcastSearch
                        )
                );
    }

    public void replacePodcastSearchPodcasts(
            MarkerType marker,
            PodcastSearchIdentifiedType podcastSearchIdentified,
            List<PodcastType> podcasts
    ) {
        try (final DimDatabase.Transaction<MarkerType> transaction = dimDatabase.newTransaction()) {
            podcastSearchResultTable.deletePodcastSearchResultsByPodcastSearchIdentifier(podcastSearchIdentified.getIdentifier());
            ListUtil.indexedStream(podcasts).forEach(indexedPodcast ->
                    podcastTable.upsertPodcast(indexedPodcast.second).ifPresent(
                            podcastIdentifier ->
                                    podcastSearchResultTable.insertPodcastSearchResult(
                                            podcastIdentifier,
                                            podcastSearchIdentified.getIdentifier(),
                                            indexedPodcast.first
                                    )
                    )
            );

            if (podcasts.isEmpty()) {
                podcastTable.triggerMarked(marker);
            }
            transaction.markSuccessful(marker);
        }
    }

    public void markPodcastSearchFailure(MarkerType marker) {
        podcastTable.triggerMarked(marker);
    }

    public Optional<PodcastIdentifiedType> upsertPodcast(PodcastType podcast) {
        return podcastTable
                .upsertPodcast(podcast)
                .map(
                        podcastIdentifier -> podcastIdentifiedFactory.newIdentified(
                                podcastIdentifier,
                                podcast
                        )
                );
    }

    public void upsertEpisodesForPodcast(
            PodcastIdentifierType podcastIdentifier,
            EpisodeListType episodes
    ) {
        episodeTable.upsertEpisodes(
                podcastIdentifier,
                episodes
        );
    }

    public Optional<SubscriptionIdentifiedType> subscribe(PodcastIdentifiedType podcastIdentified) {
        return subscribe(podcastIdentified.getIdentifier())
                .map(
                        subscriptionIdentifier -> subscriptionIdentifiedFactory.newIdentified(
                                subscriptionIdentifier,
                                subscriptionFactory.newSubscription(podcastIdentified)
                        )
                );
    }

    public Optional<SubscriptionIdentifierType> subscribe(PodcastIdentifierType podcastIdentifier) {
        return subscriptionTable.insertSubscription(podcastIdentifier);
    }

    public Result unsubscribe(SubscriptionIdentifierType subscriptionIdentifier) {
        final int deleteCount = subscriptionTable.deleteSubscription(subscriptionIdentifier);
        return deleteCount > 0 ? Result.SUCCESS : Result.FAILURE;
    }

    public Observable<PodcastSearchIdentifiedListType> observeQueryForAllPodcastSearchIdentifieds() {
        return podcastSearchTable.observeQueryForAllPodcastSearchIdentifieds();
    }

    public Observable<PodcastIdentifiedListType> observeQueryForPodcastIdentifieds(
            PodcastSearchIdentifierType podcastSearchIdentifier
    ) {
        return dimDatabase.createQuery(
                Arrays.asList(
                        PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT,
                        PodcastTable.TABLE_PODCAST,
                        PodcastSearchTable.TABLE_PODCAST_SEARCH
                ),
                "SELECT "
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ARTWORK_URL + " AS " + PodcastTable.ARTWORK_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.AUTHOR + " AS " + PodcastTable.AUTHOR + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.FEED_URL + " AS " + PodcastTable.FEED_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " AS " + PodcastTable.ID + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.NAME + " AS " + PodcastTable.NAME + " "
                        + "FROM " + PodcastTable.TABLE_PODCAST + " "
                        + "INNER JOIN " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + " "
                        + "  ON " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_ID + " "
                        + "    = " + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " "
                        + "WHERE " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_SEARCH_ID + " = ?",
                podcastSearchIdentifier.getId()
        ).mapToSpecificList(
                podcastTable::getPodcastIdentified,
                podcastIdentifiedListCapacityFactory::newCollection
        );
    }

    public Observable<
            MarkedValue<
                    MarkerType,
                    PodcastIdentifiedListType
                    >
            > observeMarkedQueryForPodcastIdentifieds(
            PodcastSearchIdentifierType podcastSearchIdentifier
    ) {
        return dimDatabase.createMarkedQuery(
                Arrays.asList(
                        PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT,
                        PodcastTable.TABLE_PODCAST,
                        PodcastSearchTable.TABLE_PODCAST_SEARCH
                ),
                "SELECT "
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ARTWORK_URL + " AS " + PodcastTable.ARTWORK_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.AUTHOR + " AS " + PodcastTable.AUTHOR + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.FEED_URL + " AS " + PodcastTable.FEED_URL + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " AS " + PodcastTable.ID + ","
                        + PodcastTable.TABLE_PODCAST + "." + PodcastTable.NAME + " AS " + PodcastTable.NAME + " "
                        + "FROM " + PodcastTable.TABLE_PODCAST + " "
                        + "INNER JOIN " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + " "
                        + "  ON " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_ID + " "
                        + "    = " + PodcastTable.TABLE_PODCAST + "." + PodcastTable.ID + " "
                        + "WHERE " + PodcastSearchResultTable.TABLE_PODCAST_SEARCH_RESULT + "." + PodcastSearchResultTable.PODCAST_SEARCH_ID + " = ?",
                podcastSearchIdentifier.getId()
        )
                .mapToSpecificList(
                        (cursor, ns) -> podcastTable.getPodcastIdentified(cursor),
                        podcastIdentifiedListCapacityFactory::newCollection
                );
    }

    public Observable<Optional<PodcastIdentifiedType>> observeQueryForPodcastIdentified(
            PodcastIdentifierType podcastIdentifier
    ) {
        return podcastTable.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    public Observable<SubscriptionIdentifiedListType> observeQueryForSubscriptions() {
        return subscriptionTable.observeQueryForSubscriptions();
    }

    public Observable<Optional<SubscriptionIdentifierType>> observeQueryForSubscriptionIdentifier(
            PodcastIdentifierType podcastIdentifier
    ) {
        return subscriptionTable.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    public Observable<EpisodeIdentifiedListType> observeQueryForEpisodeIdentifiedsForPodcast(
            PodcastIdentifierType podcastIdentifier
    ) {
        return episodeTable
                .observeQueryForEpisodeIdentifiedsForPodcast(podcastIdentifier);
    }

    public boolean deletePodcast(PodcastIdentifierType podcastIdentifier) {
        final int deleteCount = podcastTable.deletePodcast(podcastIdentifier);
        return deleteCount > 0;
    }

    public boolean deletePodcastSearch(PodcastSearchIdentifierType podcastSearchIdentifier) {
        final int deleteCount = podcastSearchTable.deletePodcastSearchById(podcastSearchIdentifier);
        return deleteCount > 0;
    }

    private Optional<PodcastSearchIdentifierType> insertPodcastSearch(PodcastSearchType podcastSearch) {
        final long id = dimDatabase.insert(
                "podcast_search",
                CONFLICT_ABORT,
                getPodcastSearchContentValues(podcastSearch)
        );
        if (id == -1) {
            return Optional.empty();
        } else {
            return Optional.of(
                    podcastSearchIdentifierFactory.newIdentifier(id)
            );
        }
    }

    private int updatePodcastSearchIdentified(
            PodcastSearchIdentifiedType podcastSearchIdentified) {
        return dimDatabase.update(
                "podcast_search",
                CONFLICT_ABORT,
                getPodcastSearchIdentifiedContentValues(podcastSearchIdentified),
                "_id = ?",
                Long.toString(podcastSearchIdentified.getIdentifier().getId())
        );
    }

    private ContentValues getPodcastSearchContentValues(PodcastSearchType podcastSearch) {
        final ContentValues values = new ContentValues(3);
        values.put(
                "search",
                podcastSearch.getSearch()
        );
        return values;
    }

    private ContentValues getPodcastSearchIdentifiedContentValues(PodcastSearchIdentifiedType podcastSearchIdentified) {
        final ContentValues values = getPodcastSearchContentValues(podcastSearchIdentified.getModel());

        putIdentifier2(
                values,
                "_id",
                podcastSearchIdentified
        );

        return values;
    }

    private <
            IdentifierType extends Identifier,
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            ModelType,
            SecondaryKeyType
            > Optional<IdentifierType> upsertModel2(
            UpsertAdapter<SecondaryKeyType> upsertAdapter,
            // secondaryKeyClass exists to force S not to be Serializable or some other
            // unexpected superclass of unrelated Model and Cursor property classes.
            // however, we don't actually need the parameter, so we suppress unused.
            @SuppressWarnings("unused") Class<SecondaryKeyType> secondaryKeyClass,
            ModelType model,
            Function<
                    ModelType,
                    SecondaryKeyType>
                    modelSecondaryKeyGetter,
            Identifier.IdentifierFactory2<
                    IdentifierType
                    > identifierFactory,
            Identified.IdentifiedFactory2<
                    IdentifiedType,
                    IdentifierType,
                    ModelType
                    > identifiedFactory,
            Function<
                    ModelType,
                    Optional<IdentifierType>
                    > modelInserter,
            Function<
                    IdentifiedType,
                    Integer
                    > identifiedUpdater
    ) {
        try (final DimDatabase.Transaction<MarkerType> transaction = dimDatabase.newTransaction()) {
            final SecondaryKeyType secondaryKey = modelSecondaryKeyGetter.apply(model);

            final SupportSQLiteQuery primaryAndSecondaryKeyQuery =
                    upsertAdapter.createPrimaryKeyAndSecondaryKeyQuery(Collections.singleton(secondaryKey));
            @Nullable final Optional<IdentifierType> upsertedIdentifierOptional;
            try (final Cursor primaryAndSecondaryKeyCursor = dimDatabase.query(primaryAndSecondaryKeyQuery)) {
                if (primaryAndSecondaryKeyCursor.moveToNext()) {
                    final long primaryKey = upsertAdapter.getPrimaryKey(primaryAndSecondaryKeyCursor);
                    final IdentifierType upsertingIdentifier = identifierFactory.newIdentifier(primaryKey);
                    final IdentifiedType updatingIdentified = identifiedFactory.newIdentified(
                            upsertingIdentifier,
                            model
                    );
                    int rowCount = identifiedUpdater.apply(updatingIdentified);
                    if (rowCount == 1) {
                        upsertedIdentifierOptional = Optional.of(upsertingIdentifier);
                    } else {
                        upsertedIdentifierOptional = Optional.empty();
                    }
                } else {
                    upsertedIdentifierOptional = modelInserter
                            .apply(model);
                }
            }

            transaction.markSuccessful();

            return upsertedIdentifierOptional;
        }
    }

    private <
            IdentifierType extends Identifier,
            IdentifiedType extends Identified<
                    IdentifierType,
                    ModelType
                    >,
            ModelListType extends List<ModelType>,
            ModelType,
            SecondaryKeyType
            > List<Optional<IdentifierType>> upsertModels2(
            UpsertAdapter<SecondaryKeyType> upsertAdapter,
            // secondaryKeyClass exists to force S not to be Serializable or some other
            // unexpected superclass of unrelated Model and Cursor property classes.
            // however, we don't actually need the parameter, so we suppress unused.
            @SuppressWarnings("unused") Class<SecondaryKeyType> secondaryKeyClass,
            ModelListType models,
            Function<
                    ModelType,
                    SecondaryKeyType
                    > modelSecondaryKeyGetter,
            Identifier.IdentifierFactory2<
                    IdentifierType
                    > identifierFactory,
            Identified.IdentifiedFactory2<
                    IdentifiedType,
                    IdentifierType,
                    ModelType
                    > identifiedFactory,
            Function<
                    ModelType,
                    Optional<IdentifierType>
                    > modelInserter,
            Function<
                    IdentifiedType,
                    Integer
                    > identifiedUpdater,
            CollectionFactory.Capacity<
                    List<Optional<IdentifierType>>,
                    Optional<IdentifierType>
                    > identifierOptionalListCapacityFactory
    ) {
        if (models.isEmpty()) {
            return identifierOptionalListCapacityFactory.newCollection(0);
        } else {
            try (final DimDatabase.Transaction<MarkerType> transaction = dimDatabase.newTransaction()) {
                final SortedSetUtil<Tuple<Integer, ModelType>> indexedModelSortedSetUtil =
                        new SortedSetUtil<>(
                                Comparator.comparing(
                                        indexedModel ->
                                                indexedModel.first
                                )
                        );
                final Map<SecondaryKeyType, SortedSet<Tuple<Integer, ModelType>>> indexedModelSetsBySecondaryKey =
                        indexedStream(models)
                                .collect(
                                        Collectors.toMap(
                                                modelSecondaryKeyGetter.compose(Tuple::getSecond),
                                                indexedModelSortedSetUtil::singletonSortedSet,
                                                indexedModelSortedSetUtil::union,
                                                // LinkedHashMap is important here to ensure that
                                                // we iterate through inserted elements in the same
                                                // order as we receive them in the models List.
                                                LinkedHashMap::new
                                        )
                                );

                final SupportSQLiteQuery primaryKeyAndSecondaryKeyQuery =
                        upsertAdapter.createPrimaryKeyAndSecondaryKeyQuery(indexedModelSetsBySecondaryKey.keySet());
                // Again, LinkedHashSet is important here to ensure that we iterate through inserted
                // elements in the same order as we receive them in the models list.
                // the above LinkedHashMap preserves that order in its keySet, and LinkedHashSet
                // preserves that order as well.
                final LinkedHashSet<SecondaryKeyType> insertingSecondaryKeys = new LinkedHashSet<>(indexedModelSetsBySecondaryKey.keySet());
                final List<IdentifiedType> updatingIdentifieds = new ArrayList<>(models.size());
                try (final Cursor primaryKeyAndSecondaryKeyCursor = dimDatabase.query(primaryKeyAndSecondaryKeyQuery)) {
                    while (primaryKeyAndSecondaryKeyCursor.moveToNext()) {
                        final long primaryKey = upsertAdapter.getPrimaryKey(primaryKeyAndSecondaryKeyCursor);
                        final SecondaryKeyType secondaryKey = upsertAdapter.getSecondaryKey(primaryKeyAndSecondaryKeyCursor);
                        @Nullable final Set<Tuple<Integer, ModelType>> indexedModelSet =
                                indexedModelSetsBySecondaryKey.get(secondaryKey);
                        if (indexedModelSet == null) {
                            throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                        } else {
                            final ModelType model = indexedModelSet.iterator().next().second;
                            insertingSecondaryKeys.remove(secondaryKey);

                            final IdentifierType updatingIdentifier = identifierFactory.newIdentifier(primaryKey);
                            final IdentifiedType updatingIdentified = identifiedFactory.newIdentified(
                                    updatingIdentifier,
                                    model
                            );
                            updatingIdentifieds.add(updatingIdentified);
                        }
                    }
                }

                final int capacity = models.size();
                final List<Optional<IdentifierType>> upsertedIdentifierOptionals = identifierOptionalListCapacityFactory.newCollection(capacity);
                for (int i = 0; i < capacity; i++) {
                    upsertedIdentifierOptionals.add(Optional.empty());
                }
                for (final SecondaryKeyType secondaryKey : insertingSecondaryKeys) {
                    @Nullable final Set<Tuple<Integer, ModelType>> indexedModelSet =
                            indexedModelSetsBySecondaryKey.get(secondaryKey);
                    if (indexedModelSet == null) {
                        throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                    } else {
                        final ModelType model = indexedModelSet.iterator().next().second;
                        final Optional<IdentifierType> identifierOpt =
                                modelInserter.apply(model);
                        if (identifierOpt.isPresent()) {
                            for (final Tuple<Integer, ModelType> indexedModel : indexedModelSet) {
                                upsertedIdentifierOptionals.set(
                                        indexedModel.first,
                                        identifierOpt
                                );
                            }
                        }
                    }
                }

                for (final IdentifiedType updatingIdentified : updatingIdentifieds) {
                    final int rowCount = identifiedUpdater.apply(updatingIdentified);
                    if (rowCount == 1) {
                        final SecondaryKeyType secondaryKey =
                                modelSecondaryKeyGetter.apply(updatingIdentified.getModel());
                        @Nullable final Set<Tuple<Integer, ModelType>> indexedModelSet =
                                indexedModelSetsBySecondaryKey.get(secondaryKey);
                        if (indexedModelSet == null) {
                            throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                        } else {
                            for (final Tuple<Integer, ModelType> indexedModel : indexedModelSet) {
                                upsertedIdentifierOptionals.set(
                                        indexedModel.first,
                                        Optional.of(updatingIdentified.getIdentifier())
                                );
                            }
                        }
                    }
                }

                transaction.markSuccessful();

                return upsertedIdentifierOptionals;
            }
        }
    }
}
