package com.ualr.todohub.model;

import java.util.Calendar;
import java.util.List;

public class Task {

    private int id;
    private int parentID;
    private Calendar dueDate;
    private int[] timeDue;
    private String title;
    private String description;
    private boolean isCompleted;
    private List<Task> subtasks;

    public Task() {
        this.isCompleted = false;
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

    public Calendar getDueDate() {
        return dueDate;
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

    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks;
    }
}
