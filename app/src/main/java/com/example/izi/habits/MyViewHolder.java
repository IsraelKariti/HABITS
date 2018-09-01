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

    Context mContext;
    public Button mHabit;

    public MyViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mHabit = getHabitButton((ConstraintLayout) itemView);
    }

    private Button getHabitButton(ConstraintLayout itemView) {
        Button btn = itemView.findViewById(R.id.habit);
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("ii", "LONKLIK");

                // VIBRATE
                Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(150,VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    //deprecated in API 26
                    v.vibrate(150);
                }
                Log.i("IZII", "VIBRATEEEEEEEEEEEEEEE");
                // ALERTDIALOG
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.buildAlertDialog(mHabit.getText().toString());
                return true;
            }
        });
        return btn;
    }
}
