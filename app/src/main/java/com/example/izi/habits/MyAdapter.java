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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.izi.habits.Interface.DragInterface;

import java.util.Calendar;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_MONTH;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_TOTAL_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_YEAR;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context mContext;
    Cursor mCursor;
    DragInterface mDragInterface;
    public MyAdapter(Context context, Cursor cursor, DragInterface dragInterface){
        mContext = context;
        mCursor = cursor;
        mDragInterface = dragInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mContext, layout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        ((Button)holder.habit).setText(mCursor.getString(1));
        ((ImageView)holder.habdle).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDragInterface.beginDrag(holder);
                return false;
            }
        });
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
