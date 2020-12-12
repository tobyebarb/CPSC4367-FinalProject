package com.ualr.todohub.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.ualr.todohub.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESC = "description";
        public static final String COL_COMPLETED = "completed";
        public static final String COL_PARENT_ID = "parentID";
        public static final String COL_HOUR = "hour";
        public static final String COL_MIN = "minute";
        public static final String COL_DAY = "day";
        public static final String COL_MONTH = "month";
        public static final String COL_YEAR = "year";
    }
}
