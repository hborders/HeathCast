package com.github.hborders.heathcast.dao;

import androidx.test.core.app.ApplicationProvider;

import javax.annotation.Nullable;

import io.reactivex.schedulers.Schedulers;

public class AbstractDatabaseTest<N> {
    @Nullable
    private Database<N> database;

    protected final Database<N> getDatabase() {
        @Nullable final Database<N> initialDatabase = this.database;
        if (initialDatabase == null) {
            final Database<N> newDatabase = new Database<>(
                    ApplicationProvider.getApplicationContext(),
                    null, // in-memory
                    Schedulers.trampoline()
            );
            this.database = newDatabase;
            return newDatabase;
        } else {
            return initialDatabase;
        }
    }
}
