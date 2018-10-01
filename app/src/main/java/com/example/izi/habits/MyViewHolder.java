package com.example.izi.habits;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public MyConstraintLayout mMyConstraintLayout;
    public TextView habit;
    public ImageView handle;
    public Button buttonNotify;
    public Button buttonEdit;
    public Button buttonDelete;
    public Button buttonHistory;
    public MyViewHolder(Context context, MyConstraintLayout layout) {
        super(layout);
        mContext = context;
        mMyConstraintLayout = layout;
        habit = (TextView) layout.findViewById(R.id.habit);
        handle = (ImageView) layout.findViewById(R.id.handle);
        buttonNotify = (Button) layout.findViewById(R.id.btnNotify);
        buttonEdit = (Button) layout.findViewById(R.id.btnEdit);
        buttonDelete = (Button) layout.findViewById(R.id.btnDelete);
        buttonHistory = (Button) layout.findViewById(R.id.btnHistory);
    }
}
