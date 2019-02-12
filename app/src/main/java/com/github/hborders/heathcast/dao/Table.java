package com.github.hborders.heathcast.dao;

import android.content.ContentValues;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.squareup.sqlbrite3.BriteDatabase;

abstract class Table {
    protected static final Object[] EMPTY_BIND_ARGS = new Object[0];

    protected final BriteDatabase mBriteDatabase;

    protected Table(BriteDatabase briteDatabase) {
        this.mBriteDatabase = briteDatabase;
    }

    protected static void putIdentifier(ContentValues contentValues, String key, Identified<?> identified) {
        putIdentifier(contentValues, key, identified.mIdentifier);
    }

    protected static void putIdentifier(ContentValues contentValues, String key, Identifier<?> identifier) {
        contentValues.put(key, identifier.mId);
    }
}
