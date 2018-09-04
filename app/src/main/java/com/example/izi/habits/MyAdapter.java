package com.example.izi.habits;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_MONTH;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_TOTAL_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_YEAR;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context mContext;
    Cursor mCursor;

    public MyAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public long getItemId(int position) {
        super.getItemId(position);
        mCursor.moveToPosition(position);
        return mCursor.getInt(0);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Button buttonHabit = (Button) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mContext, buttonHabit);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        ((Button)holder.itemView).setText(mCursor.getString(1));
        holder.position = position;
        holder.databaseId = mCursor.getInt(0);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void setCursor(Cursor cursor){
        mCursor.close();
        mCursor = cursor;
    }
}
