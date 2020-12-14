package com.ualr.todohub.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ualr.todohub.MainActivity;
import com.ualr.todohub.R;
import com.ualr.todohub.fragments.TaskDialogFragment;
import com.ualr.todohub.fragments.TaskListFragment;
import com.ualr.todohub.model.Task;
import com.ualr.todohub.utils.DataGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class subtaskAdapter extends RecyclerView.Adapter {

    private List<Task> allTasks;
    private Task mainTask;
    private int id;
    private Context mContext;
    private static final String TAG = MainActivity.class.getSimpleName();
    public int position;
    private static final int COMPLETED = 0;
    private static final int UNCOMPLETED = 1;

    public interface OnItemClickListenerSub {
        void onItemClick(View v, Task obj, int position);
    }

    public OnItemClickListenerSub mListener;

    public subtaskAdapter(Context context, List<Task> allTasks, Task mainTask) {
        this.mContext = context;
        this.allTasks = allTasks;
        this.mainTask = mainTask;
        this.id = mainTask.getId();
    }

    public void setOnItemClickListenerSub(final OnItemClickListenerSub itemClickListener) {
        this.mListener = itemClickListener;
    }

    public void removeItem(int position) { // 0: uncompleted, 1: completed, 2: all
        if (position >= allTasks.size()) {
            return;
        }
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

        if(i.getParentID()==id && !i.isCompleted()) {
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            taskViewHolder.lyt_parent.setVisibility(View.VISIBLE);
            taskViewHolder.params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            taskViewHolder.title.setText(i.getTitle());
            taskViewHolder.due_date.setText(i.getDueDateString());

            if(i.getDueDateString().equalsIgnoreCase("Late")) taskViewHolder.due_date.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccentDark));
            else if(i.getDueDateString().equalsIgnoreCase("Today") || i.getDueDateString().equalsIgnoreCase("Tomorrow")) taskViewHolder.due_date.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccentGreen));
            else taskViewHolder.due_date.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccentGrey));
        } else {
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            taskViewHolder.lyt_parent.setVisibility(View.GONE);
            taskViewHolder.params.height = 0;
        }

    }

    public void updateItems(List<Task> tasks) {
        this.allTasks = tasks;
        sortItems(allTasks);
        notifyDataSetChanged();
    }

    public void sortItems(List<Task> tasks) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override public int compare(Task task1, Task task2) {
                if(task1.getDueDateDiff() == 0) Log.d(TAG, String.valueOf(task1.getDueDateDiff() - task2.getDueDateDiff()));
                return task1.getDueDateDiff() - task2.getDueDateDiff(); // Ascending
            }

        });
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
        public ImageView checkmark_green;
        public TextView title;
        public TextView due_date;
        public LinearLayout lyt_parent;
        public ViewGroup.LayoutParams params;

        public TaskViewHolder(final View itemView) {
            super(itemView);
            checkmark = itemView.findViewById(R.id.checkmark);
            title = itemView.findViewById(R.id.title);
            due_date = itemView.findViewById(R.id.due_date);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);
            checkmark_green = itemView.findViewById(R.id.checkmark_green);
            params = lyt_parent.getLayoutParams();

            checkmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAbsoluteAdapterPosition();
                    mListener.onItemClick(v, allTasks.get(position), position);
                    TaskListFragment listFragment = new TaskListFragment();
                    listFragment.toggleCompleted(position);
                    Log.d(TAG, "TASK ID: " + allTasks.get(position).getId() + " was clicked.");
                    if(allTasks.get(position).isCompleted()) Log.d(TAG, "TASK ID: " + allTasks.get(position).getId() + " is completed.");
                }
            });

            checkmark_green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAbsoluteAdapterPosition();
                    mListener.onItemClick(v, allTasks.get(position), position);
                    TaskListFragment listFragment = new TaskListFragment();
                    listFragment.toggleCompleted(position);
                    Log.d(TAG, "TASK ID: " + allTasks.get(position).getId() + " was clicked.");
                    if(allTasks.get(position).isCompleted()) Log.d(TAG, "TASK ID: " + allTasks.get(position).getId() + " is completed.");
                }
            });

            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked item...");
                    position = getAbsoluteAdapterPosition();
                    Log.d(TAG, "Clicked item " + position);
                    Log.d(TAG, "Clicked item " + allTasks.get(position).getTitle());
                    mListener.onItemClick(v, allTasks.get(position), position);
                }
            });

        }
    }
}