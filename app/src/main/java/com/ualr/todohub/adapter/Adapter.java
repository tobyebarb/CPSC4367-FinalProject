package com.ualr.todohub.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ualr.todohub.MainActivity;
import com.ualr.todohub.R;
import com.ualr.todohub.model.Task;

import java.util.Calendar;
import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private List<Task> mItems;
    private Context mContext;
    private static final String TAG = MainActivity.class.getSimpleName();
    public int position;

    public interface OnItemClickListener {
        void onItemClick(View v, Task obj, int position);
    }

    public OnItemClickListener mListener;

    public Adapter(Context context, List<Task> items) {
        this.mItems = items;
        this.mContext = context;
    }
    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public void removeItem(int position) {
        if (position >= mItems.size()) {
            return;
        }
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        vh = new TaskViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int index) {

        TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
        Task i = mItems.get(index);

        taskViewHolder.title.setText(i.getTitle());
    }

    public void updateItems(List<Task> tasks) {
        this.mItems = tasks;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        
        public ImageView checkmark;
        public TextView title;
        public TextView due_date;

        public TaskViewHolder(final View itemView) {
            super(itemView);
            checkmark = itemView.findViewById(R.id.checkmark);
            title = itemView.findViewById(R.id.title);
            due_date = itemView.findViewById(R.id.due_date);

            checkmark.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    position = getAbsoluteAdapterPosition();
                    mListener.onItemClick(v, mItems.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition());
                    mItems.get(getAbsoluteAdapterPosition()).setCompleted(true);
                    updateItems(mItems);
                    Log.d(TAG, "Item " + getAbsoluteAdapterPosition() + " was clicked.");
                    notifyDataSetChanged();
                }
            });

        }
    }
}
