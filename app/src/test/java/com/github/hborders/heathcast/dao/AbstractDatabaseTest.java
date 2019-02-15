package com.github.hborders.heathcast.dao;

import androidx.test.core.app.ApplicationProvider;

import javax.annotation.Nullable;

import io.reactivex.schedulers.Schedulers;

public class AbstractDatabaseTest {
    @Nullable
    private Database database;

    protected final Database getDatabase() {
        @Nullable final Database initialDatabase = this.database;
        if (initialDatabase == null) {
            final Database newDatabase = new Database(
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
