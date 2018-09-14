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
    String mHabit;
    public MyDialogFragment(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("XXXXXXXX","ENTER ONCREATEDIALOG");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("MyTitle");
        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss();
                Log.i("ZZZZZZZ","one");
            }
        });

        // use builder.setView() to set a custom layout with edittext

        return builder.create();
    }
}
