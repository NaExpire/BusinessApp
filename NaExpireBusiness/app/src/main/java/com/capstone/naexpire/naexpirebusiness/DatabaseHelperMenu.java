package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperMenu extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="menu.db";
    private static final int SCHEMA=1;
    static final String TABLE="menu";
    static final String ID="id";
    static final String NAME="name";
    static final String PRICE="price";
    static final String DESCRIPTION="description";
    static final String IMAGE="image";

    public DatabaseHelperMenu(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE menu (id TEXT, name TEXT, price TEXT, description TEXT, image TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        throw new RuntimeException("How did we get here?");
    }
}
