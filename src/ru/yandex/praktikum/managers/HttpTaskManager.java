package ru.yandex.praktikum.managers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.praktikum.server.KVTaskClient;
import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;

import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
   protected KVTaskClient client;
   private final Gson gson = new Gson();

    public HttpTaskManager() {
        client = new KVTaskClient();
    }

    public KVTaskClient getClient() {
        return client;
    }


    public void save() {
        String prioritizedTasks = gson.toJson(getPrioritizedTasks());
        client.put("tasks", prioritizedTasks);

        String history = gson.toJson(getHistory());
        client.put("tasks/history", history);

        String tasks = gson.toJson(getListOfAllTasks());
        client.put("tasks/task", tasks);

        String subtasks = gson.toJson(gson.toJson(getListOfAllSubTasks()));
        client.put("tasks/subtask", subtasks);

        String epics = gson.toJson(getListOfAllEpics());
        client.put("tasks/epic", epics);
    }

    public void load() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        int maxId = 0;

        String jsonPrioritizedTasks = getClient().load("tasks");
        var prioritizedTaskType = new TypeToken<List<Task>>(){}.getType();
        List<Task> prioritizedTasks = gson.fromJson(jsonPrioritizedTasks, prioritizedTaskType);
        inMemoryTaskManager.prioritizedTasks.addAll(prioritizedTasks);

        String jsonHistory = getClient().load("tasks/history");
        var historyType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(jsonHistory, historyType);
        for(Task task : history) {
            inMemoryTaskManager.historyManager.add(task);
        }

        String jsonTasks = getClient().load("tasks/task");
        var taskType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(jsonTasks, taskType);
        for (Task task : tasks) {
            int taskId = task.getId();
            inMemoryTaskManager.taskHashMap.put(taskId, task);
            if(maxId < taskId) {
                maxId = taskId;
            }
        }

        String jsonSubTasks = getClient().load("tasks/subtask");
        var subTaskType = new TypeToken<List<SubTask>>(){}.getType();
        List<SubTask> subTasks = gson.fromJson(jsonSubTasks, subTaskType);
        for(SubTask subTask : subTasks) {
            int subTaskId = subTask.getId();
            int subTaskEpicId = subTask.getEpicId();
            inMemoryTaskManager.subTaskHashMap.put(subTaskId, subTask);
            Epic epic = inMemoryTaskManager.getEpicById(subTaskEpicId);
            epic.addSubTaskToList(subTaskId);
            if(maxId < subTaskId) {
                maxId = subTaskId;
            }
        }

        String jsonEpics = getClient().load("tasks/epic");
        var epicType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, epicType);
        for(Epic epic : epics) {
            int epicId = epic.getId();
            inMemoryTaskManager.epicHashMap.put(epicId, epic);
            if(maxId < epicId) {
                maxId = epicId;
            }
        }

        inMemoryTaskManager.setCounter(maxId + 1);
    }
}
