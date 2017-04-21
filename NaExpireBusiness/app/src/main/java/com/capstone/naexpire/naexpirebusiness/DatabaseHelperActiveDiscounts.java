package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by graemedrucker on 4/13/17.
 */

public class DatabaseHelperActiveDiscounts extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="activeDiscounts.db";
    private static final int SCHEMA=1;
    static final String TABLE="activeDiscounts";
    static final String ID="id";
    static final String NAME="name";
    static final String PRICE="price";
    static final String QUANTITY="quantity";
    static final String IMAGE="image";

    public DatabaseHelperActiveDiscounts(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE activeDiscounts (id TEXT, name TEXT, price TEXT, quantity TEXT, image TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        throw new RuntimeException("How did we get here?");
    }
}
