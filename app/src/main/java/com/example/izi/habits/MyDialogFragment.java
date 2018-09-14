package com.example.izi.habits;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

public class MyDialogFragment extends DialogFragment {

    public static MyDialogFragment getInstance(String habit){
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("habit", habit);
        myDialogFragment.setArguments(args);
        return myDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("EDIT HABIT");
        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss();
                Log.i("ZZZZZZZ","one");
            }
        });
        EditText editText = new EditText(getActivity());
        editText.setText(getArguments().getString("habit"));
        // use builder.setView() to set a custom layout with edittext
        builder.setView(editText);
        return builder.create();
    }
}
