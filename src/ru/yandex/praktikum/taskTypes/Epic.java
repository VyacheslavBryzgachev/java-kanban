package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskList = new ArrayList<>();

    public Epic(int id, TaskTypes taskType, String title, String description, Statuses status) {
        super(id, taskType, title, description, status);
    }

    public Epic(TaskTypes taskType, String title, String description, Statuses status) {
        super(taskType, title, description, status);
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
