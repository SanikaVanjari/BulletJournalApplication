package com.project.bulletjournalapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuJoSQLOpenHelper extends SQLiteOpenHelper {
    public static  final String DATABASE_NAME="bulletjournal.db";



    public BuJoSQLOpenHelper( Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (user_ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL,EMAIL TEXT NOT NULL,CELL_NO TEXT NOT NULL" +
                ",DOB TEXT NOT NULL )");

        db.execSQL("CREATE TABLE to_do(to_do_ID INTEGER PRIMARY KEY AUTOINCREMENT,CONTENT TEXT NOT NULL,DATE TEXT NOT NULL)");

        db.execSQL("CREATE TABLE goals(goals_ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTENT TEXT NOT NULL)");

        db.execSQL("CREATE TABLE journal(journal_ID INTEGER PRIMARY KEY AUTOINCREMENT,CONTENT TEXT NOT NULL,DATE TEXT NOT NULL)");
        db.execSQL(" CREATE TABLE collection(collection_ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT NOT NULL, CONTENT TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS to_do");
        db.execSQL("DROP TABLE IF EXISTS goals");
        db.execSQL("DROP TABLE IF EXISTS journal");
        db.execSQL("DROP TABLE IF EXISTS collection");
        onCreate(db);
    }
}
