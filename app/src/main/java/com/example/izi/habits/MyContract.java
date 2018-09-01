package com.example.izi.habits;

import android.provider.BaseColumns;

public class MyContract {
    private MyContract(){}

    public class MainTable{
        public static final String TABLE_NAME = "HABITS";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT_NAME = "HABIT_NAME";

        public static final String COMMA = " , ";
        public static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " ( "+
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA +
                COLUMN_HABIT_NAME + " TEXT " +
                " ) ";
    }
}
