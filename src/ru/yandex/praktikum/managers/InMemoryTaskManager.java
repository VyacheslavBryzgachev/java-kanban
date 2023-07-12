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
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    HistoryManager historyManager;

    Map<Integer, Task> taskHashMap = new HashMap<>();
    Map<Integer, SubTask> subTaskHashMap = new HashMap<>();
    Map<Integer, Epic> epicHashMap = new HashMap<>();

    private int counter = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        getListOfAllTasks();
        getListOfAllSubTasks();
        getListOfAllEpics();
        TreeSet<Task> taskTreeSet = new TreeSet<>();
        taskTreeSet.addAll(getListOfAllTasks());
        taskTreeSet.addAll(getListOfAllSubTasks());
        taskTreeSet.addAll(getListOfAllEpics());
        return taskTreeSet;
    }

    public InMemoryTaskManager() {
       this.historyManager = Managers.getDefaultHistory();
    }
    public void setCounter(int counter) {
        this.counter = counter;
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

    @Override
    public List<SubTask> getListOfAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    @Override
    public List<Epic> getListOfAllEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
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
        Task task1 = taskHashMap.get(task.getId());
        if (task1 == null) {
            return null;
        }
        return taskHashMap.put(task.getId(), task);
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
        Epic epicToChange = epicHashMap.get(epic.getId());
        if (epicToChange == null) {
            return null;
        }
        epicToChange.setTitle(epic.getTitle());
        epicToChange.setDescription(epic.getDescription());
        return epic;
    }

    @Override
    public void deleteTaskById(int id) {
        taskHashMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        final SubTask subTaskToDelete = subTaskHashMap.remove(id);
        if (subTaskToDelete != null) {
            Epic epic = epicHashMap.get(subTaskToDelete.getEpicId());
            epic.deleteSubTaskFromList(subTaskToDelete.getId());
            checkEpicStatus(epic);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        final Epic epicToDelete = epicHashMap.remove(id);
        historyManager.remove(id);
        if (epicToDelete != null) {
            for (Integer subTaskId : epicToDelete.getSubTaskList()) {
                subTaskHashMap.remove(subTaskId);
                historyManager.remove(subTaskId);
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
        for(Integer id : taskHashMap.keySet()) {
            historyManager.remove(id);
        }
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for(Integer id : subTaskHashMap.keySet()) {
            historyManager.remove(id);
        }
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.deleteAllSubTasksFromList();
            checkEpicStatus(epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        for(Integer id : epicHashMap.keySet()) {
            historyManager.remove(id);
        }
        epicHashMap.clear();
        deleteAllSubTasks();
    }

    @Override
    public List<Task> getHistory() {
       return historyManager.getHistory();
    }

    private int getNextId() {
        return counter++;
    }

    private void checkEpicStatus(Epic epic) {
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
