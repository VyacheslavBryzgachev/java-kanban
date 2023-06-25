package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

public class Task {
    private String title;
    private String description;
    private int id;
    Statuses status;
    TaskTypes taskType;

    public Task (int id, TaskTypes taskType, String title, String description, Statuses status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.taskType = taskType;
    }

    public Task(TaskTypes taskType, String title, String description, Statuses status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
    }

    public int getId() {
        return id;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
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

    public void setId(int id) {
        this.id = id;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }
}
