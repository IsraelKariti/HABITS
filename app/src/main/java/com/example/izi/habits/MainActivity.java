package com.example.izi.habits;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.MissingResourceException;
import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_DURATION;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_MONTH;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_NOTE_COUNT;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_STARTING_TIME;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_TOTAL_DAY;
import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_YEAR;
import static com.example.izi.habits.MyContract.LogTable.LOG_TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable.COLUMN_HABIT_NAME;
import static com.example.izi.habits.MyContract.MainTable.COLUMN_HAS_NOTES;
import static com.example.izi.habits.MyContract.MainTable.TABLE_NAME;
import static com.example.izi.habits.MyContract.MainTable._ID;
// todo add indication that not has been taken
public class MainActivity extends AppCompatActivity implements MyDialogFragment.UpdateEditedHabitInterface, MyDialogFragment_Delete.DeleteHabitInterface {
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
    boolean refocusEditText;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
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
        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // when recyclerview is loosing focus close expanded habit
                if(!hasFocus) {
                    if(mAdapter.expandedIndex != -1){
                        MyViewHolder holder = (MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mAdapter.expandedIndex);
                        holder.buttonNotify.setVisibility(View.GONE);
                        holder.buttonEdit.setVisibility(View.GONE);
                        holder.buttonDelete.setVisibility(View.GONE);
                        holder.buttonHistory.setVisibility(View.GONE);
                    }
                }
            }
        });

        // for the Adapter
        mDB = mSQL.getReadableDatabase();
        mCursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter = new MyAdapter(this, mCursor);
        mRecyclerView.setAdapter(mAdapter);

        // for the DRAG
        simpleCallback = new MyItemTouchCallback(this, ItemTouchHelper.UP | ItemTouchHelper.DOWN , 0);

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        fragmentManager = getSupportFragmentManager();

        refocusEditText = false;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // call the service that creates new logs (with count of zero) everyday at midnight for each habits
        Intent intentService = new Intent(this, MyService.class);
        if (!isMyServiceRunning(MyService.class)) {
            startService(intentService);

        }

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

        // insert this new habit into HABITS table
        mDB = mSQL.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HABIT_NAME, habit);
        cv.put(COLUMN_HAS_NOTES, 0);
        mDB.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        // clean edittext
        mEditText.setText("");

        updateHabitsCursor();
        Toast.makeText(MainActivity.this, "Habit was added", Toast.LENGTH_SHORT).show();
        mRecyclerView.requestFocus();

        // get the current total day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // change 0-based month 1-based month
        int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayInYear = calendar.get(Calendar.DAY_OF_YEAR);
        int totalDay = daysInYears(year-1) + dayInYear; // calculate what day is it (on calendar) when Jan 1st 0001 is the first day of all time

        // insert this new habit into LOG table
        mDB = mSQL.getWritableDatabase();
        ContentValues cv_new = new ContentValues();
        cv_new.put(LOG_COLUMN_HABIT, habit);
        cv_new.put(LOG_COLUMN_TOTAL_DAY, totalDay);
        cv_new.put(LOG_COLUMN_YEAR, year);
        cv_new.put(LOG_COLUMN_MONTH, month);
        cv_new.put(LOG_COLUMN_DAY, dayInMonth);
        cv_new.put(LOG_COLUMN_NOTE_COUNT, 0);
        cv_new.put(LOG_COLUMN_STARTING_TIME, -1);
        cv_new.put(LOG_COLUMN_DURATION, 0);
        mDB.insert(LOG_TABLE_NAME, null, cv_new);

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

    @Override
    public void updateEditedHabit(String from, String to) {

        if(from.equals(to)){
            mAdapter.notifyDataSetChanged();
            return;
        }

        // close expanded habit
        mAdapter.closeExpandedHabit();

        // update edited habit in HABITS table
        mDB = mSQL.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HABIT_NAME, to);
        mDB.update(TABLE_NAME, cv, COLUMN_HABIT_NAME+"=?", new String[]{from});
        updateHabitsCursor();
        mAdapter.notifyDataSetChanged();


        // update edited habit also in LOG table
        ContentValues cv_log = new ContentValues();
        cv_log.put(LOG_COLUMN_HABIT, to);
        mDB.update(LOG_TABLE_NAME, cv_log, LOG_COLUMN_HABIT+"=?", new String[]{from});

    }

    public int getUpdatedPosition(String habit){
        mDB = mSQL.getReadableDatabase();
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{_ID}, COLUMN_HABIT_NAME+"=?", new String[]{habit}, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0)-1;
    }

    public void delete(View view){
        // get the habit as a string
        MyConstraintLayout myConstraintLayout = (MyConstraintLayout) view.getParent();
        TextView textView = myConstraintLayout.findViewById(R.id.habit);
        String string = textView.getText().toString();

        // create dialog for delete
        MyDialogFragment_Delete myDialogFragment_delete = MyDialogFragment_Delete.getInstance(string);
        myDialogFragment_delete.show(fragmentManager, "deleteTag");

    }

    public void deleteHabit(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        mDB = mSQL.getReadableDatabase();

        // keep index of deleted habit
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{"*"}, COLUMN_HABIT_NAME+"=?", new String[]{string}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getInt(0);
        String indexString = String.valueOf(index);

        // delete habit from HABITS table
        mDB = mSQL.getWritableDatabase();
        mDB.delete(TABLE_NAME, COLUMN_HABIT_NAME+"=?", new String[]{string});

        //decrease ID by 1 for all items with ID higher than the deleted ID
        mDB.execSQL("update habits set _id = _id - 1 where _id > "+indexString);

        // get updated cursor
        mDB = mSQL.getReadableDatabase();
        cursor = mDB.query(TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        mAdapter.setCursor(cursor);
        mAdapter.notifyItemRemoved(index-1);

        // delete from LOG table
        mDB = mSQL.getWritableDatabase();
        mDB.delete(LOG_TABLE_NAME, LOG_COLUMN_HABIT+"=?", new String[]{string});

        // remove item from indexExpanded
        mAdapter.expandedIndex = -1;
    }


    public void edit(View view){
        MyConstraintLayout myConstraintLayout = (MyConstraintLayout) view.getParent();
        TextView tv = myConstraintLayout.findViewById(R.id.habit);
        String str =  tv.getText().toString();
        myDialogFragment = MyDialogFragment.getInstance(str);
        myDialogFragment.show(fragmentManager, "abcd");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void note(View view){
        final Button btn = (Button) view;
        Toast.makeText(this, "Note taken", Toast.LENGTH_SHORT).show();
        btn.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(new Runnable() {
            @Override
            public void run() {
                btn.animate().scaleX(1f).scaleY(1f).setDuration(150);
            }
        });

        // get the pressed habit
        MyConstraintLayout myConstraintLayout = (MyConstraintLayout) view.getParent();
        TextView tv = myConstraintLayout.findViewById(R.id.habit);
        String habitString = tv.getText().toString();

        // enable view_history button
        Button viewHistory = myConstraintLayout.findViewById(R.id.btnHistory);
        if(viewHistory.isEnabled() == false){
            viewHistory.setEnabled(true);
            viewHistory.setBackgroundResource(R.drawable.ic_view_history_black_24dp);

            // update HABITS table that this habit was documented and has viewable history
            mDB = mSQL.getWritableDatabase();
            ContentValues cv_hasNotes = new ContentValues();
            cv_hasNotes.put(COLUMN_HAS_NOTES, 1);
            mDB.update(TABLE_NAME, cv_hasNotes, COLUMN_HABIT_NAME+"=?", new String[]{habitString});
            updateHabitsCursor();
        }


        // get the current total day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // change 0-based month 1-based month
        int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayInYear = calendar.get(Calendar.DAY_OF_YEAR);

        int totalDay = daysInYears(year-1) + dayInYear; // calculate what day is it (on calendar) when Jan 1st 0001 is the first day of all time
        String totalDayString = String.valueOf(totalDay);

        // get the minute
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int totalMinute = hour*60+minute;

        // check if this button was clicked today already
        mDB = mSQL.getReadableDatabase();
        Cursor cursor = mDB.query(LOG_TABLE_NAME, new String[]{"*"}, LOG_COLUMN_HABIT+"=? AND "+LOG_COLUMN_TOTAL_DAY+"=?", new String[]{habitString, totalDayString}, null, null, null);
        cursor.moveToFirst();
        Log.i("XXXX", ""+cursor.getCount());
        if(cursor.getInt(7) == -1){
            // this is the first entry for this day
            mDB = mSQL.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(LOG_COLUMN_NOTE_COUNT, 1);
            cv.put(LOG_COLUMN_STARTING_TIME, totalMinute);
            cv.put(LOG_COLUMN_DURATION, 0  );
            mDB.update(LOG_TABLE_NAME, cv, LOG_COLUMN_HABIT+"=?", new String[]{habitString});

        }
        else{
            cursor.moveToFirst();
            int noteCount = cursor.getInt(6);

            int updateNoteCount = ++noteCount;

            int startTime = cursor.getInt(7);
            int updatedDuration = totalMinute - startTime;
            mDB = mSQL.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(LOG_COLUMN_NOTE_COUNT, updateNoteCount);
            cv.put(LOG_COLUMN_DURATION, updatedDuration);
            mDB.updateWithOnConflict(LOG_TABLE_NAME, cv,LOG_COLUMN_HABIT+"=? AND "+LOG_COLUMN_TOTAL_DAY+"=?", new String[]{habitString, totalDayString}, SQLiteDatabase.CONFLICT_REPLACE );
        }
    }

    private int daysInYears(int years) {
        int incubatedYRS4 = years / 4;
        int incubatedYRS100 = years / 100;
        int incubatedYRS400 = years / 400;
        int incubatedYRS = incubatedYRS4 - incubatedYRS100 + incubatedYRS400;
        int normalYRS = years - incubatedYRS;
        int res = normalYRS*365 + incubatedYRS*366;
        return res;
    }


    public void history(View view){

        mAdapter.closeExpandedHabit();

        // get the pressed habit
        MyConstraintLayout myConstraintLayout = (MyConstraintLayout) view.getParent();
        TextView tv = myConstraintLayout.findViewById(R.id.habit);
        String habitString = tv.getText().toString();

        // start activity
        Intent intent = new Intent(this, LogActivity.class);
        intent.putExtra("habit", habitString);
        intent.putExtra("activity", MainActivity.class);
        startActivity(intent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
}