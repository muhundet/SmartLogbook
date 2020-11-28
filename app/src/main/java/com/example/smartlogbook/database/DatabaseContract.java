package com.example.smartlogbook.database;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract(){}

    public static final class EmployeesEntry implements BaseColumns{
        public static final String TABLE_NAME = "employees_entry";
        public static final String COLUMN_EMPLOYEE_ID = "employee_id";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_STATUS = "status";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EMPLOYEE_ID + " TEXT UNIQUE NOT NULL, " +
                COLUMN_FIRST_NAME + " TEXT NOT NULL, "+
                COLUMN_SURNAME + " TEXT NOT NULL, " +
                COLUMN_DEPARTMENT + " TEXT NOT NULL, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_STATUS + ")";

    }

    public static final class RegisterEntry implements BaseColumns{
        public static final String TABLE_NAME = "register_entry";
        public static final String COLUMN_EMPLOYEE_ID = "employee_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME_IN = "time_in";
        public static final String COLUMN_TIME_OUT = "time_out";
        public static final String COLUMN_STATUS = "status";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_EMPLOYEE_ID + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_TIME_IN + " TEXT NOT NULL, "+
                COLUMN_TIME_OUT + ", " +
                COLUMN_STATUS + ")";

    }
}
