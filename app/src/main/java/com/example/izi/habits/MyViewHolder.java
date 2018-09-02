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

import java.util.Calendar;
import java.util.Date;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_MONTH;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_TOTAL_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_YEAR;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public Button mButtonHabit;
    public int position;

    public MyViewHolder(Context context, Button btn) {
        super(btn);
        mContext = context;
        mButtonHabit = createButtonHabit(btn);
    }

    // create the button which holds the habit and set its functionality
    private Button createButtonHabit(Button btn) {
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // VIBRATE
                Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    //deprecated in API 26
                    v.vibrate(100);
                }

                // ALERT DIALOG
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.buildAlertDialog(mButtonHabit.getText().toString());
                return true;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habit = ((Button) view).getText().toString();

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

                long totalDay = year*365+dayOfYear;

                ContentValues cv = new ContentValues();
                cv.put(LOG_COLUMN_TOTAL_DAY, totalDay);
                cv.put(LOG_COLUMN_YEAR, year);
                cv.put(LOG_COLUMN_MONTH, month);
                cv.put(LOG_COLUMN_DAY, day);
                cv.put(LOG_COLUMN_HABIT, habit);

                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.newLog(cv);
            }
        });
        return btn;
    }
}
