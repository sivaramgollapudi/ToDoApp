package com.sivaram.todoapp;

/**
 * Created by User on 06/10/2017.
 */

public class ToDoList {

    private String title; // Task Title
    private String description; //  Task Description
    private String actionDate; // Task Date
    private int status; // Task Status

    public ToDoList(String title, String description, String actionDate, int status) {
        this.title = title;
        this.description = description;
        this.actionDate = actionDate;
        this.status = status;
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

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
