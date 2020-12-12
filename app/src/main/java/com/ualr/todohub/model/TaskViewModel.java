package com.ualr.todohub.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<List<Task>> taskList;
    private int selectedIndex;
    private String TAG = TaskViewModel.class.getSimpleName();

    public TaskViewModel() {
        taskList = new MutableLiveData<>();
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
}
