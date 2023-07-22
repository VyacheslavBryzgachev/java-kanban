package ru.yandex.praktikum.managersInterfaces;

import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;

import java.util.List;

public interface TaskManager {
    int getCounter();
    Task createTask(Task task);
    SubTask createSubTask(SubTask subTask);
    Epic createEpic(Epic epic);
    List<Task> getListOfAllTasks();
    List<SubTask> getListOfAllSubTasks();
    List<Epic> getListOfAllEpics();
    Task getTaskById(int id);
    SubTask getSubTaskById(int id);
    Epic getEpicById(int id);
    Task upgradeTask(Task task);
    SubTask upgradeSubTask(SubTask subTask);
    Epic upgradeEpic(Epic epic);
    void deleteTaskById(int id);
    void deleteSubTaskById(int id);
    void deleteEpicById(int id);
    List<SubTask> getAllEpicSubTasks(Epic epic);
    void deleteAllTasks();
    void deleteAllSubTasks();
    void deleteAllEpics();
    List<Task> getHistory();
    List<Task> getPrioritizedTasks();
}
