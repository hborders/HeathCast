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
}
