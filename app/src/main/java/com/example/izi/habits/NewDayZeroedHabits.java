package com.example.izi.habits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DURATION;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_MONTH;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_NOTE_COUNT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_STARTING_TIME;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_TOTAL_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_YEAR;
import static com.example.izi.habits.MyContract.LogTable.LOG_TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable.COLUMN_HABIT_NAME;
import static com.example.izi.habits.MyContract.MainTable.TABLE_NAME;

public class NewDayZeroedHabits {

    public static void newDayZeroedHabits(Context context){
        // get the habits
        SQL sql = new SQL(context, null, null, 1);
        SQLiteDatabase mDB = sql.getReadableDatabase();
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{COLUMN_HABIT_NAME}, null, null, null, null, null);
        cursor.moveToFirst();
        mDB = sql.getWritableDatabase();

        // get the date
        // get the current total day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // change 0-based month 1-based month
        int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayInYear = calendar.get(Calendar.DAY_OF_YEAR);
        int totalDay = daysInYears(year-1) + dayInYear; // calculate what day is it (on calendar) when Jan 1st 0001 is the first day of all time

        while(!cursor.isAfterLast()){
            // insert this new habit into LOG table
            mDB = sql.getWritableDatabase();
            ContentValues cv_new = new ContentValues();
            cv_new.put(LOG_COLUMN_HABIT, cursor.getString(0));
            cv_new.put(LOG_COLUMN_TOTAL_DAY, totalDay);
            cv_new.put(LOG_COLUMN_YEAR, year);
            cv_new.put(LOG_COLUMN_MONTH, month);
            cv_new.put(LOG_COLUMN_DAY, dayInMonth);
            cv_new.put(LOG_COLUMN_NOTE_COUNT, 0);
            cv_new.put(LOG_COLUMN_STARTING_TIME, -1);
            cv_new.put(LOG_COLUMN_DURATION, 0);
            mDB.insert(LOG_TABLE_NAME, null, cv_new);
            cursor.moveToNext();
        }

    }

    private static int daysInYears(int years) {
        int incubatedYRS4 = years / 4;
        int incubatedYRS100 = years / 100;
        int incubatedYRS400 = years / 400;
        int incubatedYRS = incubatedYRS4 - incubatedYRS100 + incubatedYRS400;
        int normalYRS = years - incubatedYRS;
        int res = normalYRS*365 + incubatedYRS*366;
        return res;
    }

}
