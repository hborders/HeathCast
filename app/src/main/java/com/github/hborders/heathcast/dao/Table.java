package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.android.CursorUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
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
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.hborders.heathcast.android.SqlUtil.inPlaceholderClause;
import static com.github.hborders.heathcast.core.ListUtil.indexedStream;

abstract class Table<MarkerType> {
    @SuppressWarnings("unused")
    protected static final Object[] EMPTY_BIND_ARGS = new Object[0];

    protected final DimDatabase<MarkerType> dimDatabase;

    protected Table(DimDatabase<MarkerType> dimDatabase) {
        this.dimDatabase = dimDatabase;
    }

    protected static <
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

    protected static <
            IdentifierType extends Identifier,
            ModelType
            > void putIdentifier2(
            ContentValues contentValues,
            String key,
            IdentifierType identifier
    ) {
        contentValues.put(key, identifier.getId());
    }

    protected final <
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

    protected final <
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

    protected static <
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

    protected interface UpsertAdapter<SecondaryKeyType> {
        SupportSQLiteQuery createPrimaryKeyAndSecondaryKeyQuery(Set<SecondaryKeyType> secondaryKeys);

        long getPrimaryKey(Cursor primaryAndSecondaryKeyCursor);

        SecondaryKeyType getSecondaryKey(Cursor primaryAndSecondaryKeyCursor);
    }

    protected static class SingleColumnSecondaryKeyUpsertAdapter<SecondaryKeyType> implements UpsertAdapter<SecondaryKeyType> {
        private final String tableName;
        private final String primaryKeyColumnName;
        private final String secondaryKeyColumnName;
        private final Function<Cursor, SecondaryKeyType> cursorSecondaryKeyGetter;

        public SingleColumnSecondaryKeyUpsertAdapter(
                String tableName,
                String primaryKeyColumnName,
                String secondaryKeyColumnName,
                Function<Cursor, SecondaryKeyType> cursorSecondaryKeyGetter
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
        public SecondaryKeyType getSecondaryKey(Cursor primaryAndSecondaryKeyCursor) {
            return cursorSecondaryKeyGetter.apply(primaryAndSecondaryKeyCursor);
        }
    }
}
