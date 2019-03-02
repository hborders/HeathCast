package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.utils.CursorUtil;
import com.github.hborders.heathcast.utils.NonnullPair;
import com.github.hborders.heathcast.utils.SortedSetUtil;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.ArrayList;
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

import javax.annotation.Nullable;

import static com.github.hborders.heathcast.utils.ListUtil.indexedStream;
import static com.github.hborders.heathcast.utils.SqlUtil.inPlaceholderClause;

abstract class Table {
    protected static final Object[] EMPTY_BIND_ARGS = new Object[0];

    protected final BriteDatabase briteDatabase;

    protected Table(BriteDatabase briteDatabase) {
        this.briteDatabase = briteDatabase;
    }

    protected static void putIdentifier(ContentValues contentValues, String key, Identified<?> identified) {
        putIdentifier(contentValues, key, identified.identifier);
    }

    protected static void putIdentifier(ContentValues contentValues, String key, Identifier<?> identifier) {
        contentValues.put(key, identifier.id);
    }

    @Nullable
    @SuppressWarnings("unused")
    protected final <M, S> Identifier<M> upsertModel(
            String tableName,
            UpsertAdapter<S> upsertAdapter,
            Class<M> modelClass,
            // secondaryKeyClass exists to force S not to be Serializable or some other
            // unexpected superclass of unrelated Model and Cursor property classes.
            // however, we don't actually need the parameter, so we suppress unused.
            Class<S> secondaryKeyClass,
            M model,
            Function<M, S> modelSecondaryKeyGetter,
            Function<M, Optional<Identifier<M>>> modelInserter,
            Function<Identified<M>, Integer> identifiedUpdater
    ) {
        try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
            final S secondaryKey = modelSecondaryKeyGetter.apply(model);


            final SupportSQLiteQuery primaryAndSecondaryKeyQuery =
                    upsertAdapter.createPrimaryKeyAndSecondaryKeyQuery(Collections.singleton(secondaryKey));
            @Nullable final Identifier<M> upsertedIdentifier;
            try (final Cursor primaryAndSecondaryKeyCursor = briteDatabase.query(primaryAndSecondaryKeyQuery)) {
                if (primaryAndSecondaryKeyCursor.moveToNext()) {
                    final long primaryKey = upsertAdapter.getPrimaryKey(primaryAndSecondaryKeyCursor);
                    final Identifier<M> upsertingIdentifier = new Identifier<>(
                            modelClass,
                            primaryKey
                    );
                    int rowCount = identifiedUpdater.apply(
                            new Identified<>(
                                    upsertingIdentifier,
                                    model
                            )
                    );
                    if (rowCount == 1) {
                        upsertedIdentifier = upsertingIdentifier;
                    } else {
                        upsertedIdentifier = null;
                    }
                } else {
                    upsertedIdentifier = modelInserter
                            .apply(model)
                            .orElse(null);
                }
            }

            transaction.markSuccessful();

            return upsertedIdentifier;
        }
    }

    @SuppressWarnings("unused")
    protected final <M, S> List<Optional<Identifier<M>>> upsertModels(
            String tableName,
            UpsertAdapter<S> upsertAdapter,
            Class<M> modelClass,
            // secondaryKeyClass exists to force S not to be Serializable or some other
            // unexpected superclass of unrelated Model and Cursor property classes.
            // however, we don't actually need the parameter, so we suppress unused.
            Class<S> secondaryKeyClass,
            List<M> models,
            Function<M, S> modelSecondaryKeyGetter,
            Function<M, Optional<Identifier<M>>> modelInserter,
            Function<Identified<M>, Integer> identifiedUpdater
    ) {
        if (models.isEmpty()) {
            return Collections.emptyList();
        } else {
            try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
                final SortedSetUtil<NonnullPair<Integer, M>> indexedModelSortedSetUtil =
                        new SortedSetUtil<>(
                                Comparator.comparing(
                                        indexedModel ->
                                                indexedModel.first
                                )
                        );
                final Map<S, SortedSet<NonnullPair<Integer, M>>> indexedModelSetsBySecondaryKey =
                        indexedStream(models)
                                .collect(
                                        Collectors.toMap(
                                                modelSecondaryKeyGetter.compose(NonnullPair::getSecond),
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
                final List<Identified<M>> updatingIdentifieds = new ArrayList<>(models.size());
                try (final Cursor primaryKeyAndSecondaryKeyCursor = briteDatabase.query(primaryKeyAndSecondaryKeyQuery)) {
                    while (primaryKeyAndSecondaryKeyCursor.moveToNext()) {
                        final long primaryKey = upsertAdapter.getPrimaryKey(primaryKeyAndSecondaryKeyCursor);
                        final S secondaryKey = upsertAdapter.getSecondaryKey(primaryKeyAndSecondaryKeyCursor);
                        @Nullable final Set<NonnullPair<Integer, M>> indexedModelSet =
                                indexedModelSetsBySecondaryKey.get(secondaryKey);
                        if (indexedModelSet == null) {
                            throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                        } else {
                            final M model = indexedModelSet.iterator().next().second;
                            insertingSecondaryKeys.remove(secondaryKey);

                            updatingIdentifieds.add(
                                    new Identified<>(
                                            new Identifier<>(
                                                    modelClass,
                                                    primaryKey
                                            ),
                                            model
                                    )
                            );
                        }
                    }
                }

                final ArrayList<Optional<Identifier<M>>> upsertedIdentifiers =
                        new ArrayList<>(
                                Collections.nCopies(
                                        models.size(),
                                        Optional.empty()
                                )
                        );
                for (final S secondaryKey : insertingSecondaryKeys) {
                    @Nullable final Set<NonnullPair<Integer, M>> indexedModelSet =
                            indexedModelSetsBySecondaryKey.get(secondaryKey);
                    if (indexedModelSet == null) {
                        throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                    } else {
                        final M model = indexedModelSet.iterator().next().second;
                        final Optional<Identifier<M>> identifierOptional =
                                modelInserter.apply(model);
                        if (identifierOptional.isPresent()) {
                            for (final NonnullPair<Integer, M> indexedModel : indexedModelSet) {
                                upsertedIdentifiers.set(
                                        indexedModel.first,
                                        identifierOptional
                                );
                            }
                        }
                    }
                }

                for (final Identified<M> updatingIdentified : updatingIdentifieds) {
                    final int rowCount = identifiedUpdater.apply(updatingIdentified);
                    if (rowCount == 1) {
                        final S secondaryKey =
                                modelSecondaryKeyGetter.apply(updatingIdentified.model);
                        @Nullable final Set<NonnullPair<Integer, M>> indexedModelSet =
                                indexedModelSetsBySecondaryKey.get(secondaryKey);
                        if (indexedModelSet == null) {
                            throw new IllegalStateException("Found unexpected secondary key: " + secondaryKey);
                        } else {
                            for (final NonnullPair<Integer, M> indexedModel : indexedModelSet) {
                                upsertedIdentifiers.set(
                                        indexedModel.first,
                                        Optional.of(updatingIdentified.identifier)
                                );
                            }
                        }
                    }
                }

                transaction.markSuccessful();

                return upsertedIdentifiers;
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
