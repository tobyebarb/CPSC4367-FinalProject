package com.ualr.todohub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ualr.todohub.MainActivity;
import com.ualr.todohub.model.Task;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String TAG = MainActivity.class.getSimpleName();
    private Context context;
    private int count;

    public static final String TASK_TABLE = "TASK_TABLE";
    public static final String COLUMN_TASK_TITLE = "TASK_TITLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TASK_PARENT_ID = "TASK_PARENT_" + COLUMN_ID;
    public static final String COLUMN_TASK_DESCRIPTION = "TASK_DESCRIPTION";
    public static final String COLUMN_TASK_DUE_MONTH = "TASK_DUE_MONTH";
    public static final String COLUMN_TASK_DUE_DAY = "TASK_DUE_DAY";
    public static final String COLUMN_TASK_DUE_YEAR = "TASK_DUE_YEAR";
    public static final String COLUMN_TASK_IS_COMPLETED = "TASK_IS_COMPLETED";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "task.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TASK_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_PARENT_ID + " INT, " +
                COLUMN_TASK_TITLE + " TEXT, " +
                COLUMN_TASK_DESCRIPTION + " TEXT," +
                COLUMN_TASK_DUE_MONTH + " INT," +
                COLUMN_TASK_DUE_DAY + " INT," +
                COLUMN_TASK_DUE_YEAR + " INT," +
                COLUMN_TASK_IS_COMPLETED + " BOOL)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_PARENT_ID, task.getParentID());
        cv.put(COLUMN_TASK_TITLE, task.getTitle());
        cv.put(COLUMN_TASK_DESCRIPTION, task.getDescription());
        cv.put(COLUMN_TASK_DUE_MONTH, task.getDueDate()[0]);
        cv.put(COLUMN_TASK_DUE_DAY, task.getDueDate()[1]);
        cv.put(COLUMN_TASK_DUE_YEAR, task.getDueDate()[2]);
        cv.put(COLUMN_TASK_IS_COMPLETED, task.isCompleted());

        long insert = db.insert(TASK_TABLE, null, cv);

        return (insert == -1) ? false : true;
    }

    public List<Task> getAll() {
        List<Task> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TASK_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // if results exist, loop through and put them into returnList
            do {
                int taskID = cursor.getInt(0);
                int taskParentID = cursor.getInt(1);
                String taskTitle = cursor.getString(2);
                String taskDescription = cursor.getString(3);
                int taskDueDateMonth = cursor.getInt(4);
                int taskDueDateDay = cursor.getInt(5);
                int taskDueDateYear = cursor.getInt(6);
                boolean taskIsCompleted = cursor.getInt(7) == 1 ? true : false;

                Task task = new Task(taskID, taskParentID, taskTitle, taskDescription, taskDueDateMonth, taskDueDateDay, taskDueDateYear, taskIsCompleted);
                returnList.add(task);

            } while (cursor.moveToNext());

        } else {
            // if there are no items, dont add anything to the list.
        }

        cursor.close();
        //db.close();

        return returnList;
    }

    public void toggleItem(Task task) {
        int taskID = task.getId();
        int isComplete = task.isCompleted() == true ? 1 : 0;
        int opposite = !task.isCompleted() == true ? 1 : 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TASK_TABLE + " SET " + COLUMN_TASK_IS_COMPLETED + " = '" + opposite + "' WHERE " + COLUMN_ID + " = '" + taskID + "'" + " AND " + COLUMN_TASK_IS_COMPLETED + " = '" + isComplete + "'";

        Log.d(TAG, "Task ID: " + taskID + " is set to: " + isComplete);
        Log.d(TAG, query);
        db.execSQL(query);

        db.close();

    }

    public void deleteItem(Task task) {
        int taskID = task.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TASK_TABLE + " WHERE " + COLUMN_ID + " = '" + taskID + "'";
        Log.d(TAG, "QUERY: " + query);
        db.execSQL(query);

        db.close();
    }

    public int getNextID() {
        int taskID=-1;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TASK_TABLE + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) taskID = cursor.getInt(0);

        return taskID+1;
    }
}
