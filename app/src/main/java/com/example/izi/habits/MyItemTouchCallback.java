package com.example.izi.habits;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;

public class MyItemTouchCallback extends ItemTouchHelper.SimpleCallback {

    Context mContext;
    MainActivity mainActivity;
    SQL mSQL;

    public MyItemTouchCallback(Context context, int drag, int swipe){
        super(drag, swipe);
        mContext = context;
        mainActivity = (MainActivity) mContext;
        mSQL = new SQL(mContext, null, null, 1);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getLayoutPosition();
        int to = target.getLayoutPosition();
        int dbPositionFrom = from +1;
        int dbPositionTo = to+1;
        String strFrom = String.valueOf(dbPositionFrom);
        String strTo = String.valueOf(dbPositionTo);

        mainActivity.mDB = mainActivity.mSQL.getWritableDatabase();
        mainActivity.mDB.execSQL("update habits set _id = (case when _id = "+strFrom+" then -"+strTo+" else -"+strFrom+" end) where _id in ( "+strFrom+" , "+strTo+" )");
        mainActivity.mDB.execSQL("update habits set _id = - _id where _id < 0");
        mainActivity.mAdapter.notifyItemMoved(from, to);

        //check if one of the replaced rows is the expanded row
        MyAdapter myAdapter = ((MainActivity)mContext).mAdapter;
        int expandedIndex = myAdapter.getExpandedIndex();
        if(expandedIndex==from)
            myAdapter.updateExpandedIndex(to);
        if(expandedIndex==to)
            myAdapter.updateExpandedIndex(from);

        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        TextView tv = ((ConstraintLayout)viewHolder.itemView).findViewById(R.id.habit);
//        String str =  tv.getText().toString();
//        ((MainActivity)mContext).startAlertDialog(str);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(viewHolder != null){
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder != null){
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.itemView.setBackgroundColor(0);
        }
        ((MainActivity)mContext).updateCursor();
    }
}
