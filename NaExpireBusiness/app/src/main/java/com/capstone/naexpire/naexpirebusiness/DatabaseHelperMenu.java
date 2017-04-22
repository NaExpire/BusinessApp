package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperMenu extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="menu.db";
    private static final int SCHEMA=3;
    static final String TABLE="menu";
    static final String ID="id";
    static final String NAME="name";
    static final String PRICE="price";
    static final String DESCRIPTION="description";
    static final String QUANTITY="quantity";
    static final String DEAL="deal";
    static final String IMAGE="image";

    public DatabaseHelperMenu(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE+" ("+ID+" TEXT, "+NAME+" TEXT, "+PRICE+" TEXT, "
                +DESCRIPTION+" TEXT, "+QUANTITY+" TEXT, "+DEAL+" TEXT, "+IMAGE+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // Create a new one.
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion,
                            int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // Create a new one.
        onCreate(db);
    }
}
