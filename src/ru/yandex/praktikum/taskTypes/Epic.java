package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, TaskTypes taskType, String title, String description, Statuses status) {
        super(id, taskType, title, description, status);
    }

    public Epic(TaskTypes taskType, String title, String description, Statuses status) {
        super(taskType, title, description, status);
    }

    public void calculateStartAndEndEpicTime(ArrayList<SubTask> subTasks) {
        if (subTaskList.isEmpty()) {
            return;
        }

        LocalDateTime epicsStartTime = subTasks.stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MAX);

        LocalDateTime epicEndTime = subTasks.stream()
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MIN);

        if (epicEndTime != LocalDateTime.MIN && epicsStartTime != LocalDateTime.MAX) {
            endTime = epicEndTime;
            startTime = epicsStartTime;
            duration = subTasks.stream()
                    .mapToInt(subTask -> getDuration())
                    .sum();
        }
    }

    public LocalDateTime getEndTime(Epic epic) {
        return endTime;
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
