package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(int id, TaskTypes taskType, String title, String description,
                   Statuses status, LocalDateTime startTime, int durationMinutes, int epicId) {
        super(id, taskType, title, description, status, startTime, durationMinutes);
        this.epicId = epicId;
    }

    public SubTask(TaskTypes taskType, String title, String description, Statuses status, LocalDateTime startTime, int durationMinutes, int epicId) {
        super(taskType, title, description, status, startTime, durationMinutes);
        this.epicId = epicId;
    }

    public SubTask(int id, TaskTypes taskType, String title, String description, Statuses status, int epicId) {
        super(id, taskType, title, description, status);
        this.epicId = epicId;
    }

    public SubTask(TaskTypes taskType, String title, String description, Statuses status, int epicId) {
        super(taskType, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
