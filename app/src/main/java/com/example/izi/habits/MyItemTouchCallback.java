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

        Button btnHabit = (Button) viewHolder.itemView;
        float y = btnHabit.getY();
        float height = btnHabit.getHeight();

        if(dX>0){
            //draw red background
            Rect rect = new Rect(0, (int)y, (int)dX, (int)(y+height) );
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffD32F2F"));
            c.drawRect(rect, paint);

            // draw trashcan icon
            VectorDrawable vd = (VectorDrawable) ContextCompat.getDrawable(mContext, R.drawable.ic_delete_white_24dp);
            float vd_width = vd.getIntrinsicWidth();
            float vd_height = vd.getIntrinsicHeight();
            float scale = height / vd_height;


            if(dX < vd_width*scale)
                vd.setBounds(0,(int)y, (int)dX, (int)(y+height));
            else
                vd.setBounds(0, (int)y, (int)(vd_width*scale),(int)(y+height));
            vd.draw(c);
        }
        else if(dX<0){
            // get screen screenWidth
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;

            //draw green background
            Rect rect = new Rect(screenWidth+(int)dX, (int)y, screenWidth, (int)(y+height) );
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ff36f7a1"));
            c.drawRect(rect, paint);

            // draw edit icon
            VectorDrawable vd = (VectorDrawable) ContextCompat.getDrawable(mContext, R.drawable.ic_edit_white_24dp);
            float vd_width = vd.getIntrinsicWidth();
            float vd_height = vd.getIntrinsicHeight();
            float scale = height / vd_height;

            if(Math.abs(dX) < vd_width*scale)
                vd.setBounds(screenWidth+(int)dX,(int)y, screenWidth, (int)(y+height));
            else
                vd.setBounds(screenWidth-(int)(vd_width*scale), (int)y,screenWidth,(int)(y+height));
            vd.draw(c);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int id = (int) viewHolder.getItemId();

        // DELETE
        if(direction == ItemTouchHelper.RIGHT){
            // save habit before deletion (for UNDO button)
            mDeletedHabit = ((Button)((MyViewHolder)viewHolder).itemView).getText().toString();

            mDB = mSQL.getReadableDatabase();
            Cursor cursor = mDB.query(TABLE_NAME, new String[]{_ID}, COLUMN_HABIT_NAME+"=?", new String[]{mDeletedHabit}, null, null, null);
            cursor.moveToFirst();
            mDeletedHabitDbId = cursor.getInt(0);

            // delete habit
            mDB = mSQL.getWritableDatabase();
            mDB.delete(TABLE_NAME, _ID+"=?", new String[]{String.valueOf(mDeletedHabitDbId)});

            // update id after deletion
            mDB.execSQL("update habits set _id = _id - 1 where _id > "+String.valueOf(mDeletedHabitDbId));

            mainActivity.updateHabitsCursor();
            mainActivity.mAdapter.notifyDataSetChanged();

            // Snackbar to UNDO the delete
            Snackbar snackbar = Snackbar.make(mainActivity.mCoordinatorLayout, R.string.snackbarMessage , Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbarButton, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDB = mSQL.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(_ID, mDeletedHabitDbId);
                    cv.put(COLUMN_HABIT_NAME, mDeletedHabit);
                    mDB.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    mainActivity.updateHabitsCursor();
                    mainActivity.mAdapter.notifyDataSetChanged();
                }
            });
            snackbar.show();
        }
        // EDIT the habit
        else{
            // VIBRATE
            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                //deprecated in API 26
                v.vibrate(100);
            }

            // ALERT DIALOG
            MainActivity mainActivity = (MainActivity) mContext;
            Button button = (Button) ((MyViewHolder) viewHolder).itemView;
            mainActivity.buildAlertDialog(button.getText().toString());
        }
    }


}
