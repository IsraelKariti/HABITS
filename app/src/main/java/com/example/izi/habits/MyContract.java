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
                _ID + " INTEGER PRIMARY KEY " + COMMA +
                COLUMN_HABIT_NAME + " TEXT " +
                " ) ";
    }

    public class LogTable{
        public static final String LOG_TABLE_NAME = "LOG";

        public static final String LOG_COLUMN_ID = BaseColumns._ID;
        public static final String LOG_COLUMN_HABIT = " Habit ";
        public static final String LOG_COLUMN_TOTAL_DAY = " TotalDay ";
        public static final String LOG_COLUMN_YEAR = " Year ";
        public static final String LOG_COLUMN_MONTH = " Month ";
        public static final String LOG_COLUMN_DAY = " Day ";
        public static final String LOG_COLUMN_NOTE_COUNT = "noteCount";
        public static final String LOG_COLUMN_STARTING_TIME = " StartingTime ";
        public static final String LOG_COLUMN_DURATION = " Duration ";


        public static final String LOG_TEXT = " TEXT ";
        public static final String LOG_INT = " INTEGER ";

        public static final String COMMA = " , ";
        public static final String LOG_CREATE_TABLE = "CREATE TABLE "+ LOG_TABLE_NAME + " ( "+
                /* 0 */     LOG_COLUMN_ID +         LOG_INT +" PRIMAREY KEY "+COMMA +
                /* 1 */     LOG_COLUMN_HABIT +      LOG_TEXT + COMMA +
                /* 2 */     LOG_COLUMN_TOTAL_DAY +  LOG_INT + COMMA+
                /* 3 */     LOG_COLUMN_YEAR +       LOG_INT + COMMA+
                /* 4 */     LOG_COLUMN_MONTH +      LOG_INT + COMMA+
                /* 5 */     LOG_COLUMN_DAY +        LOG_INT + COMMA+
                /* 6 */     LOG_COLUMN_NOTE_COUNT+  LOG_INT + COMMA+
                /* 7 */     LOG_COLUMN_STARTING_TIME+LOG_INT + COMMA+
                /* 8 */     LOG_COLUMN_DURATION +   LOG_INT +" )";
    }
}
