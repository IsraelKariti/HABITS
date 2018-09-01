package com.example.izi.habits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {


    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(0, 0, 150, 150);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(rect, paint);

        Rect rect2 = new Rect(100, 550, 200, 650);
        Paint paint2 = new Paint();
        canvas.drawRect(rect2, paint2);
    }
}
