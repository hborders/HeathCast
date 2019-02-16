/*
 * Copyright (C) 2017 The Android Open Source Project
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

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * A simple query builder to create SQL SELECT queries.
 */
@SuppressWarnings("unused")
public final class SupportSQLiteQueryBuilder2 {
    private static final Pattern sLimitPattern =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    private boolean mDistinct = false;
    private final String mTable;
    @Nullable
    private String[] mColumns = null;
    @Nullable
    private String mSelection;
    @Nullable
    private Object[] mBindArgs;
    @Nullable
    private String mGroupBy = null;
    @Nullable
    private String mHaving = null;
    @Nullable
    private String mOrderBy = null;
    private boolean mOrderByDesc = false;
    @Nullable
    private String mLimit = null;

    /**
     * Creates a query for the given table name.
     *
     * @param tableName The table name(s) to query.
     * @return A builder to create a query.
     */
    public static SupportSQLiteQueryBuilder2 builder(String tableName) {
        return new SupportSQLiteQueryBuilder2(tableName);
    }

    private SupportSQLiteQueryBuilder2(String table) {
        mTable = table;
    }

    /**
     * Adds DISTINCT keyword to the query.
     *
     * @return this
     */
    public SupportSQLiteQueryBuilder2 distinct() {
        mDistinct = true;
        return this;
    }

    /**
     * Sets the given list of columns as the columns that will be returned.
     *
     * @param columns The list of column names that should be returned.
     * @return this
     */
    public SupportSQLiteQueryBuilder2 columns(String[] columns) {
        mColumns = columns;
        return this;
    }

    /**
     * Sets the arguments for the WHERE clause.
     *
     * @param selection The list of selection columns
     * @param bindArgs  The list of bind arguments to match against these columns
     * @return this
     */
    public SupportSQLiteQueryBuilder2 selection(String selection, Object[] bindArgs) {
        mSelection = selection;
        mBindArgs = bindArgs;
        return this;
    }

    /**
     * Adds a GROUP BY statement.
     *
     * @param groupBy The value of the GROUP BY statement.
     * @return this
     */
    @SuppressWarnings("WeakerAccess")
    public SupportSQLiteQueryBuilder2 groupBy(String groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    /**
     * Adds a HAVING statement. You must also provide {@link #groupBy(String)} for this to work.
     *
     * @param having The having clause.
     * @return this
     */
    public SupportSQLiteQueryBuilder2 having(String having) {
        mHaving = having;
        return this;
    }

    /**
     * Adds an ORDER BY statement.
     *
     * @param orderBy The order clause.
     * @return this
     */
    public SupportSQLiteQueryBuilder2 orderBy(String orderBy) {
        mOrderBy = orderBy;
        mOrderByDesc = false;
        return this;
    }

    /**
     * Adds an ORDER BY statement.
     *
     * @param orderBy The order clause.
     * @param desc whether to use descending order
     * @return this
     */
    public SupportSQLiteQueryBuilder2 orderBy(String orderBy, boolean desc) {
        mOrderBy = orderBy;
        mOrderByDesc = desc;
        return this;
    }

    /**
     * Adds a LIMIT statement.
     *
     * @param limit The limit value.
     * @return this
     */
    public SupportSQLiteQueryBuilder2 limit(String limit) {
        if (!isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }
        mLimit = limit;
        return this;
    }

    /**
     * Creates the {@link SupportSQLiteQuery} that can be passed into
     * {@link SupportSQLiteDatabase#query(SupportSQLiteQuery)}.
     *
     * @return a new query
     */
    public SupportSQLiteQuery create() {
        if (isEmpty(mGroupBy) && !isEmpty(mHaving)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if (mDistinct) {
            query.append("DISTINCT ");
        }
        if (mColumns != null && mColumns.length != 0) {
            appendColumns(query, mColumns);
        } else {
            query.append(" * ");
        }
        query.append(" FROM ");
        query.append(mTable);
        appendClause(query, " WHERE ", mSelection);
        appendClause(query, " GROUP BY ", mGroupBy);
        appendClause(query, " HAVING ", mHaving);
        appendClause(query, " ORDER BY ", mOrderBy);
        if (mOrderByDesc) {
            query.append(" DESC");
        }
        appendClause(query, " LIMIT ", mLimit);

        return new SimpleSQLiteQuery(query.toString(), mBindArgs);
    }

    private static void appendClause(StringBuilder s, String name, @Nullable String clause) {
        if (!isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     */
    private static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];
            if (i > 0) {
                s.append(", ");
            }
            s.append(column);
        }
        s.append(' ');
    }

    private static boolean isEmpty(@Nullable String input) {
        return input == null || input.length() == 0;
    }
}
