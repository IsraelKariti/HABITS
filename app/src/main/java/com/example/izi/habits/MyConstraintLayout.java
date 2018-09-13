package com.example.izi.habits;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public class MyConstraintLayout extends ConstraintLayout {
    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int pureSize = MeasureSpec.getSize(widthMeasureSpec);
        int pureMode = MeasureSpec.getMode(widthMeasureSpec);
        super.onMeasure(pureMode+(int)(2*pureSize), heightMeasureSpec);
    }
}
