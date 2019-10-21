/*
 * Copyright (C) 2017 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hborders.heathcast.dao;

import android.database.Cursor;

import androidx.annotation.Nullable;

import com.squareup.sqlbrite3.SqlBrite;

import java.util.ArrayList;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;

final class QueryToSpecificArrayListOperator<L extends ArrayList<T>, T> implements ObservableOperator<L, SqlBrite.Query> {

    interface NewSpecificArrayList<L extends ArrayList<T>, T> {
        L newSpecificArrayList(int initialCapacity);
    }

    private final Function<Cursor, T> mapper;
    private final NewSpecificArrayList<L, T> newSpecificArrayList;

    QueryToSpecificArrayListOperator(
            Function<Cursor, T> mapper,
            NewSpecificArrayList<L, T> newSpecificArrayList
    ) {
        this.mapper = mapper;
        this.newSpecificArrayList = newSpecificArrayList;
    }

    @Override public Observer<? super SqlBrite.Query> apply(Observer<? super L> observer) {
        return new MappingObserver<L, T>(
                observer,
                mapper,
                newSpecificArrayList
        );
    }

    private static final class MappingObserver<L extends ArrayList<T>, T> extends DisposableObserver<SqlBrite.Query> {
        private final Observer<? super L> downstream;
        private final Function<Cursor, T> mapper;
        private final NewSpecificArrayList<L, T> newSpecificArrayList;

        MappingObserver(
                Observer<? super L> downstream,
                Function<Cursor, T> mapper,
                NewSpecificArrayList<L, T> newSpecificArrayList
        ) {
            this.downstream = downstream;
            this.mapper = mapper;
            this.newSpecificArrayList = newSpecificArrayList;
        }

        @Override protected void onStart() {
            downstream.onSubscribe(this);
        }

        @Override public void onNext(SqlBrite.Query query) {
            try {
                @Nullable final Cursor cursor = query.run();
                if (cursor == null || isDisposed()) {
                    return;
                }
                L items = newSpecificArrayList.newSpecificArrayList(cursor.getCount());
                try {
                    while (cursor.moveToNext()) {
                        items.add(mapper.apply(cursor));
                    }
                } finally {
                    cursor.close();
                }
                if (!isDisposed()) {
                    downstream.onNext(items);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                onError(e);
            }
        }

        @Override public void onComplete() {
            if (!isDisposed()) {
                downstream.onComplete();
            }
        }

        @Override public void onError(Throwable e) {
            if (isDisposed()) {
                RxJavaPlugins.onError(e);
            } else {
                downstream.onError(e);
            }
        }
    }
}

