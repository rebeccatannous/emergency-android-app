package com.mobile.safetyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class ContactSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="contacts";
    private static final int DB_VERSION= 1;

    public ContactSQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PROFILE (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "BIRTHDAY TEXT," +
                "BLOODTYPE TEXT," +
                "PHONE TEXT);");

        db.execSQL("CREATE TABLE CONTACTS (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "PHONE TEXT);");

        insertCredentials(db,"Jane Doe", "19/10/2001","A+","76461083");

    }

    private static void insertCredentials(SQLiteDatabase db,String name, String birthday, String bloodtype,String phone){
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("BIRTHDAY", birthday);
        contentValues.put("BLOODTYPE", bloodtype);
        contentValues.put("PHONE", phone);
        db.insert("PROFILE",null,contentValues);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
