package com.example.izi.habits;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

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
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Toast.makeText(mContext, "SWIPEDDDDD", Toast.LENGTH_SHORT).show();
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
    }
}
