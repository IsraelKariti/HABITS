package com.example.izi.habits;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import static com.example.izi.habits.MyContract.LogTable.LOG_COLUMN_HABIT;
import static com.example.izi.habits.MyContract.LogTable.LOG_TABLE_NAME;

public class LogActivity extends AppCompatActivity {
    String mHabitString;

    LineChart lineChart;
    SQL mSQL;
    SQLiteDatabase mDB;
    Cursor mCursor;

    LineData data_count;
    LineData data_duration;

    boolean dataIsCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chart);
        lineChart = findViewById(R.id.chart);
        lineChart.setScaleXEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setExtraOffsets(10, 0, 0, 10);

        // style Axises
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new MyValueFormatter());

        YAxis yAxis_right = lineChart.getAxisRight();
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = lineChart.getAxisLeft();
        yAxis_left.setDrawGridLines(false);
        yAxis_left.setGranularity(1.0f);

        // get the habit that suppose to be shown
        Intent intent = getIntent();
        mHabitString = intent.getStringExtra("habit");

        // get cursor containint all logs
        mSQL = new SQL(this, null, null, 1);
        mDB = mSQL.getReadableDatabase();
        mCursor = mDB.query(LOG_TABLE_NAME, new String[]{"*"}, LOG_COLUMN_HABIT+"=?", new String[]{mHabitString}, null, null, null);

        mCursor.moveToFirst();
        // create the list
        List<Entry> entries_by_count = new ArrayList<Entry>();
        while(!mCursor.isAfterLast()){
            int x = mCursor.getInt(2);
            int y = mCursor.getInt(6);
            entries_by_count.add(new Entry(x,y));
            mCursor.moveToNext();
        }

        mCursor.moveToFirst();
        List<Entry> entries_by_duration = new ArrayList<Entry>();
        while(!mCursor.isAfterLast()){
            int x = mCursor.getInt(2);
            int y = mCursor.getInt(8);
            entries_by_duration.add(new Entry(x,y));
            mCursor.moveToNext();
        }

        // style the dataset
        LineDataSet dataSet = new LineDataSet(entries_by_count, "Label");
        dataSet.setLineWidth(7);
        dataSet.setDrawValues(false);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(5f);
        dataSet.setCircleColorHole(Color.WHITE);
        dataSet.setCircleHoleRadius(2f);
        data_count = new LineData(dataSet);

        // style dataset - durationset
        LineDataSet dataSet_duration = new LineDataSet(entries_by_duration, "Label");
        dataSet_duration.setLineWidth(7);
        dataSet_duration.setDrawValues(false);
        dataSet_duration.setDrawHighlightIndicators(false);
        dataSet_duration.setCircleColor(Color.BLUE);
        dataSet_duration.setCircleRadius(5f);
        dataSet_duration.setCircleColorHole(Color.WHITE);
        dataSet_duration.setCircleHoleRadius(2f);
        data_duration = new LineData(dataSet_duration);

        lineChart.setData(data_count);
        dataIsCount = true;
        lineChart.invalidate();

        // ranges MUST be declared AFTER INVALIDATE
        lineChart.setVisibleXRangeMinimum(3f);
        lineChart.setVisibleXRangeMaximum(15f);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(mHabitString + " count");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(dataIsCount){
            lineChart.setData(data_duration);
            lineChart.invalidate();
            lineChart.setVisibleXRangeMaximum(5.0f);
            dataIsCount = false;
            setTitle(mHabitString + " duration");
        }
        else{
            lineChart.setData(data_count);
            lineChart.invalidate();
            lineChart.setVisibleXRangeMaximum(5.0f);
            dataIsCount = true;
            setTitle(mHabitString + " count");
        }
        return super.onOptionsItemSelected(item);
    }
}
