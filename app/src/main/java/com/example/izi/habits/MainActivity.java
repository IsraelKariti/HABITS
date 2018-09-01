package com.example.izi.habits;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.izi.habits.MyContract.MainTable.COLUMN_HABIT_NAME;
import static com.example.izi.habits.MyContract.MainTable.TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable._ID;

public class MainActivity extends AppCompatActivity {
    SQL mSQL;
    SQLiteDatabase mDB;
    Cursor mCursor;
    EditText mEditText;
    Button mPlusButton;
    RecyclerView mRecyclerView;
    MyAdapter mAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSQL = new SQL(this, null, null, 1);

        mEditText = getEditText();
        mPlusButton = getPlusButton();

        // set the RECYCLER VIEW
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // choose linearlayout and not gridlayout

        // for the Adapter
        mDB = mSQL.getReadableDatabase();
        mCursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter = new MyAdapter(mCursor);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        // for the SWIPE
        simpleCallback = getSimpleCallback();
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private Button getPlusButton() {
        Button btn = findViewById(R.id.button);
        btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    add_habit(view);
                }
            }
        });

        return btn;
    }

    private EditText getEditText() {
        EditText editText = findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    closeSoftKeyboard();
                }
            }
        });
        return editText;
    }

    @NonNull
    private ItemTouchHelper.SimpleCallback getSimpleCallback() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                // get the habit id
                int id = (int) viewHolder.getItemId();

                if(direction == ItemTouchHelper.RIGHT){
                    // look for the text in the database and delete it
                    mDB = mSQL.getWritableDatabase();
                    mDB.delete(TABLE_NAME, _ID+"=?", new String[]{String.valueOf(id)});
                    updateHabitsCursor();
                    mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                ConstraintLayout layout = (ConstraintLayout) viewHolder.itemView;
                float y = layout.getY();
                float height = layout.getHeight();

                //draw red background
                Rect rect = new Rect(0, (int)y, (int)dX, (int)(y+height) );
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#ffD32F2F"));
                c.drawRect(rect, paint);

                // draw trashcan icon
                VectorDrawable vd = (VectorDrawable) ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_delete_white_24dp);
                float vd_width = vd.getIntrinsicWidth();
                float vd_height = vd.getIntrinsicHeight();
                float scale = height / vd_height;


                if(dX < vd_width*scale)
                   vd.setBounds(0,(int)y, (int)dX, (int)(y+height));
                else
                    vd.setBounds(0, (int)y, (int)(vd_width*scale),(int)(y+height));
                vd.draw(c);
            }
        };
    }

    public void add_habit(View view){

        String habit = mEditText.getText().toString().trim();

        if(habit.matches("")){
            Toast.makeText(MainActivity.this, "Write a habit, don't be shy", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            mDB = mSQL.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_HABIT_NAME, habit);
            mDB.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            mEditText.setText("");
            updateHabitsCursor();
            Toast.makeText(MainActivity.this, "Habit was added", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHabitsCursor(){
        mCursor.close();
        mCursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter.setCursor(mCursor);
    }

    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
