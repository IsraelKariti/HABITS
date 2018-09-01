package com.example.izi.habits;

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

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Cursor mCursor;

    public MyAdapter(Cursor cursor){
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
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(constraintLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        ((Button) holder.mLayout.findViewById(R.id.habit) ).setText(mCursor.getString(1));
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
