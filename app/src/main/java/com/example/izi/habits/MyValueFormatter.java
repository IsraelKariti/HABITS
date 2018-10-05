package com.example.izi.habits;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/*
* this is a shitty implementation of date converter
* it has been made when time is on the essence
* and with the intent of making good perfocemance code without too much computation
* please REWRITE THIS ENTIRE CLASS
* */

public class MyValueFormatter implements IAxisValueFormatter {
    int year;
    int month;
    int day;
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int totalDays = (int) value;
        Log.i("XXXX", "total days: "+String.valueOf(totalDays));
        int pastDays = 2017*365; // 2017 full years =
        int presentDays = totalDays - pastDays;
        Log.i("XXXX", "present days: "+String.valueOf(presentDays));

        if(presentDays > 365 )
            year = 2019;
        else
            year = 2018;

        Log.i("XXXX", "year: "+String.valueOf(year));

        int dayInYear = presentDays%365;
        if( dayInYear<=31) {
            month = 1;
            day = dayInYear;
        }
        else if (dayInYear <= 59) {
            month = 2;
            day = dayInYear = 31;
        }
        else if(dayInYear <= 90) {
            month = 3;
            day = dayInYear - 59;
        }
        else if(dayInYear <= 120) {
            month = 4;
            day = dayInYear - 90;
        }
        else if (dayInYear <= 151) {
            month = 5;
            day = dayInYear - 120;
        }
        else if (dayInYear <= 181) {
            month = 6;
            day = dayInYear - 151;
        }
        else if (dayInYear <= 212) {
            month = 7;
            day = dayInYear - 181;
        }
        else if (dayInYear <= 243) {
            month = 8;
            day = dayInYear - 212;
        }
        else if (dayInYear <= 273) {
            month = 9;
            day = dayInYear - 243;
        }
        else if (dayInYear <= 304) {
            month = 10;
            day = dayInYear - 273;
        }
        else if (dayInYear <= 334) {
            month = 11;
            day = dayInYear - 304;
        }
        else {
            month = 12;
            day = dayInYear - 334;
        }
        String dayName = String.valueOf(day);
        String monthName = String.valueOf(month);
        String yearName = String.valueOf(year);
        return dayName+"/"+monthName+"/"+yearName;
    }
}
