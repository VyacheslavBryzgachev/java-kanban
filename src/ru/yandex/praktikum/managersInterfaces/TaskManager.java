package ru.yandex.praktikum.managersInterfaces;

import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    List<Task> historyList = new ArrayList<>();
    int getNextId();
    int getCounter();
    Task createTask(Task task);
    SubTask createSubTask(SubTask subTask);
    Epic createEpic(Epic epic);
    ArrayList<Task> getListOfAllTasks();
    ArrayList<SubTask> getListOfAllSubTasks();
    ArrayList<Epic> getListOfAllEpics();
    Task getTaskById(int id);
    SubTask getSubTaskById(int id);
    Epic getEpicById(int id);
    Task upgradeTask(Task task);
    SubTask upgradeSubTask(SubTask subTask);
    Epic upgradeEpic(Epic epic);
    void deleteTaskById(int id);
    void deleteSubTaskById(int id);
    void deleteEpicById(int id);
    ArrayList<SubTask> getAllEpicSubTasks(Epic epic);
    void deleteAllTasks();
    void deleteAllSubTasks();
    void deleteAllEpics();
    void checkEpicStatus(Epic epic);
    List<Task> getHistory();
}
