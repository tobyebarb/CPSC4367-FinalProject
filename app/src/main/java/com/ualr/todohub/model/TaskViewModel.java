package com.ualr.todohub.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ualr.todohub.db.TaskContract;
import com.ualr.todohub.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<List<Task>> taskList;
    private TaskDbHelper mHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private int selectedIndex;
    private String TAG = TaskViewModel.class.getSimpleName();

    public TaskViewModel() {
        taskList = new MutableLiveData<>(new ArrayList<>());
        db = mHelper.getWritableDatabase();
        values = new ContentValues();
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

    public SQLiteDatabase getDb() { return db;}

    public void updateUI() {
        List<Task> taskList = new ArrayList<>();
        db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TaskContract.TaskEntry.TABLE, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DESC));
                int day = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_DAY));
                int month = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_YEAR));

                Task obj = new Task();
                obj.setTitle(title);
                obj.setDescription(desc);
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,day);
                obj.setDueDate(cal);

                taskList.add(obj);
                cursor.moveToNext();
            }
        }
        this.setTaskList(taskList);
    }

    public void toggleItem(int position) {
        List<Task> currentTaskList = this.taskList.getValue();
        currentTaskList.get(position).toggleCompleted();
        this.taskList.setValue(currentTaskList);
    }
}