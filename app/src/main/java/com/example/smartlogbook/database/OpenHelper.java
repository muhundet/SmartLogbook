package com.example.smartlogbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.smartlogbook.database.DatabaseContract.RegisterEntry.COLUMN_DATE;
import static com.example.smartlogbook.database.DatabaseContract.RegisterEntry.COLUMN_STATUS;
import static com.example.smartlogbook.database.DatabaseContract.RegisterEntry.TABLE_NAME;

public class OpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SmartLogbook.db";
    public static final int DATABASE_VERSION = 1;

    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.EmployeesEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.RegisterEntry.SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean saveRegisterEntry(String registerEntryId, String employeeId, String date, String timeIn, String timeOut, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.RegisterEntry.COLUMN_REGISTER_ENTRY_ID, registerEntryId);
        contentValues.put(DatabaseContract.RegisterEntry.COLUMN_EMPLOYEE_ID, employeeId);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(DatabaseContract.RegisterEntry.COLUMN_TIME_IN, timeIn);
        contentValues.put(DatabaseContract.RegisterEntry.COLUMN_TIME_OUT, timeOut);
        contentValues.put(COLUMN_STATUS, status);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }


    public void updateRegisterEntryStatus(String registerEntryID, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, DatabaseContract.RegisterEntry.COLUMN_REGISTER_ENTRY_ID + "=" + registerEntryID, null);
        db.close();
        ;
    }


    public Cursor getAllRegister(String employeeId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = " + date + ";";
//        <!--TODO: Select register entries by date and merge with employee table-->
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getUnsynchronizedRegisterEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

//    TODO; To add a method that returns the last element so as to use it as register entry id

}
