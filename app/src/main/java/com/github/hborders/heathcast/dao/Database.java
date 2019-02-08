package com.github.hborders.heathcast.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Factory;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;
import static com.github.hborders.heathcast.dao.Database.Schema.EmployeeTable.ID;
import static com.github.hborders.heathcast.dao.Database.Schema.EmployeeTable.NAME;
import static com.github.hborders.heathcast.dao.Database.Schema.EmployeeTable.USERNAME;
import static com.github.hborders.heathcast.dao.Database.Schema.ManagerTable.EMPLOYEE_ID;
import static com.github.hborders.heathcast.dao.Database.Schema.ManagerTable.MANAGER_ID;

public final class Database {
    private final BriteDatabase briteDatabase;
    private Database(
            Context context,
            @Nullable String name
    ) {
        final Configuration configuration = Configuration
                .builder(context)
                .callback(new Schema())
                .name(name)
                .build();
        final Factory factory = new FrameworkSQLiteOpenHelperFactory();
        final SupportSQLiteOpenHelper supportSQLiteOpenHelper = factory.create(configuration);
        final SqlBrite sqlBrite = new SqlBrite.Builder().build();
        this.briteDatabase = sqlBrite.wrapDatabaseHelper(supportSQLiteOpenHelper, Schedulers.io());
    }

    static final class Schema extends Callback {
        static final String TABLE_EMPLOYEE = "employee";
        static final String TABLE_MANAGER = "manager";

        static final String SELECT_EMPLOYEES =
                "SELECT " + USERNAME + ", " + NAME + " FROM " + TABLE_EMPLOYEE;
        static final String SELECT_MANAGER_LIST = ""
                + "SELECT e." + NAME + ", m." + NAME + " "
                + "FROM " + TABLE_MANAGER + " AS manager "
                + "JOIN " + TABLE_EMPLOYEE + " AS e "
                + "ON manager." + EMPLOYEE_ID + " = e." + ID + " "
                + "JOIN " + TABLE_EMPLOYEE + " as m "
                + "ON manager." + MANAGER_ID + " = m." + ID;
        static final Collection<String> BOTH_TABLES =
                Arrays.asList(TABLE_EMPLOYEE, TABLE_MANAGER);

        interface EmployeeTable {
            String ID = "_id";
            String USERNAME = "username";
            String NAME = "name";
        }

        static final class Employee {
            static final Function<Cursor, Employee> MAPPER = cursor -> new Employee(
                    cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME))
            );

            final String username;
            final String name;

            Employee(String username, String name) {
                this.username = username;
                this.name = name;
            }

            @Override
            public boolean equals(Object o) {
                if (o == this) return true;
                if (!(o instanceof Employee)) return false;
                Employee other = (Employee) o;
                return username.equals(other.username) && name.equals(other.name);
            }

            @Override
            public int hashCode() {
                return username.hashCode() * 17 + name.hashCode();
            }

            @Override
            public String toString() {
                return "Employee[" + username + ' ' + name + ']';
            }
        }

        interface ManagerTable {
            String ID = "_id";
            String EMPLOYEE_ID = "employee_id";
            String MANAGER_ID = "manager_id";
        }

        private static final String CREATE_EMPLOYEE = "CREATE TABLE " + TABLE_EMPLOYEE + " ("
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + USERNAME + " TEXT NOT NULL UNIQUE, "
                + NAME + " TEXT NOT NULL)";
        private static final String CREATE_MANAGER = "CREATE TABLE " + TABLE_MANAGER + " ("
                + ManagerTable.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + EMPLOYEE_ID + " INTEGER NOT NULL UNIQUE REFERENCES " + TABLE_EMPLOYEE + "(" + ID + "), "
                + MANAGER_ID + " INTEGER NOT NULL REFERENCES " + TABLE_EMPLOYEE + "(" + ID + "))";

        long aliceId;
        long bobId;
        long eveId;

        Schema() {
            super(1);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");

            db.execSQL(CREATE_EMPLOYEE);
            aliceId = db.insert(TABLE_EMPLOYEE, CONFLICT_FAIL, employee("alice", "Alice Allison"));
            bobId = db.insert(TABLE_EMPLOYEE, CONFLICT_FAIL, employee("bob", "Bob Bobberson"));
            eveId = db.insert(TABLE_EMPLOYEE, CONFLICT_FAIL, employee("eve", "Eve Evenson"));

            db.execSQL(CREATE_MANAGER);
            db.insert(TABLE_MANAGER, CONFLICT_FAIL, manager(eveId, aliceId));
        }

        static ContentValues employee(String username, String name) {
            ContentValues values = new ContentValues();
            values.put(USERNAME, username);
            values.put(NAME, name);
            return values;
        }

        static ContentValues manager(long employeeId, long managerId) {
            ContentValues values = new ContentValues();
            values.put(EMPLOYEE_ID, employeeId);
            values.put(MANAGER_ID, managerId);
            return values;
        }

        @Override
        public void onUpgrade(
                SupportSQLiteDatabase db,
                int oldVersion,
                int newVersion
        ) {
            throw new AssertionError();
        }
    }
}
