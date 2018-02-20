package com.example.bi.voicetolist5.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bi on 16-Aug-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // db.execSQL(Constants.CREATE_TB);
           // db.execSQL(Constants.CREATE_TABLE_TUTORIALS);
            db.execSQL(Constants.CREATE_TABLE);

        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.DROP_TB);
        onCreate(db); //create new table with structures
    }



}
