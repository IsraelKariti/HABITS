package com.example.izi.habits;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.izi.habits.MyContract.MainTable.COLUMN_HABIT_NAME;
import static com.example.izi.habits.MyContract.MainTable.TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable._ID;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.UpdateEditedHabitInterface, MyDialogFragment.DeleteHabitInterface {
    SQL mSQL;
    SQLiteDatabase mDB;
    Cursor mCursor;
    EditText mEditText;
    Button mPlusButton;
    RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback;
    ItemTouchHelper itemTouchHelper;
    MyDialogFragment myDialogFragment;
    FragmentManager fragmentManager;

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
        mAdapter = new MyAdapter(this, mCursor);
        mRecyclerView.setAdapter(mAdapter);

        // for the SWIPE
        simpleCallback = new MyItemTouchCallback(this, ItemTouchHelper.UP | ItemTouchHelper.DOWN , 0);

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        fragmentManager = getSupportFragmentManager();
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
        Button btn = findViewById(R.id.btnNotify);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_habit(view);
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
        closeSoftKeyboard();
        mDB = mSQL.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HABIT_NAME, habit);
        mDB.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        mEditText.setText("");
        updateHabitsCursor();
        Toast.makeText(MainActivity.this, "Habit was added", Toast.LENGTH_SHORT).show();
        mRecyclerView.requestFocus();
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

    public void startAlertDialog(String str){
        myDialogFragment = MyDialogFragment.getInstance(str);
        myDialogFragment.show(fragmentManager, "abcd");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void updateEditedHabit(String from, String to) {

        if(from.equals(to)){
            mAdapter.notifyDataSetChanged();
            return;
        }
        mDB = mSQL.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HABIT_NAME, to);
        mDB.update(TABLE_NAME, cv, COLUMN_HABIT_NAME+"=?", new String[]{from});
        updateHabitsCursor();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteHabit(String habit) {
        mDB = mSQL.getWritableDatabase();
        mDB.delete(TABLE_NAME, COLUMN_HABIT_NAME+"=?", new String[]{habit});
        updateHabitsCursor();
        mAdapter.notifyDataSetChanged();
    }

    public int getUpdatedPosition(String habit){
        mDB = mSQL.getReadableDatabase();
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{_ID}, COLUMN_HABIT_NAME+"=?", new String[]{habit}, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0)-1;
    }

    public void updateCursor(){
        Cursor updatedCursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter.setCursor(updatedCursor);
    }

    public void delete(View view){
        MyConstraintLayout myConstraintLayout = (MyConstraintLayout) view.getParent();
        TextView textView = myConstraintLayout.findViewById(R.id.habit);
        String string = textView.getText().toString();
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        mDB = mSQL.getReadableDatabase();
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{"*"}, COLUMN_HABIT_NAME+"=?", new String[]{string}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getInt(0);
        String indexString = String.valueOf(index);
        mDB = mSQL.getWritableDatabase();
        mDB.delete(TABLE_NAME, COLUMN_HABIT_NAME+"=?", new String[]{string});

        //decrease ID by 1 for all items with ID higher than the deleted ID
        mDB.execSQL("update habits set _id = _id - 1 where _id > "+indexString);

        mDB = mSQL.getReadableDatabase();
        cursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter.setCursor(cursor);
        mAdapter.notifyItemRemoved(index-1);

    }
}