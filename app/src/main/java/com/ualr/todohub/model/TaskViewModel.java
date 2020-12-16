package com.ualr.todohub.model;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ualr.todohub.MainActivity;
import com.ualr.todohub.database.DataBaseHelper;
import com.ualr.todohub.fragments.TaskListFragment;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<List<Task>> taskList;
    private int selectedIndex;
    public DataBaseHelper dataBaseHelper;
    private String TAG = TaskViewModel.class.getSimpleName();

    public TaskViewModel() {
        taskList = new MutableLiveData<>(new ArrayList<>());
        selectedIndex = 0;
    }

    public LiveData<List<Task>> getTaskList() {
        if (taskList == null) {
            taskList = new MutableLiveData<>();
            Log.d(TAG, "here");
        }
        return taskList;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int position) {
        this.selectedIndex = position;
    }

    public void setTaskList(List<Task> list) {
        this.taskList.setValue(list);
    }

    public void toggleItem(int position) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(TaskListFragment.ctx);
        List<Task> currentTaskList = this.taskList.getValue();
        Task task = currentTaskList.get(position);
        currentTaskList = dataBaseHelper.getAll();
        //currentTaskList.get(position).toggleCompleted();
        Log.d(TAG, task.toString());
        dataBaseHelper.toggleItem(task);
        currentTaskList = dataBaseHelper.getAll();
        this.taskList.setValue(currentTaskList);
    }
}