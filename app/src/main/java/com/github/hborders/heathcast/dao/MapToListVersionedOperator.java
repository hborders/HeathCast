package com.github.hborders.heathcast.dao;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Tuple;
import com.github.hborders.heathcast.models.Versioned;
import com.stealthmountain.sqldim.SqlDim;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.ObservableOperator;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

final class MapToListVersionedOperator<
        ListVersionedType extends Versioned<ListType>,
        ListType extends List<ItemType>,
        ItemType
        > implements ObservableOperator<
        Optional<ListVersionedType>,
        SqlDim.Query
        > {
    private final CollectionFactory.Capacity<
            ListType,
            ItemType
            > listCapacityFactory;
    private final Function<
            Cursor,
            Tuple<
                    Long,
                    ItemType
                    >
            > mapper;
    private final BiFunction<
            ListType,
            Long,
            ListVersionedType
            > listVersionedFactory;

    public MapToListVersionedOperator(
            CollectionFactory.Capacity<
                    ListType,
                    ItemType
                    > listCapacityFactory,
            Function<
                    Cursor,
                    Tuple<
                            Long,
                            ItemType
                            >
                    > mapper,
            BiFunction<
                    ListType,
                    Long,
                    ListVersionedType
                    > listVersionedFactory
    ) {
        this.listCapacityFactory = listCapacityFactory;
        this.mapper = mapper;
        this.listVersionedFactory = listVersionedFactory;
    }

    @Override
    public Observer<? super SqlDim.Query> apply(Observer<
            ? super Optional<ListVersionedType>
            > observer
    ) throws Throwable {
        return new MappingObserver<>(
                observer,
                listCapacityFactory,
                mapper,
                listVersionedFactory
        );
    }

    static final class MappingObserver<
            ListVersionedType extends Versioned<ListType>,
            ListType extends List<ItemType>,
            ItemType
            > extends DisposableObserver<SqlDim.Query> {
        private final Observer<
                ? super Optional<ListVersionedType>
                > downstream;
        private final CollectionFactory.Capacity<
                ListType,
                ItemType
                > listCapacityFactory;
        private final Function<
                Cursor,
                Tuple<
                        Long,
                        ItemType
                        >
                > mapper;
        private final BiFunction<
                ListType,
                Long,
                ListVersionedType
                > listVersionedFactory;

        public MappingObserver(
                Observer<
                        ? super Optional<ListVersionedType>
                        > downstream,
                CollectionFactory.Capacity<
                        ListType,
                        ItemType
                        > listCapacityFactory,
                Function<
                        Cursor,
                        Tuple<
                                Long,
                                ItemType
                                >
                        > mapper,
                BiFunction<
                        ListType,
                        Long,
                        ListVersionedType
                        > listVersionedFactory
        ) {
            this.downstream = downstream;
            this.listCapacityFactory = listCapacityFactory;
            this.mapper = mapper;
            this.listVersionedFactory = listVersionedFactory;
        }

        @Override
        protected void onStart() {
            downstream.onSubscribe(this);
        }

        @Override
        public void onNext(SqlDim.Query query) {
            try {
                @Nullable Tuple<
                        Long,
                        ItemType
                        > versionAndItem;
                @Nullable Long expectedVersion = null;
                @Nullable final Cursor cursor = query.run();
                if (cursor == null || isDisposed()) {
                    return;
                }
                final int count = cursor.getCount();
                if (count == 0) {
                    downstream.onNext(Optional.empty());
                } else {
                    @NonNull final ListType items = listCapacityFactory.newCollection(count);
                    try {
                        while (cursor.moveToNext()) {
                            versionAndItem = mapper.apply(cursor);
                            // even though the type system should make this impossible,
                            // Java doesn't always check nullability annotations,
                            // so leave this in just in case our clients don't follow the rules.
                            if (versionAndItem == null) {
                                downstream.onError(new NullPointerException("MapToListVersionedOperator mapper returned null"));
                                return;
                            }
                            final Long version = versionAndItem.first;
                            if (expectedVersion == null) {
                                expectedVersion = version;
                            } else if (!version.equals(expectedVersion)) {
                                throw new IllegalStateException("All versions should be the equal, but found " + version + " after initially finding " + expectedVersion);
                            }
                            items.add(versionAndItem.second);
                        }
                    } finally {
                        cursor.close();
                    }
                    @Nullable final Long version = expectedVersion;
                    if (version == null) {
                        throw new IllegalStateException("A non-empty cursor should have found a version.");
                    }
                    final ListVersionedType listVersioned = listVersionedFactory.apply(
                            items,
                            version
                    );

                    if (!isDisposed()) {
                        downstream.onNext(Optional.of(listVersioned));
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                onError(e);
            }
        }

        @Override
        public void onComplete() {
            if (!isDisposed()) {
                downstream.onComplete();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isDisposed()) {
                RxJavaPlugins.onError(e);
            } else {
                downstream.onError(e);
            }
        }
    }
}
