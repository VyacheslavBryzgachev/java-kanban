package ru.yandex.praktikum.managersInterfaces;

import ru.yandex.praktikum.taskTypes.Task;

import java.util.List;
public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
