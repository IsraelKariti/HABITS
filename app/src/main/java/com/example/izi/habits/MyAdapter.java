package com.example.izi.habits;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context mContext;
    Cursor mCursor;
    int expandedIndex;
    public MyAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
        expandedIndex = -1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final MyConstraintLayout layout = (MyConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mContext, layout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final boolean isEpanded = position == expandedIndex;
        mCursor.moveToPosition(position);
        boolean hasNotes = mCursor.getInt(2) == 0 ? false : true;
        ((TextView)holder.habit).setText(mCursor.getString(1));
        holder.buttonNotify.setVisibility(isEpanded?View.VISIBLE:View.GONE);
        holder.buttonEdit.setVisibility(isEpanded?View.VISIBLE:View.GONE);
        holder.buttonDelete.setVisibility(isEpanded?View.VISIBLE:View.GONE);
        holder.buttonHistory.setVisibility(isEpanded?View.VISIBLE:View.GONE);

        // enable or disable view history button
        if(hasNotes) {
            holder.buttonHistory.setBackgroundResource(R.drawable.ic_view_history_black_24dp);
            holder.buttonHistory.setEnabled(true);
        }
        else{
            holder.buttonHistory.setBackgroundResource(R.drawable.ic_view_history_grey_24dp);
            holder.buttonHistory.setEnabled(false);
        }

        holder.mMyConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habit = ((TextView)((MyConstraintLayout)view).findViewById(R.id.habit)).getText().toString();
                int updatedPosition = ((MainActivity)mContext).getUpdatedPosition(habit);
                int preExpanded = expandedIndex;
                expandedIndex = isEpanded?-1:updatedPosition;

                notifyItemChanged(expandedIndex);
                if(preExpanded!=-1)
                    notifyItemChanged(preExpanded);
            }
        });
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.mRecyclerView.requestFocus();
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void setCursor(Cursor cursor){
        mCursor.close();
        mCursor = cursor;
    }

    public void updateExpandedIndex(int updatedIndex){
        this.expandedIndex = updatedIndex;
    }

    public int getExpandedIndex(){
        return expandedIndex;
    }

    public void closeExpandedHabit(){
        int temp = expandedIndex;
        expandedIndex = -1;
        if(temp != -1){
            Log.i("XXXX", "CLOSE EXPANDED INDEX: "+String.valueOf(temp));
            notifyItemChanged(temp);
        }
    }
}
