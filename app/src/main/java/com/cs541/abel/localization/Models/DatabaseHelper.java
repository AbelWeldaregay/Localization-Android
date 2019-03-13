package com.cs541.abel.localization.Models;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Locations.db";
    public static final String TABLE_NAME = "checkInLocations_table";
    public static final String COL_1 = "TIME";
    public static final String COL_2 = "LONGITUDE";
    public static final String COL_3 = "LATITUDE";
    public static final String COL_4 = "NICKNAME";
    public static final String COL_5 = "ADDRESS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT, LONGITUDE TEXT, NICKNAME TEXT, ADDRESS TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}
