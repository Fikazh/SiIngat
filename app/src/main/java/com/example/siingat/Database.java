package com.example.siingat;

import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SiIngat.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USER = "create table " +
            "Users" +
            "(UID TEXT PRIMARY KEY," +
            "Name TEXT null, " +
            "Gender TEXT null, " +
            "Birth TEXT null);";

    private static final String CREATE_TABLE_DAILY = "create table " +
            "Dailies" +
            "(ID_DAILY TEXT PRIMARY KEY," +
            "UID TEXT FORGEIN KEY," +
            "Day TEXT null, " +
            "Time TEXT null, " +
            "Description TEXT null," +
            "Priority INTEGER DEFAULT 0);";

    private static final String CREATE_TABLE_EVENT = "create table " +
            "Events" +
            "(ID_EVENT TEXT PRIMARY KEY," +
            "UID TEXT FORGEIN KEY," +
            "Date TEXT null, " +
            "Time TEXT null, " +
            "Description TEXT null," +
            "Priority INTEGER DEFAULT 0);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Data","onCreate: " + CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER);
        Log.d("Data","onCreate: " + CREATE_TABLE_DAILY);
        db.execSQL(CREATE_TABLE_DAILY);
        Log.d("Data","onCreate: " + CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

