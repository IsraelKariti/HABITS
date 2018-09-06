package com.example.izi.habits;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_MONTH;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_TOTAL_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_YEAR;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public Button habit;
    public ImageView habdle;

    public MyViewHolder(Context context, ConstraintLayout layout) {
        super(layout);
        mContext = context;
        habit = (Button) layout.findViewById(R.id.habit);
        habdle = (ImageView) layout.findViewById(R.id.handle);
    }
}
