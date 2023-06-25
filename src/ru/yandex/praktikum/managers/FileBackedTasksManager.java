package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.exceptions.ManagerSaveException;
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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
       try {
           Writer writer = new FileWriter(file);
           writer.write("id,type,name,status,description,epicId" + "\n");

           List<Task> taskList = super.getListOfAllTasks();
           List<SubTask> subTaskList = super.getListOfAllSubTasks();
           List<Epic> epicList = super.getListOfAllEpics();
           List<Task> allTasks = new ArrayList<>();
           allTasks.addAll(taskList);
           allTasks.addAll(epicList);
           allTasks.addAll(subTaskList);

           for (Task task : allTasks) {
               String st = toString(task);
               String[] split = st.split(", ");
               for (String a : split) {
                   writer.write(a);
               }
           }
           writer.write('\n');
           List<Task> history = super.getHistory();

           for (Task task : history) {
               int id = task.getId();
               writer.write(String.valueOf(id));
               writer.write(", ");
           }
           writer.close();
       }
       catch (IOException e) {
           throw new ManagerSaveException("Ошибка записи в файл!");
       }
    }

    public String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        if(task.getTaskType() == TaskTypes.SUB_TASK) {
            SubTask subTask = subTaskHashMap.get(task.getId());
            sb.append(subTask.getId());
            sb.append(",");
            sb.append(subTask.getTaskType());
            sb.append(",");
            sb.append(subTask.getTitle());
            sb.append(",");
            sb.append(subTask.getDescription());
            sb.append(",");
            sb.append(subTask.getStatus());
            sb.append(",");
            sb.append(subTask.getEpicId());
            sb.append('\n');
            return sb.toString();
        }
        sb.append(task.getId());
        sb.append(",");
        sb.append(task.getTaskType());
        sb.append(",");
        sb.append(task.getTitle());
        sb.append(",");
        sb.append(task.getDescription());
        sb.append(",");
        sb.append(task.getStatus());
        sb.append('\n');
        return sb.toString();
    }

    public Task fromString(String value) {
        if(value.contains("epicId")) {
            String[] parts = value.split(",");
            int id = Integer.parseInt(parts[0]);
            TaskTypes type = TaskTypes.valueOf(parts[1]);
            String name = parts[2];
            String description = parts[3];
            Statuses status = Statuses.valueOf(parts[4]);
            int epicId = Integer.parseInt(parts[5]);
            return new SubTask(id, type, name, description, status, epicId);
        }

        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskTypes type = TaskTypes.valueOf(parts[1]);
        String name = parts[2];
        String description = parts[3];
        Statuses status = Statuses.valueOf(parts[4].trim());
        return new Task(id, type, name, description, status);
    }


    public static String historyToString(InMemoryHistoryManager manager) {
        List<String> s = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            s.add(String.valueOf(task.getId()));
        }
        return String.join(",", s);
    }

    public static List<Integer> historyFromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> tasksId = new ArrayList<>();
        for (String idString : idsString) {
            tasksId.add(Integer.valueOf(idString));
        }
        return tasksId;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        String path;
        try {
            path = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        String[] split = path.split("'\n'");
        for (String st : split) {
            if(st.contains(TaskTypes.TASK.toString())) {
                Task task = fileBackedTasksManager.fromString(st);
                int id = task.getId();
                fileBackedTasksManager.taskHashMap.put(id, task);
            } else if (st.contains(TaskTypes.SUB_TASK.toString())) {
                SubTask subTask = (SubTask) fileBackedTasksManager.fromString(st);
                int id = subTask.getId();
                fileBackedTasksManager.subTaskHashMap.put(id, subTask);
            } else if (containsOnlyIntegers(st)) {
                String[] ids = st.split(", ");
                for (String s : ids) {
                   int id = Integer.parseInt(s);
                   Task task = fileBackedTasksManager.getTaskById(id);
                   inMemoryHistoryManager.add(task);
                }
            } else {
                Epic epic = (Epic) fileBackedTasksManager.fromString(st);
                int id = epic.getId();
                fileBackedTasksManager.epicHashMap.put(id, epic);
            }
        }
        return fileBackedTasksManager;
    }

    public static boolean containsOnlyIntegers(String value) {
        String[] parts = value.split(",");
        for (String part : parts) {
            try {
                Integer.parseInt(part);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
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
}
