package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

import java.time.Duration;
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

    public Epic(int id, TaskTypes taskType, String title, String description, Statuses status) {
        super(id, taskType, title, description, status);
    }

    public Epic(TaskTypes taskType, String title, String description, Statuses status) {
        super(taskType, title, description, status);
    }

    public LocalDateTime getEndTime(Epic epic) {
        if (subTaskList.isEmpty()) {
            return null;
        }
        return endTime;
    }

    public void calculateStartAndEndEpicTime(ArrayList<SubTask> subTasks) {
        LocalDateTime epicsStartTime = LocalDateTime.MIN;
        LocalDateTime epicEndTime = LocalDateTime.MIN;
        for(SubTask subTask : subTasks) {
            LocalDateTime subTaskStartTime = subTask.getStartTime();
            LocalDateTime subTaskEndTime = subTask.getEndTime();
            if(subTaskEndTime != null && subTaskStartTime != null) {
                if(subTaskStartTime.isAfter(epicsStartTime)) {
                    epicsStartTime = subTaskStartTime;
                }
                if(subTaskEndTime.isAfter(epicEndTime)) {
                    epicEndTime = subTaskEndTime;
                }
                endTime = epicEndTime;
                startTime = epicsStartTime;
                Duration d  = Duration.between(startTime, endTime);
                duration = (int) d.toMinutes();
            }
        }
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
