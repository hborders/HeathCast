package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.utils.CursorUtil;
import com.github.hborders.heathcast.utils.NonnullPair;
import com.github.hborders.heathcast.utils.SetUtil;
import com.squareup.sqlbrite3.BriteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    protected final <M, S> Identifier<M> upsertModel(
            String tableName,
            String primaryKeyColumnName,
            String secondaryKeyColumnName,
            Class<M> modelClass,
            M model,
            Function<M, S> modelSecondaryKeyGetter,
            Function<Cursor, S> cursorSecondaryKeyGetter,
            Function<M, Optional<Identifier<M>>> modelInserter,
            Function<Identified<M>, Integer> identifiedUpdater
    ) {
        try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
            final S secondaryKey = modelSecondaryKeyGetter.apply(model);
            final SupportSQLiteQuery primaryKeyQuery =
                    SupportSQLiteQueryBuilder
                            .builder(tableName)
                            .columns(new String[]{
                                    primaryKeyColumnName
                            })
                            .selection(
                                    secondaryKeyColumnName + " = ?",
                                    new Object[]{secondaryKey}
                            )
                            .create();
            @Nullable final Identifier<M> upsertedIdentifier;
            try (final Cursor primaryKeyCursor = briteDatabase.query(primaryKeyQuery)) {
                if (primaryKeyCursor.moveToNext()) {
                    final long primaryKey = primaryKeyCursor.getLong(0);
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

    protected final <M, S> List<Optional<Identifier<M>>> upsertModels(
            String tableName,
            String primaryKeyColumnName,
            String secondaryKeyColumnName,
            Class<M> modelClass,
            List<M> models,
            Function<M, S> modelSecondaryKeyGetter,
            Function<Cursor, S> cursorSecondaryKeyGetter,
            Function<M, Optional<Identifier<M>>> modelInserter,
            Function<Identified<M>, Integer> identifiedUpdater
    ) {
        if (models.isEmpty()) {
            return Collections.emptyList();
        } else {
            try (final BriteDatabase.Transaction transaction = briteDatabase.newTransaction()) {
                final Map<S, Set<NonnullPair<Integer, M>>> indexedModelSetsBySecondaryKey =
                        indexedStream(models)
                                .collect(
                                        Collectors.toMap(
                                                modelSecondaryKeyGetter.compose(NonnullPair::getSecond),
                                                Collections::singleton,
                                                SetUtil::union
                                        )
                                );

                final String selection =
                        secondaryKeyColumnName + inPlaceholderClause(indexedModelSetsBySecondaryKey.keySet().size());
                final SupportSQLiteQuery primaryKeyAndSecondaryKeyQuery =
                        SupportSQLiteQueryBuilder
                                .builder(tableName)
                                .columns(new String[]{
                                        primaryKeyColumnName,
                                        secondaryKeyColumnName
                                })
                                .selection(
                                        selection,
                                        indexedModelSetsBySecondaryKey.keySet().toArray()
                                )
                                .create();
                final HashSet<S> insertingSecondaryKeys = new HashSet<>(indexedModelSetsBySecondaryKey.keySet());
                final List<Identified<M>> updatingIdentifieds = new ArrayList<>(models.size());
                try (final Cursor primaryKeyAndSecondaryKeyCursor = briteDatabase.query(primaryKeyAndSecondaryKeyQuery)) {
                    while (primaryKeyAndSecondaryKeyCursor.moveToNext()) {
                        final long primaryKey =
                                CursorUtil.getNonnullLong(
                                        primaryKeyAndSecondaryKeyCursor,
                                        primaryKeyColumnName
                                );
                        final S secondaryKey =
                                cursorSecondaryKeyGetter.apply(primaryKeyAndSecondaryKeyCursor);
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
}
