package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.managersInterfaces.HistoryManager;
import ru.yandex.praktikum.managersInterfaces.TaskManager;

public class Managers  <T extends TaskManager> {

    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
