package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.managersInterfaces.HistoryManager;
import ru.yandex.praktikum.taskTypes.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    @Override
    public void add(Task task) {
        viewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
