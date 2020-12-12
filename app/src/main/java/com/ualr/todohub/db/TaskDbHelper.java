package com.ualr.todohub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper{

    public TaskDbHelper(Context context){
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_TASK_DESC + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_COMPLETED + " BOOLEAN,  " +
                TaskContract.TaskEntry.COL_PARENT_ID + " INTEGER REFERENCES " + TaskContract.TaskEntry.TABLE + ", " +
                TaskContract.TaskEntry.COL_HOUR + " INTEGER, " +
                TaskContract.TaskEntry.COL_MIN + " INTEGER, " +
                TaskContract.TaskEntry.COL_DAY + " INTEGER, " +
                TaskContract.TaskEntry.COL_MONTH + " INTEGER, " +
                TaskContract.TaskEntry.COL_YEAR + " INTEGER);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
