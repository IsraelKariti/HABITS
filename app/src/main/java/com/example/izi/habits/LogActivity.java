package com.example.izi.habits;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
    LineChart lineChart;
    SQL mSQL;
    SQLiteDatabase mDB;
    Cursor mCursor;

    String dayName;
    String monthName;
    String yearName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chart);
        lineChart = findViewById(R.id.chart);
        lineChart.setScaleXEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        // get the habit that suppose to be shown
        Intent intent = getIntent();
        String habitString = intent.getStringExtra("habit");

        // get cursor containint all logs
        mSQL = new SQL(this, null, null, 1);
        mDB = mSQL.getReadableDatabase();
        mCursor = mDB.query(LOG_TABLE_NAME, new String[]{"*"}, LOG_COLUMN_HABIT+"=?", new String[]{habitString}, null, null, null);

        mCursor.moveToFirst();
        Log.i("XXXX", "MOVE TO FIRST");

        // create the list
        List<Entry> entries_by_count = new ArrayList<Entry>();
        while(mCursor.isAfterLast() == false){
            int x = mCursor.getInt(2);
//            int year = mCursor.getInt(3);
//            int month = mCursor.getInt(4);
//            int day = mCursor.getInt(5);
            int y = mCursor.getInt(6);
            entries_by_count.add(new Entry(x,y));
            mCursor.moveToNext();
        }

        // style the dataset
        LineDataSet dataSet = new LineDataSet(entries_by_count, "Label");
        dataSet.setLineWidth(5);
        dataSet.setDrawValues(false);
        dataSet.setDrawHighlightIndicators(false);
        LineData data = new LineData(dataSet);

        lineChart.setData(data);
        lineChart.setDragEnabled(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setValueFormatter(new MyValueFormatter());

        YAxis yAxis_right = lineChart.getAxisRight();
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = lineChart.getAxisLeft();
        yAxis_left.setDrawGridLines(false);
        yAxis_left.setGranularity(1.0f);

        lineChart.invalidate();

        lineChart.setVisibleXRangeMaximum(5.0f);
    }
}
