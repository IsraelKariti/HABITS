package com.example.izi.habits;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TestActivity extends AppCompatActivity {
    ConstraintLayout layout;
    ConstraintLayout.LayoutParams params;
    ImageView imageView;
    View view;
    Canvas canvas;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;
    Rect rect;
    Paint paint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        layout = findViewById(R.id.layout);
        imageView = new ImageView(this);

        // CREATE A PAINTED BITMAP
        bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);//PIXELS
        canvas = new Canvas(bitmap);
        rect = new Rect(0, 0, 100, 400);//PIXELS
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        canvas.drawRect(rect, paint);

        imageView.setImageBitmap(bitmap);
        layout.addView(imageView);
    }
}
