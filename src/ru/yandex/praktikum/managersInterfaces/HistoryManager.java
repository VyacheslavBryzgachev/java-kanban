package ru.yandex.praktikum.managersInterfaces;

import ru.yandex.praktikum.taskTypes.Task;

import java.util.ArrayList;
import java.util.List;
public interface HistoryManager {
    List<Task> viewedTasks = new ArrayList<>();
    void add(Task task);
    List<Task> getHistory();
}
