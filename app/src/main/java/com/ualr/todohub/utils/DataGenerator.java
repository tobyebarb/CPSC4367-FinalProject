package com.ualr.todohub.utils;
import android.content.Context;

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
            Task obj = new Task();
            obj.setTitle(title_arr[randInt(2)]);
            obj.setDescription(desc_arr[randInt(2)]);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,randInt(11),randInt(29));
            obj.setDueDate(cal);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }
}