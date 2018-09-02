package com.example.izi.habits;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public Button mButtonHabit;
    public int position;

    public MyViewHolder(Context context, Button btn) {
        super(btn);
        mContext = context;
        mButtonHabit = createButtonHabit(btn);
    }

    // create the button which holds the habit and set its functionality
    private Button createButtonHabit(Button btn) {
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // VIBRATE
                Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    //deprecated in API 26
                    v.vibrate(100);
                }

                // ALERTDIALOG
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.buildAlertDialog(mButtonHabit.getText().toString());
                return true;
            }
        });
        return btn;
    }
}
