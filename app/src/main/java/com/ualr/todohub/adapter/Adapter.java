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
import com.ualr.todohub.fragments.TaskListFragment;
import com.ualr.todohub.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    //private List<Task> uncompletedTasks;
    //private List<Task> completedTasks;
    private List<Task> allTasks;
    private Context mContext;
    private static final String TAG = MainActivity.class.getSimpleName();
    public int position;
    public int selector;
    private static final int COMPLETED = 0;
    private static final int UNCOMPLETED = 1;

    public interface OnItemClickListener {
        void onItemClick(View v, Task obj, int position);
    }

    public OnItemClickListener mListener;

    public Adapter(Context context, List<Task> allTasks, int selector) {
        this.mContext = context;
        this.allTasks = allTasks;
        this.selector = selector;

    }
    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public void removeItem(int position) { // 0: uncompleted, 1: completed, 2: all
        if (position >= allTasks.size()) {
            return;
        }
        Log.d(TAG, allTasks.toString());
        Log.d(TAG, String.valueOf(allTasks.size()));
        allTasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        return this.allTasks.get(position).isCompleted()? COMPLETED : UNCOMPLETED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View itemView = null;

        switch (viewType) {
            case (COMPLETED) :
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
                vh = new TaskViewHolder(itemView);
                break;
            case (UNCOMPLETED):
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
                vh = new TaskViewHolder(itemView);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int index) {
        Task i = allTasks.get(index);
        switch (selector){
            case 0:
                if(!i.isCompleted()) {
                    TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
                    taskViewHolder.title.setText(i.getTitle() + " ID: " + i.getId());
                    taskViewHolder.due_date.setText(i.getDueDateString());
                }
                break;
            case 1:
                if(i.isCompleted()) {
                    TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
                    taskViewHolder.title.setText(i.getTitle() + " ID: " + i.getId());
                    taskViewHolder.due_date.setText(i.getDueDateString());
                }
                break;
            default:
                TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
                taskViewHolder.title.setText(i.getTitle() + " ID: " + i.getId());
                taskViewHolder.due_date.setText(i.getDueDateString());
                break;
        }
    }

    /*public void deleteUpdate(List<Task> tasks) {
        removeItem();
        this.uncompletedTasks = tasks;
    }*/

    public void updateItems(List<Task> tasks) {
        this.allTasks = tasks;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.allTasks.size();
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
                    mListener.onItemClick(v, allTasks.get(position), position);
                    TaskListFragment listFragment = new TaskListFragment();
                    listFragment.toggleCompleted(position);
                    Log.d(TAG, "TASK ID: " + allTasks.get(position).getId() + " was clicked.");
                    if(allTasks.get(position).isCompleted()) Log.d(TAG, "TASK ID: " + allTasks.get(position).getId() + " is completed.");
                }
            });

        }
    }
}
