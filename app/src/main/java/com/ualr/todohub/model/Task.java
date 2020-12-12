package com.ualr.todohub.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Task {

    private static int count;
    private int id=0;
    private int parentID;
    private Calendar dueDate;
    private int[] timeDue;
    private String title;
    private String description;
    private boolean isCompleted;
    private List<Task> subtasks;

    public Task() {
        this.isCompleted = false;
        this.id = count++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int[] getDueDate() {
        int[] values = new int[3];
        values[0] = dueDate.get(Calendar.MONTH);
        values[1] = dueDate.get(Calendar.DAY_OF_MONTH);
        values[2] = dueDate.get(Calendar.YEAR);
        return values;
    }

    public String getDueDateString() {
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        String yearInString = String.valueOf(currYear);
        String dueDateString = "Something went wrong...";
        if (getCalendarDayOfYear() - currDayOfYear < 0) {
            dueDateString = "Late";
        }else if (dueDate.get(Calendar.DAY_OF_YEAR) - currDayOfYear == 0) {
            dueDateString = "Today";
        }else if (dueDate.get(Calendar.DAY_OF_YEAR) - currDayOfYear == 1){
            dueDateString = "Tomorrow";
        }else if (dueDate.get(Calendar.DAY_OF_YEAR) - currDayOfYear > 1 && dueDate.get(Calendar.DAY_OF_YEAR) - currDayOfYear < 7) {
            dueDateString = getDayOfWeekString();
        }else if (dueDate.get(Calendar.YEAR) - currYear == 0) {
            dueDateString = getMonthDayString();
        }else{
            dueDateString = getMonthDayYearString();
        }

        return dueDateString;
    }

    public int getCalendarDayOfYear() {
        String calendarDayOfYear="";
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int dayOfYear = dueDate.get(Calendar.DAY_OF_YEAR);
        final int days=365;
        int extraDays;
        // 2023 2021
        int yearDiff = dueDate.get(Calendar.YEAR) - currYear;
        if(yearDiff == 0) {
            return dayOfYear;
        } else if (yearDiff > 0) {
            extraDays=days*yearDiff;
            return dayOfYear+=extraDays;
        } else {
            return dayOfYear;
        }
    }

    public String getMonthDayString() {
        String month = "";
        String day = "";

        String monthDayString="";

        switch(dueDate.get(Calendar.MONTH)) {
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sept";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
                break;
        }
        day = String.valueOf(dueDate.get(Calendar.DAY_OF_MONTH));

        return month + " " + day;
    }

    public String getMonthDayYearString() {
        String monthDay = getMonthDayString();
        return monthDay + ", " + String.valueOf(dueDate.get(Calendar.YEAR));
    }

    public String getDayOfWeekString() {
        String day = "";
        switch(dueDate.get(Calendar.DAY_OF_WEEK)){
            case 1:
                day="Sunday";
                break;
            case 2:
                day="Monday";
                break;
            case 3:
                day="Tuesday";
                break;
            case 4:
                day="Wednesday";
                break;
            case 5:
                day="Thursday";
                break;
            case 6:
                day="Friday";
                break;
            case 7:
                day="Saturday";
                break;
        }
        return day;
    }

    public int getDayOfWeek(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public int[] getTimeDue() {
        return timeDue;
    }

    public void setTimeDue(int[] timeDue) {
        this.timeDue = timeDue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void toggleCompleted() {
        this.isCompleted = !this.isCompleted;
    }

    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks;
    }
}
