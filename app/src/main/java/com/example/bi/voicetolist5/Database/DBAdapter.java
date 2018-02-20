package com.example.bi.voicetolist5.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bi on 16-Aug-17.
 */

public class DBAdapter {

    Context context;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;


    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }



    //openDB
    public void openDB(){
        try {
            db= dbHelper.getWritableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }



    //closeDB
    public void closeDB(){
        try {
            dbHelper.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }



    public boolean add(String name){
        try {
            ContentValues cv=new ContentValues();
            cv.put(Constants.COL_2, name);
            cv.put(Constants.COL_DATE, getCDate());
            cv.put(Constants.COL_TIME, getCTime());
            //  cv.put(Constants.COL_DATE, getDateTime());

            db.insert(Constants.TB_NAME, Constants.COL_1, cv);
            return true;
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        return false;
    }



    public String getCDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getCTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //CURRENT_DATE, CURRENT_TIME, and CURRENT_TIMESTAMP
    public String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public Cursor retrieive(){
        String[] columns = {Constants.COL_1, Constants.COL_2, Constants.COL_DATE, Constants.COL_TIME};
      //  return db.query(Constants.TB_NAME, columns, null, null, null, null, null);  //problem here
        return db.query(Constants.TB_NAME, columns, null, null, null, null, Constants.COL_1 + " ASC"); //nosure why I need ASC
    }


    public boolean delete(int id){
        try {
            int result = db.delete(Constants.TB_NAME, Constants.COL_1+" =?", new String[] {String.valueOf(id)}); //compare ids
            if (result>0){
                return true;
            }
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        return false;
    }


    //select * from table order by name1, col2, col3, col4
    public Cursor sortDate(){
        String sortStr = Constants.COL_DATE + " ASC, " + Constants.COL_TIME + " ASC";
        String[] columns = { Constants.COL_1,Constants.COL_2, Constants.COL_DATE, Constants.COL_TIME};   //Constants.COL_DATE + " ASC"
        return db.query(Constants.TB_NAME, columns, null, null, null, null, sortStr);

        //https://stackoverflow.com/questions/8051800/sort-a-list-in-ascending-order-by-date-from-sqlite
    }

    public Cursor sortDateDesc(){
        String sortStr = Constants.COL_DATE + " DESC, " + Constants.COL_TIME + " DESC";
        String[] columns = { Constants.COL_1,Constants.COL_2, Constants.COL_DATE, Constants.COL_TIME};   //Constants.COL_DATE + " ASC"
        return db.query(Constants.TB_NAME, columns, null, null, null, null, sortStr);

        //https://stackoverflow.com/questions/8051800/sort-a-list-in-ascending-order-by-date-from-sqlite
    }

    //select * from table order by name1, col2, col3, col4
    public Cursor sortAZ(){
        String[] columns = { Constants.COL_1,Constants.COL_2, Constants.COL_DATE, Constants.COL_TIME};
        return db.query(Constants.TB_NAME, columns, null, null, null, null, Constants.COL_2 + " ASC");
    }

    public Cursor sortZA(){
        String[] columns = { Constants.COL_1,Constants.COL_2, Constants.COL_DATE, Constants.COL_TIME};
        return db.query(Constants.TB_NAME, columns, null, null, null, null, Constants.COL_2 + " DESC");
        // return db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
    }


    //http://camposha.info/source/android-sqlite-database-recyclerview-saveretrieve-searchfilter/
    //RETRIEVE OR FILTERING
    public Cursor searchRetrieve(String searchTerm){
        String[] columns = { Constants.COL_1,Constants.COL_2, Constants.COL_DATE, Constants.COL_TIME};
        Cursor cursor =null;
        if (searchTerm != null && searchTerm.length()>0){
            String sql = "SELECT * FROM "+Constants.TB_NAME+ " WHERE "+Constants.COL_2+" LIKE '%"+searchTerm+"%'";
            cursor = db.rawQuery(sql, null);
            return cursor;
        }
        cursor  = db.query(Constants.TB_NAME, columns, null, null, null, null, null);
        return cursor;
    } //end  searchRetrieve


}
