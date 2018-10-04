package com.example.izi.habits;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class MyDialogFragment_Delete extends DialogFragment {

    MyDialogFragment_Delete.DeleteHabitInterface mDeleteHabitInterface;

    public interface DeleteHabitInterface{
        void deleteHabit(String habit);
    }

    public static MyDialogFragment_Delete getInstance(String habit){
        MyDialogFragment_Delete myDialogFragment = new MyDialogFragment_Delete();
        Bundle args = new Bundle();
        args.putString("habit", habit);
        myDialogFragment.setArguments(args);
        return myDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");

        final TextView tv = new TextView(getActivity());
        final String origionalHabit = getArguments().getString("habit");
        tv.setText("Are you sure you want to delete "+ origionalHabit + " and all its history?\n(This cannot be undone)");

        builder.setView(tv, 50, 0, 50, 0);

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDeleteHabitInterface.deleteHabit(origionalHabit);
            }
        });
        builder.setNegativeButton("Do not delete the habit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            // i can get a class cast exception
            mDeleteHabitInterface = (MyDialogFragment_Delete.DeleteHabitInterface)getActivity();
        }
        catch (ClassCastException e){
            Log.e("XXXXX", e.getMessage() );
        }
    }
}
