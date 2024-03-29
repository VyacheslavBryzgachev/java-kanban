package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task implements Comparable<Task> {
    private String title;
    private String description;
    private int id;
    Statuses status;
    TaskTypes taskType;
    protected int duration;
    protected LocalDateTime startTime;

    public Task(int id, TaskTypes taskType, String title, String description, Statuses status, LocalDateTime startTime,
                int durationMinutes) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.taskType = taskType;
        this.startTime = startTime;
        this.duration = durationMinutes;
    }

    public Task(int id, TaskTypes taskType, String title, String description, Statuses status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.taskType = taskType;
    }

    public Task(TaskTypes taskType, String title, String description, Statuses status, LocalDateTime startTime, int durationMinutes) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
        this.startTime = startTime;
        this.duration = durationMinutes;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int durationMinutes) {
        this.duration = durationMinutes;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(Duration.ofMinutes(duration));
    }

    @Override
    public int compareTo(Task o) {
        if (o.getStartTime() != null && this.startTime != null) {
            return this.startTime.compareTo(o.getStartTime());
        }
        else if (o.getStartTime() != null && this.startTime == null) {
                return 1;
        } else if (o.getStartTime() == null && this.startTime != null) {
                return -1;
        } else {
                return 0;
            }
        }
    }
