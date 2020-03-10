package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CapacityListFactory;
import com.github.hborders.heathcast.core.Opt;
import com.github.hborders.heathcast.core.Opt.OptFactory;
import com.github.hborders.heathcast.core.SortedSetUtil;
import com.github.hborders.heathcast.core.Tuple;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.stealthmountain.sqldim.DimDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;
import static com.github.hborders.heathcast.core.ListUtil.indexedStream;

abstract class Table<N> {
    @SuppressWarnings("unused")
    protected static final Object[] EMPTY_BIND_ARGS = new Object[0];

    protected final DimDatabase<N> dimDatabase;

    protected Table(DimDatabase<N> dimDatabase) {
        this.dimDatabase = dimDatabase;
    }

    protected static void putIdentifier(ContentValues contentValues, String key, Identified<?, ?> identified) {
        putIdentifier(contentValues, key, identified.identifier);
    }

    protected static void putIdentifier(ContentValues contentValues, String key, Identifier<?> identifier) {
        contentValues.put(key, identifier.id);
    }


    protected final <
            I extends Identifier<M>,
            J extends Identified<I, M>,
            M,
            OF extends OptFactory<P, I>,
            P extends Opt<I>,
            S
            > P upsertModel(
            UpsertAdapter<S> upsertAdapter,
            // secondaryKeyClass exists to force S not to be Serializable or some other
            // unexpected superclass of unrelated Model and Cursor property classes.
            // however, we don't actually need the parameter, so we suppress unused.
            @SuppressWarnings("unused") Class<S> secondaryKeyClass,
            M model,
            Function<M, S> modelSecondaryKeyGetter,
            Function<Long, I> identifierFactory,
            BiFunction<I, M, J> identifiedFactory,
            Function<M, P> modelInserter,
            Function<J, Integer> identifiedUpdater,
            OF optFactory
    ) {
        try (final DimDatabase.Transaction<N> transaction = dimDatabase.newTransaction()) {
            final S secondaryKey = modelSecondaryKeyGetter.apply(model);

            final SupportSQLiteQuery primaryAndSecondaryKeyQuery =
                    upsertAdapter.createPrimaryKeyAndSecondaryKeyQuery(Collections.singleton(secondaryKey));
            @Nullable final P upsertedIdentifierOpt;
            try (final Cursor primaryAndSecondaryKeyCursor = dimDatabase.query(primaryAndSecondaryKeyQuery)) {
                if (primaryAndSecondaryKeyCursor.moveToNext()) {
                    final long primaryKey = upsertAdapter.getPrimaryKey(primaryAndSecondaryKeyCursor);
                    final I upsertingIdentifier = identifierFactory.apply(primaryKey);
                    final J updatingIdentified = identifiedFactory.apply(
                            upsertingIdentifier,
                            model
                    );
                    int rowCount = identifiedUpdater.apply(updatingIdentified);
                    if (rowCount == 1) {
                        upsertedIdentifierOpt = optFactory.of(upsertingIdentifier);
                    } else {
                        upsertedIdentifierOpt = optFactory.empty();
                    }
                } else {
                    upsertedIdentifierOpt = modelInserter
                            .apply(model);
                }
            }

            transaction.markSuccessful();

            return upsertedIdentifierOpt;
        }
    }


