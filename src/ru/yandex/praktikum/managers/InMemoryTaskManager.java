package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.managersInterfaces.HistoryManager;
import ru.yandex.praktikum.managersInterfaces.TaskManager;
import ru.yandex.praktikum.statuses.Statuses;
import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    HistoryManager historyManager = Managers.getDefaultHistory();

    Map<Integer, Task> taskHashMap = new HashMap<>();
    Map<Integer, SubTask> subTaskHashMap = new HashMap<>();
    Map<Integer, Epic> epicHashMap = new HashMap<>();

    private int counter = 1;

    public int getNextId() {
        return counter++;
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public Task createTask(Task task) {
        int id = getNextId();
        task.setId(id);
        return taskHashMap.put(id, task);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        int id = getNextId();
        subTask.setId(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.addSubTaskToList(id);
        subTaskHashMap.put(id, subTask);
        checkEpicStatus(epic);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        int id = getNextId();
        epic.setId(id);
        return epicHashMap.put(id, epic);
    }

    @Override
    public List<Task> getListOfAllTasks() {
       return new ArrayList<>(taskHashMap.values());
    }

    public List<SubTask> getListOfAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public List<Epic> getListOfAllEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    public Task getTaskById(int id) {
        Task task = taskHashMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicHashMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Task upgradeTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
           return taskHashMap.put(task.getId(), task);

        } else
                return null;
    }

    @Override
    public SubTask upgradeSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        if (epic != null && epic.getSubTaskList().contains(subTask.getId())) {
           subTaskHashMap.put(subTask.getId(), subTask);
           checkEpicStatus(epic);
           return subTask;
        } else
            return null;
    }

    @Override
    public Epic upgradeEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            Epic epicToChange = epicHashMap.get(epic.getId());
            epicToChange.setTitle(epic.getTitle());
            epicToChange.setDescription(epic.getDescription());
            return epic;
        } else
                return null;
    }

    @Override
    public void deleteTaskById(int id) {
        taskHashMap.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        final SubTask subTaskToDelete = subTaskHashMap.remove(id);
        if (subTaskToDelete != null) {
            Epic epic = epicHashMap.get(subTaskToDelete.getEpicId());
            epic.deleteSubTaskFromList(subTaskToDelete.getId());
            checkEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        final Epic epicToDelete = epicHashMap.remove(id);
        if (epicToDelete != null) {
            for (Integer subTaskId : epicToDelete.getSubTaskList()) {
                subTaskHashMap.remove(subTaskId);
            }
        }
    }

    @Override
    public ArrayList<SubTask> getAllEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer id : epic.getSubTaskList()) {
               subTasks.add(subTaskHashMap.get(id));
        }
        return subTasks;
    }

    @Override
    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.deleteAllSubTasksFromList();
            checkEpicStatus(epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        epicHashMap.clear();
        deleteAllSubTasks();
    }

    public List<Task> viewHistory() {
       return historyManager.getHistory();
    }

    public void checkEpicStatus(Epic epic) {

        if (epic != null) {

        boolean allNew = false;
        boolean allDone = false;
        boolean inProgress = false;

            if (epic.getSubTaskList().isEmpty()) {
                epic.setStatus(Statuses.NEW);
            }

                for (SubTask subTask : getAllEpicSubTasks(epic)) {
                    if (subTask.getStatus().equals(Statuses.NEW)) {
                        allNew = true;
                    } else if (subTask.getStatus().equals(Statuses.IN_PROGRESS)) {
                            inProgress = true;
                    } else if (subTask.getStatus().equals(Statuses.DONE)) {
                                allDone = true;
                    }
                }

            if ((inProgress || allDone) && allNew) {
                epic.setStatus(Statuses.IN_PROGRESS);
            } else if (inProgress) {
                epic.setStatus(Statuses.IN_PROGRESS);
            } else if (allDone) {
                epic.setStatus(Statuses.DONE);
            } else if (allNew) {
                epic.setStatus(Statuses.NEW);
            }
        }
    }
}
