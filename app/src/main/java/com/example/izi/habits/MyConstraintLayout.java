package com.example.izi.habits;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public class MyConstraintLayout extends ConstraintLayout {
    public int internalPosition;
    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
