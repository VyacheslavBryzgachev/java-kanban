package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.exceptions.ManagerSaveException;
import ru.yandex.praktikum.managersInterfaces.HistoryManager;
import ru.yandex.praktikum.statuses.Statuses;
import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;
import ru.yandex.praktikum.taskTypes.TaskTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String path;
        int maxId = 0;

        try {
            path = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        String[] fileLine = path.split("'\n'");
        for (int i = 1; i < fileLine.length; i++) {
            if (fileLine[i].isEmpty()) {
                String history = fileLine[i + 1];
                List<Integer> idsList = historyFromString(history);
                for (int id : idsList) {
                    Task task = fileBackedTasksManager.getTaskById(id);
                    fileBackedTasksManager.historyManager.add(task);
                }
            }
                fileBackedTasksManager.fromString(fileLine[i], fileBackedTasksManager);
            }
                for(int epicId : fileBackedTasksManager.epicHashMap.keySet()) {
                   if(maxId < epicId) {
                       maxId = epicId;
                   }
               }
                for(int subTaskId : fileBackedTasksManager.subTaskHashMap.keySet()) {
                    if(maxId < subTaskId) {
                        maxId = subTaskId;
                    }
                }
                for(int taskId : fileBackedTasksManager.taskHashMap.keySet()) {
                    if(maxId < taskId) {
                        maxId = taskId;
                    }
                }

                for (SubTask subTask : fileBackedTasksManager.subTaskHashMap.values()) {
                    int id = subTask.getEpicId();
                    Epic epic = fileBackedTasksManager.getEpicById(id);
                    epic.addSubTaskToList(id);
                }

            fileBackedTasksManager.setCounter(maxId + 1);
            return fileBackedTasksManager;
    }
    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask createdSubTask = super.createSubTask(subTask);
        save();
        return createdSubTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public List<Task> getListOfAllTasks() {
        List<Task> taskList = super.getListOfAllTasks();
        save();
        return taskList;
    }

    @Override
    public List<SubTask> getListOfAllSubTasks() {
        List<SubTask> subTasksList = super.getListOfAllSubTasks();
        save();
        return subTasksList;
    }

    @Override
    public List<Epic> getListOfAllEpics() {
       List<Epic> epicList = super.getListOfAllEpics();
       save();
       return epicList;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Task upgradeTask(Task task) {
        Task upgradeTask = super.upgradeTask(task);
        save();
        return upgradeTask;
    }

    @Override
    public SubTask upgradeSubTask(SubTask subTask) {
        SubTask upgradeSubTask = super.upgradeSubTask(subTask);
        save();
        return upgradeSubTask;
    }

    @Override
    public Epic upgradeEpic(Epic epic) {
        Epic upgradeEpic = super.upgradeEpic(epic);
        save();
        return upgradeEpic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public ArrayList<SubTask> getAllEpicSubTasks(Epic epic) {
        ArrayList<SubTask> epicSubtasks = super.getAllEpicSubTasks(epic);
        save();
        return epicSubtasks;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    private void save() {
        try {
            Writer writer = new FileWriter(file);
            writer.write("id,type,name,status,description,epicId" + "\n");

            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(super.getListOfAllTasks());
            allTasks.addAll(super.getListOfAllEpics());
            allTasks.addAll(super.getListOfAllSubTasks());

            for (Task task : allTasks) {
                String st = toString(task);
                writer.write(st);
            }
            writer.write('\n');
            writer.write('\n');

            String history = historyToString(super.historyManager);
            writer.write(history);
            writer.close();
        }
        catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл!");
        }
    }
    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId());
        sb.append(",");
        sb.append(task.getTaskType());
        sb.append(",");
        sb.append(task.getTitle());
        sb.append(",");
        sb.append(task.getDescription());
        sb.append(",");
        sb.append(task.getStatus());
        if(task.getTaskType() == TaskTypes.SUB_TASK) {
            SubTask subTask = subTaskHashMap.get(task.getId());
            sb.append(",");
            sb.append(subTask.getEpicId());
        }
        sb.append('\n');
        return sb.toString();
    }
    private void fromString(String value, FileBackedTasksManager fileBackedTasksManager) {
        String[] parts = value.split(", ");
        if(parts[1].contains(TaskTypes.SUB_TASK.toString())) {
            int id = Integer.parseInt(parts[0]);
            TaskTypes type = TaskTypes.valueOf(parts[1]);
            String name = parts[2];
            String description = parts[3];
            Statuses status = Statuses.valueOf(parts[4]);
            int epicId = Integer.parseInt(parts[5]);
            SubTask subTask =  new SubTask(id, type, name, description, status, epicId);
            fileBackedTasksManager.taskHashMap.put(id, subTask);
        } else if (parts[1].contains(TaskTypes.EPIC.toString())) {
            int id = Integer.parseInt(parts[0]);
            TaskTypes type = TaskTypes.valueOf(parts[1]);
            String name = parts[2];
            String description = parts[3];
            Statuses status = Statuses.valueOf(parts[4]);
            Epic epic = new Epic(id, type, name, description, status);
            fileBackedTasksManager.epicHashMap.put(id, epic);
        }

        int id = Integer.parseInt(parts[0]);
        TaskTypes type = TaskTypes.valueOf(parts[1]);
        String name = parts[2];
        String description = parts[3];
        Statuses status = Statuses.valueOf(parts[4].trim());
        Task task = new Task(id, type, name, description, status);
        fileBackedTasksManager.taskHashMap.put(id, task);
    }
    private static String historyToString(HistoryManager manager) {
        List<String> s = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            s.add(String.valueOf(task.getId()));
        }
        return String.join(",", s);
    }
    private static List<Integer> historyFromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> tasksId = new ArrayList<>();
        for (String idString : idsString) {
            tasksId.add(Integer.valueOf(idString));
        }
        return tasksId;
    }
}