    protected final <
            CLF extends CapacityListFactory<L, P>,
            I extends Identifier<M>,
            J extends Identified<I, M>,
            K extends List<M>,
            L extends List<P>,
            M,
            OF extends OptFactory<P, I>,
            P extends Opt<I>,
            S
            > L upsertModels(
            UpsertAdapter<S> upsertAdapter,
            // secondaryKeyClass exists to force S not to be Serializable or some other
            // unexpected superclass of unrelated Model and Cursor property classes.
            // however, we don't actually need the parameter, so we suppress unused.
            @SuppressWarnings("unused") Class<S> secondaryKeyClass,
            K models,
            Function<M, S> modelSecondaryKeyGetter,
            Function<Long, I> identifierFactory,
            BiFunction<I, M, J> identifiedFactory,
            Function<M, P> modelInserter,
            Function<J, Integer> identifiedUpdater,
            OF optFactory,
            CLF capacityListFactory
    ) {
        if (models.isEmpty()) {
            return capacityListFactory.newList(0);
        } else {
            try (final DimDatabase.Transaction<N> transaction = dimDatabase.newTransaction()) {
                final SortedSetUtil<Tuple<Integer, M>> indexedModelSortedSetUtil =
                        new SortedSetUtil<>(
                                Comparator.comparing(
                                        indexedModel ->
                                                indexedModel.first
                                )
                        );
                final Map<S, SortedSet<Tuple<Integer, M>>> indexedModelSetsBySecondaryKey =
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
                final LinkedHashSet<S> insertingSecondaryKeys = new LinkedHashSet<>(indexedModelSetsBySecondaryKey.keySet());
                final List<J> updatingIdentifieds = new ArrayList<>(models.size());
                try (final Cursor primaryKeyAndSecondaryKeyCursor = dimDatabase.query(primaryKeyAndSecondaryKeyQuery)) {
                    while (primaryKeyAndSecondaryKeyCursor.moveToNext()) {
                        final long primaryKey = upsertAdapter.getPrimaryKey(primaryKeyAndSecondaryKeyCursor);
                        final S secondaryKey = upsertAdapter.getSecondaryKey(primaryKeyAndSecondaryKeyCursor);
                        @Nullable final Set<Tuple<Integer, M>> indexedModelSet =
                                indexedModelSetsBySecondaryKey.get(secondaryKey);
                        if (indexedModelSet == null) {
                            throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                        } else {
                            final M model = indexedModelSet.iterator().next().second;
                            insertingSecondaryKeys.remove(secondaryKey);

                            final I updatingIdentifier = identifierFactory.apply(primaryKey);
                            final J updatingIdentified = identifiedFactory.apply(
                                    updatingIdentifier,
                                    model
                            );
                            updatingIdentifieds.add(updatingIdentified);
                        }
                    }
                }

                final int capacity = models.size();
                final L upsertedIdentifierOpts = capacityListFactory.newList(capacity);
                for (int i=0; i < capacity; i++) {
                    upsertedIdentifierOpts.add(optFactory.empty());
                }
                for (final S secondaryKey : insertingSecondaryKeys) {
                    @Nullable final Set<Tuple<Integer, M>> indexedModelSet =
                            indexedModelSetsBySecondaryKey.get(secondaryKey);
                    if (indexedModelSet == null) {
                        throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                    } else {
                        final M model = indexedModelSet.iterator().next().second;
                        final P identifierOpt =
                                modelInserter.apply(model);
                        if (identifierOpt.value != null) {
                            for (final Tuple<Integer, M> indexedModel : indexedModelSet) {
                                upsertedIdentifierOpts.set(
                                        indexedModel.first,
                                        identifierOpt
                                );
                            }
                        }
                    }
                }

                for (final J updatingIdentified : updatingIdentifieds) {
                    final int rowCount = identifiedUpdater.apply(updatingIdentified);
                    if (rowCount == 1) {
                        final S secondaryKey =
                                modelSecondaryKeyGetter.apply(updatingIdentified.model);
                        @Nullable final Set<Tuple<Integer, M>> indexedModelSet =
                                indexedModelSetsBySecondaryKey.get(secondaryKey);
                        if (indexedModelSet == null) {
                            throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                        } else {
                            for (final Tuple<Integer, M> indexedModel : indexedModelSet) {
                                upsertedIdentifierOpts.set(
                                        indexedModel.first,
                                        optFactory.of(updatingIdentified.identifier)
                                );
                            }
                        }
                    }
                }

                transaction.markSuccessful();

                return upsertedIdentifierOpts;
            }
        }
    }

    protected static String[] idStrings(Collection<? extends Identifier<?>> identifiers) {
        final String[] idStrings = new String[identifiers.size()];
        int i = 0;
        for (Identifier<?> identifier : identifiers) {
            idStrings[i] = Long.toString(identifier.id);
            i++;
        }

        return idStrings;
    }

    protected interface UpsertAdapter<S> {
        SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<S> secondaryKeys);

        long getPrimaryKey(Cursor primaryAndSecondaryKeyCursor);

        S getSecondaryKey(Cursor primaryAndSecondaryKeyCursor);
    }

    protected static class SingleColumnSecondaryKeyUpsertAdapter<S> implements UpsertAdapter<S> {
        private final String tableName;
        private final String primaryKeyColumnName;
        private final String secondaryKeyColumnName;
        private final Function<Cursor, S> cursorSecondaryKeyGetter;

        public SingleColumnSecondaryKeyUpsertAdapter(
                String tableName,
                String primaryKeyColumnName,
                String secondaryKeyColumnName,
                Function<Cursor, S> cursorSecondaryKeyGetter
        ) {
            this.tableName = tableName;
            this.primaryKeyColumnName = primaryKeyColumnName;
            this.secondaryKeyColumnName = secondaryKeyColumnName;
            this.cursorSecondaryKeyGetter = cursorSecondaryKeyGetter;
        }

        @Override
        public SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<S> secondaryKeys) {
            final String selection =
                    secondaryKeyColumnName + inPlaceholderClause(secondaryKeys.size());
            return SupportSQLiteQueryBuilder
                    .builder(tableName)
                    .columns(new String[]{
                            primaryKeyColumnName,
                            secondaryKeyColumnName
                    })
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
                    primaryKeyColumnName
            );
        }

        @Override
        public S getSecondaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return cursorSecondaryKeyGetter.apply(primaryAndSecondaryKeyCursor);
        }
    }
}
