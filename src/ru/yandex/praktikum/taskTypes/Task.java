package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

public class Task {
    private String title;
    private String description;
    private int id;
    Statuses status;

    public Task(String title, String description, int id, Statuses status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String title, String description, Statuses status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId () {
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
}
