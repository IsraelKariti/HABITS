package com.example.izi.habits;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.izi.habits.MyContract.LogTable.LOG_CREATE_TABLE;
import static com.example.izi.habits.MyContract.MainTable.CREATE_TABLE;

public class SQL extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "habits.db";

    public SQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
        //sqLiteDatabase.execSQL(LOG_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
