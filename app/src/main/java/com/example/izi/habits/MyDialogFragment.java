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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MyDialogFragment extends DialogFragment {

    UpdateEditedHabitInterface mUpdateEditedHabitInterface;
    public interface UpdateEditedHabitInterface{
        void updateEditedHabit(String from, String to);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);;
    }

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("EDIT HABIT");

        final EditText editText = new EditText(getActivity());
        final String origionallySwipedHabit = getArguments().getString("habit");
        editText.setText(origionallySwipedHabit);
        editText.setSelection(editText.getText().length());
        editText.requestFocus();
        builder.setView(editText);

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editedHabit = editText.getText().toString();
                mUpdateEditedHabitInterface.updateEditedHabit(origionallySwipedHabit, editedHabit);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            // i can get a class cast exception
            mUpdateEditedHabitInterface = (UpdateEditedHabitInterface)getActivity();
        }
        catch (ClassCastException e){
            Log.e("XXXXX", e.getMessage() );
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        InputMethodManager imm =
                (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        super.onDismiss(dialog);
    }
}
