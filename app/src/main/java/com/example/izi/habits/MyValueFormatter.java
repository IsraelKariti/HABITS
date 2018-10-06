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
        int dayInYear = 0;
        int daysIn400YRS = 146097;
        int daysIn100YRS = 36524;
        int daysIn4YRS = 1461;
        int fullYear = 0;
        int roundsOf400YRS = totalDays/daysIn400YRS;
        totalDays = totalDays%daysIn400YRS;
        int roundsOf100YRS = totalDays/daysIn100YRS;
        totalDays = totalDays%daysIn100YRS;
        int roundsOf4YRS = totalDays/daysIn4YRS;
        totalDays = totalDays%daysIn4YRS;
        if(totalDays > 3*365){ // if day is in the leap year
            fullYear = roundsOf400YRS*400 + roundsOf100YRS*100 + roundsOf4YRS*4 + 3;
            dayInYear = totalDays%3*365;

            if( dayInYear<=31) {
                month = 1;
                day = dayInYear;
            }
            else if (dayInYear <= 60) {
                month = 2;
                day = dayInYear - 31;
            }
            else if(dayInYear <= 91) {
                month = 3;
                day = dayInYear - 60;
            }
            else if(dayInYear <= 121) {
                month = 4;
                day = dayInYear - 91;
            }
            else if (dayInYear <= 152) {
                month = 5;
                day = dayInYear - 121;
            }
            else if (dayInYear <= 182) {
                month = 6;
                day = dayInYear - 152;
            }
            else if (dayInYear <= 213) {
                month = 7;
                day = dayInYear - 182;
            }
            else if (dayInYear <= 244) {
                month = 8;
                day = dayInYear - 213;
            }
            else if (dayInYear <= 274) {
                month = 9;
                day = dayInYear - 244;
            }
            else if (dayInYear <= 305) {
                month = 10;
                day = dayInYear - 274;
            }
            else if (dayInYear <= 335) {
                month = 11;
                day = dayInYear - 305;
            }
            else {
                month = 12;
                day = dayInYear - 335;
            }
        }
        else{                   // if this is normal year
            int roundsOf1YRS = totalDays/365;
            totalDays = totalDays%365;
            fullYear = roundsOf400YRS*400 + roundsOf100YRS*100 + roundsOf4YRS*4 + roundsOf1YRS;
            dayInYear = totalDays;

            if( dayInYear<=31) {
                month = 1;
                day = dayInYear;
            }
            else if (dayInYear <= 59) {
                month = 2;
                day = dayInYear - 31;
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
        }
        year = fullYear + 1; // current year is partial, so it is one more than the amount of full years

        String dayName = String.valueOf(day);
        String monthName = String.valueOf(month);
        String yearName = String.valueOf(year);
        return dayName+"/"+monthName+"/"+yearName;
    }
}
