package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.managers.InMemoryTaskManager;
import ru.yandex.praktikum.statuses.Statuses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, TaskTypes taskType, String title, String description, Statuses status, LocalDateTime startTime, int durationMinutes) {
        super(id, taskType, title, description, status, startTime, durationMinutes);
    }

    public Epic(TaskTypes taskType, String title, String description, Statuses status, LocalDateTime startTime, int durationMinutes) {
        super(taskType, title, description, status, startTime, durationMinutes);
    }

    public LocalDateTime getEndTime(InMemoryTaskManager inMemoryTaskManager) {
        LocalDateTime epicStartTime = getStartTime();
        endTime = LocalDateTime.MIN;
        for(Integer id : subTaskList) {
            SubTask subTask = inMemoryTaskManager.getSubTaskById(id);
            LocalDateTime subTaskStartTime = subTask.getStartTime();
            LocalDateTime subTaskEndTime = subTask.getEndTime();
            if(subTaskStartTime.isAfter(epicStartTime)) {
                epicStartTime = subTaskStartTime;
            }
            if(subTaskEndTime.isAfter(endTime)) {
                endTime = subTaskEndTime;
            }
        }
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setDuration(int durationMinutes) {
       this.duration = durationMinutes;
    }

    public List<Integer> getSubTaskList() {
        return subTaskList;
    }

    public List<Integer> addSubTaskToList(Integer id) {
        subTaskList.add(id);
        return subTaskList;
    }

    public void deleteSubTaskFromList(Integer id) {
        subTaskList.remove(id);
    }

    public void deleteAllSubTasksFromList() {
        subTaskList.clear();
    }
}
