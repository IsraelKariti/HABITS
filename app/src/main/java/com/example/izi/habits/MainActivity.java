package com.example.izi.habits;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.izi.habits.MyContract.LogTable.LOG_TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable.COLUMN_HABIT_NAME;
import static com.example.izi.habits.MyContract.MainTable.TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable._ID;
// TODO change swipe to delete to alertdialog to confirm delete
//TODO when delete also delete from LOG TABLE
public class MainActivity extends AppCompatActivity {
    public CoordinatorLayout mCoordinatorLayout;
    SQL mSQL;
    SQLiteDatabase mDB;
    Cursor mCursor;
    EditText mEditText;
    Button mPlusButton;
    RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback;
    ItemTouchHelper itemTouchHelper;
    AlertDialog.Builder mAlertDialogBuilder;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoordinatorLayout = findViewById(R.id.coordinator);

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
        mAdapter = new MyAdapter(this, mCursor);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        // for the SWIPE
        simpleCallback = new MyItemTouchCallback(this, ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // init Alert Dialog
        mAlertDialogBuilder = new AlertDialog.Builder(this);
        mAlertDialogBuilder.setTitle("Edit Habit");
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

    public void add_habit(View view){

        String habit = mEditText.getText().toString().trim();
        // check if user is trying to add an empty habit
        if(habit.matches("")){
            Toast.makeText(MainActivity.this, "Write a habit, don't be shy", Toast.LENGTH_SHORT).show();
            return;
        }
        // check if user entered a habit which already exist in database
        mDB = mSQL.getReadableDatabase();
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{"*"}, COLUMN_HABIT_NAME+"=?", new String[]{habit}, null, null, null );
        if(cursor.getCount() != 0){
            Toast.makeText(MainActivity.this, "Habit already exist", Toast.LENGTH_SHORT).show();
            return;
        }
        mDB = mSQL.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HABIT_NAME, habit);
        mDB.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        mEditText.setText("");
        updateHabitsCursor();
        Toast.makeText(MainActivity.this, "Habit was added", Toast.LENGTH_SHORT).show();
    }

    public void buildAlertDialog(final String habit){
        final EditText editText = (EditText) LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        editText.setText(habit);
        editText.setSelection(editText.getText().length());
        mAlertDialogBuilder.setView(editText);
        mAlertDialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDB = mSQL.getWritableDatabase();
                ContentValues cv = new ContentValues();
                // if user left edittext empty restore original habit
                if (editText.getText().toString().isEmpty()) {
                    // doSomething
                    editText.setText(habit);
                }
                cv.put(COLUMN_HABIT_NAME, editText.getText().toString());
                mDB.update(TABLE_NAME, cv, COLUMN_HABIT_NAME+"=?", new String[]{habit});
                updateHabitsCursor();
                //mAdapter.notifyDataSetChanged(); // i can't understand why this line doesnt cause recycler view to redraw
                mRecyclerView.setAdapter(mAdapter);// use this line instead
                dialogInterface.dismiss();
            }
        });
        mAlertDialog = mAlertDialogBuilder.create();
        mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mAlertDialog.show();
    }

    public void updateHabitsCursor(){
        mCursor.close();
        mCursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter.setCursor(mCursor);
    }

    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public void newLog(ContentValues cv){
        mDB = mSQL.getWritableDatabase();
        mDB.insertWithOnConflict(LOG_TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }
}