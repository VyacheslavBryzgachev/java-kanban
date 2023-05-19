package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;
import ru.yandex.praktikum.managersInterfaces.TaskManager;
import ru.yandex.praktikum.statuses.Statuses;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int counter = 1;

    @Override
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
        return TaskManager.taskHashMap.put(id, task);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        int id = getNextId();
        subTask.setId(id);
        Epic epic = TaskManager.epicHashMap.get(subTask.getEpicId());
        epic.addSubTaskToList(id);
        TaskManager.subTaskHashMap.put(id, subTask);
        checkEpicStatus(epic);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        int id = getNextId();
        epic.setId(id);
        return TaskManager.epicHashMap.put(id, epic);
    }

    @Override
    public ArrayList<Task> getListOfAllTasks() {
       return new ArrayList<>(TaskManager.taskHashMap.values());
    }

    public ArrayList<SubTask> getListOfAllSubTasks() {
        return new ArrayList<>(TaskManager.subTaskHashMap.values());
    }

    public ArrayList<Epic> getListOfAllEpics() {
        return new ArrayList<>(TaskManager.epicHashMap.values());
    }

    public Task getTaskById(int id) {
        Task task = TaskManager.taskHashMap.get(id);
        if(TaskManager.historyList.size() >= 10) {
            TaskManager.historyList.remove(9);
        }
        TaskManager.historyList.add(0, task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = TaskManager.subTaskHashMap.get(id);
        if(TaskManager.historyList.size() >= 10) {
            TaskManager.historyList.remove(9);
        }
        TaskManager.historyList.add(0, subTask);
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = TaskManager.epicHashMap.get(id);
        if(TaskManager.historyList.size() >= 10){
            TaskManager.historyList.remove(9);
        }
        TaskManager.historyList.add(0, epic);
        return epic;
    }

    @Override
    public Task upgradeTask(Task task) {
        if (TaskManager.taskHashMap.containsKey(task.getId())) {
           return TaskManager.taskHashMap.put(task.getId(), task);

        } else
                return null;
    }

    @Override
    public SubTask upgradeSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        if (epic != null && epic.getSubTaskList().contains(subTask.getId())) {
           TaskManager.subTaskHashMap.put(subTask.getId(), subTask);
           checkEpicStatus(epic);
           return subTask;
        } else
            return null;
    }

    @Override
    public Epic upgradeEpic(Epic epic) {
        if (TaskManager.epicHashMap.containsKey(epic.getId())) {
            Epic epicToChange = TaskManager.epicHashMap.get(epic.getId());
            epicToChange.setTitle(epic.getTitle());
            epicToChange.setDescription(epic.getDescription());
            return epic;
        } else
                return null;
    }

    @Override
    public void deleteTaskById(int id) {
        TaskManager.taskHashMap.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        final SubTask subTaskToDelete = TaskManager.subTaskHashMap.remove(id);
        if (subTaskToDelete != null) {
            Epic epic = TaskManager.epicHashMap.get(subTaskToDelete.getEpicId());
            epic.deleteSubTaskFromList(subTaskToDelete.getId());
            checkEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        final Epic epicToDelete = TaskManager.epicHashMap.remove(id);
        if (epicToDelete != null) {
            for (Integer subTaskId : epicToDelete.getSubTaskList()) {
                TaskManager.subTaskHashMap.remove(subTaskId);
            }
        }
    }

    @Override
    public ArrayList<SubTask> getAllEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer id : epic.getSubTaskList()) {
               subTasks.add(TaskManager.subTaskHashMap.get(id));
        }
        return subTasks;
    }

    @Override
    public void deleteAllTasks() {
        TaskManager.taskHashMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        TaskManager.subTaskHashMap.clear();
        for (Epic epic : TaskManager.epicHashMap.values()) {
            epic.deleteAllSubTasksFromList();
            checkEpicStatus(epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        TaskManager.epicHashMap.clear();
        deleteAllSubTasks();
    }

    @Override
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
    @Override
    public List<Task> getHistory() {
        return TaskManager.historyList;
    }
}
