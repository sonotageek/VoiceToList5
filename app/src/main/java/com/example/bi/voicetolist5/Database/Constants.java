package com.example.bi.voicetolist5.Database;

/**
 * Created by bi on 16-Aug-17.
 */

public class Constants {

    static final int DATABASE_VERSION = 1;
    static final String DB_NAME = "tasklist.db";
    static final String TB_NAME = "task";

    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    public static final String COL_DATE = "created";
    public static final String COL_TIME = "time";

    static final String CREATE_TB="CREATE TABLE task(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL);";

    static final String DROP_TB="DROP TABLE IF EXISTS "+TB_NAME;

    static final String CREATE_TABLE_TUTORIALS = "CREATE TABLE task(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL, created DATETIME DEFAULT CURRENT_DATE);";

    static final String CREATE_TABLE = "CREATE TABLE task(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL, created DATETIME DEFAULT CURRENT_DATE, time DATETIME DEFAULT CURRENT_TIME );";


}

/* //https://tips.androidhive.info/2013/10/android-insert-datetime-value-in-sqlite-database/
* CREATE TABLE users(
    id INTEGER PRIMARY KEY,
    username TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
* */

/*
* CURRENT_TIME – Inserts only time
CURRENT_DATE – Inserts only date
CURRENT_TIMESTAMP – Inserts both time and date
* */
