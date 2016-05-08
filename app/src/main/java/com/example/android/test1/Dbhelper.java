package com.example.android.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sanu on 08-May-16.
 */
public class Dbhelper extends SQLiteOpenHelper {

    public static final String Database_Name="records.db";
    public static final String Table_Name="records_table";
    public static final String Col1="name";
    public static final String Col2="subject";
    public static final String Col3="message";
    public static final String Col4="time";
    public static final String Col5="image";
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Table_Name + "("
            + Col1+ " TEXT," + Col2 + " TEXT,"+ Col3 + " TEXT,"+ Col4 + " TEXT,"
            + Col5 + " TEXT" + ")";

    public Dbhelper(Context context) {
        super(context, Database_Name, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }



    public void insertData(String name, String subject, String message, String time, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(Col1, name);
            values.put(Col2, subject);
            values.put(Col3,message);
            values.put(Col4 ,time);
            values.put(Col5, image);
            db.insert(Table_Name, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem", e + "");
        }
    }


}
