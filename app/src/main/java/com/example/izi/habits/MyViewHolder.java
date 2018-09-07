package com.example.izi.habits;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public TextView habit;
    public ImageView handle;

    public MyViewHolder(Context context, ConstraintLayout layout) {
        super(layout);
        mContext = context;
        habit = (TextView) layout.findViewById(R.id.habit);
        handle = (ImageView) layout.findViewById(R.id.handle);
    }
}
