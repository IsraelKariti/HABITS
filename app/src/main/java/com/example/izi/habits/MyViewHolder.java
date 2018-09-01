package com.example.izi.habits;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout mLayout;

    public MyViewHolder(View itemView) {
        super(itemView);
        mLayout = (ConstraintLayout) itemView;
    }
}
