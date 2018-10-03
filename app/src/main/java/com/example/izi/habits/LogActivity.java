package com.example.izi.habits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    LineChart lineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        lineChart = findViewById(R.id.chart);
        lineChart.setScaleXEnabled(true);// todo check pinching on Huawei - emulator it doesnt work. must make sure x axis scale in and out
        lineChart.setScaleYEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(101,2));
        entries.add(new Entry(102,5));
        entries.add(new Entry(103,3));
        entries.add(new Entry(104,1));
        entries.add(new Entry(105,3));
        entries.add(new Entry(106,1));
        entries.add(new Entry(107,3));
        entries.add(new Entry(108,1));
        entries.add(new Entry(109,3));

        LineDataSet dataSet = new LineDataSet(entries, "Label");
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

        YAxis yAxis_right = lineChart.getAxisRight();
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = lineChart.getAxisLeft();
        yAxis_left.setDrawGridLines(false);
        yAxis_left.setGranularity(1.0f);

        lineChart.invalidate();

        lineChart.setVisibleXRangeMaximum(5.0f);

    }
}
