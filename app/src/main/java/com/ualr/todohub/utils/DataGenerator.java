package com.ualr.todohub.utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ualr.todohub.MainActivity;
import com.ualr.todohub.db.TaskContract;
import com.ualr.todohub.db.TaskDbHelper;

import com.ualr.todohub.R;
import com.ualr.todohub.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    public static List<Task> getTaskData(Context ctx) {
        List<Task> items = new ArrayList<>();
        String title_arr[] = ctx.getResources().getStringArray(R.array.task_title);
        String desc_arr[] = ctx.getResources().getStringArray(R.array.task_description);

        for (int i = 0; i < 30; i++) {
            int month = randInt(11);
            int day = randInt(29);
            int year = 2020 + randInt(1);
            Task obj = new Task();
            obj.setTitle(title_arr[randInt(2)]);
            obj.setDescription(desc_arr[randInt(2)]);
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            obj.setDueDate(cal);

            items.add(obj);
        }

        Collections.shuffle(items);
        return items;
    }
}