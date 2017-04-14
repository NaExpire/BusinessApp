package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperFoods extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="foods.db";
    private static final int SCHEMA=1;
    static final String TABLE="foods";
    static final String CHECKED="checked";

    public DatabaseHelperFoods(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE+" ("+CHECKED+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        throw new RuntimeException("How did we get here?");
    }
}
