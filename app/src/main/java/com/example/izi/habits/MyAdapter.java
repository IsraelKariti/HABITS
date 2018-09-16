package com.example.izi.habits;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context mContext;
    Cursor mCursor;
    public MyAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        //todo set on click listener to open the bigredbutton
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = layout.findViewById(R.id.button);
                //todo smooth animation of layout - maybe with constraintlayout animations?
                if (button.getVisibility() == View.GONE) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
            }
        });
        MyViewHolder myViewHolder = new MyViewHolder(mContext, layout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        ((TextView)holder.habit).setText(mCursor.getString(1));
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.mRecyclerView.requestFocus();
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
