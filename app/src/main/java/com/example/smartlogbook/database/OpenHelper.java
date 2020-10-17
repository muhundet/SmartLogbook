package com.example.smartlogbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.smartlogbook.models.RegisterEntryModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.smartlogbook.database.DatabaseContract.RegisterEntry.COLUMN_DATE;
import static com.example.smartlogbook.database.DatabaseContract.RegisterEntry.COLUMN_STATUS;
import static com.example.smartlogbook.database.DatabaseContract.RegisterEntry.TABLE_NAME;

public class OpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SmartLogbook.db";
    public static final int DATABASE_VERSION = 1;
    private final List<RegisterEntryModel> registerEntry = new ArrayList<>();

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
//        TODO: the calling method should check if the user already clocked in today
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
    }


    public Cursor getRegisterByDate( String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = " + date + ";";
//        TODO: Select register entries by date and merge with employee table-->
        String sql = "SELECT * FROM " + DatabaseContract.RegisterEntry.TABLE_NAME + " INNER JOIN " + DatabaseContract.EmployeesEntry.TABLE_NAME
                + " ON " + DatabaseContract.RegisterEntry.COLUMN_EMPLOYEE_ID + " = " + DatabaseContract.EmployeesEntry.COLUMN_EMPLOYEE_ID
                + " WHERE " + COLUMN_DATE + " = " +  date;
        return db.rawQuery(sql, null);
    }


    public Cursor getUnsynchronizedRegisterEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

//        TODO; To add a method that returns the last element so as to use it as register entry id

    public Boolean isLoggedInAlready(String employeeID){
        Cursor cursor = getRegisterByDate( "datenow");
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_EMPLOYEE_ID))== employeeID){
                return true;
            }
        }
//        TODO: check if employee already clocked in check with today's date
        return false;
    }

    public List populateRegisterEntries(){
        registerEntry.clear();
        Cursor cursor = getRegisterByDate( "datenow");
        if (cursor.moveToFirst()) {
            do {
                RegisterEntryModel name = new RegisterEntryModel(
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_REGISTER_ENTRY_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_EMPLOYEE_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.EmployeesEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.EmployeesEntry.COLUMN_SURNAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.EmployeesEntry.COLUMN_DEPARTMENT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_TIME_IN)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_TIME_OUT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_STATUS))
                );
                registerEntry.add(name);
            } while (cursor.moveToNext());

        }else{
///        TODO: show the user that there are no records yet
        }
        return registerEntry;
    }
}
