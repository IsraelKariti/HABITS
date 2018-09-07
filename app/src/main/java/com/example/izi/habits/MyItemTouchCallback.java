package com.example.izi.habits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static com.example.izi.habits.MyContract.MainTable.COLUMN_HABIT_NAME;
import static com.example.izi.habits.MyContract.MainTable.TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable._ID;

public class MyItemTouchCallback extends ItemTouchHelper.SimpleCallback {

    Context mContext;
    MainActivity mainActivity;
    String mDeletedHabit;
    int mDeletedHabitDbId;
    SQL mSQL;
    SQLiteDatabase mDB;

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

    }

    @Override
    public boolean isLongPressDragEnabled() {
        super.isLongPressDragEnabled();
        return true;
    }
}
