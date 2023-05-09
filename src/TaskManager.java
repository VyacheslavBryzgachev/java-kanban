import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    private int counter = 1;

    public int getNextId() {
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

    public Task createTask(Task task) {
        int id = getNextId();
        task.setId(id);
        return taskHashMap.put(id, task);
    }

    public SubTask createSubTask(SubTask subTask) {
        int id = getNextId();
        subTask.setId(id);
        int epicId = subTask.getEpicId();
        Epic epic = epicHashMap.get(epicId);
        epic.addSubTaskToList(id);
        subTaskHashMap.put(id, subTask);
        checkEpicStatus(epicHashMap.get(epicId));
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        int id = getNextId();
        epic.setId(id);
        return epicHashMap.put(id, epic);

    }
    
    public Collection<Task> getListOfAllTasks() {
        return taskHashMap.values();
    }

    public Collection<SubTask> getListOfAllSubTasks() {
        return subTaskHashMap.values();
    }

    public Collection<Epic> getListOfAllEpics() {
        return epicHashMap.values();
    }

    public Task getTaskById(int id) {
        return taskHashMap.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTaskHashMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicHashMap.get(id);
    }

    public Task upgradeTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
            return task;
    } else
            return null;
    }

    public SubTask upgradeSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        if (epicHashMap.containsKey(subTask.getEpicId()) && epic.subTaskList.contains(subTask.getId())) {
           subTaskHashMap.put(subTask.getId(), subTask);
           checkEpicStatus(epicHashMap.get(subTask.getEpicId()));
           return subTask;
    } else
            return null;
    }

    public Epic upgradeEpic(Epic epic, String newTitle, String newDescription) {
        if (epicHashMap.containsKey(epic.getId())) {
            epic.setTitle(newTitle);
            epic.setDescription(newDescription);
            epicHashMap.put(epic.getId(),epic);
            return epic;
    } else
            return null;
    }

    public void deleteTaskById(int id) {
        taskHashMap.remove(id);
    }

    public void deleteSubTaskById(int id) {
        taskHashMap.remove(id);
    }

    public void deleteEpicById(int id) {
        epicHashMap.remove(id);
    }

    public ArrayList<SubTask> getAllEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer id : epic.getSubTaskList()) {
               SubTask subTask = subTaskHashMap.get(id);
               subTasks.add(subTask);
            }
        return subTasks;
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllSubTasks() {
        subTaskHashMap.clear();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
    }

    private void checkEpicStatus(Epic epic) {
        boolean allNew = false;
        boolean allDone = false;
        boolean inProgress = false;

            if (epic.getSubTaskList().isEmpty()) {
                epic.setStatus("New");
            }

                for (SubTask subTask : getAllEpicSubTasks(epic)) {
                if (subTask.getStatus().equals("New")) {
                    allNew = true;
                } else if (subTask.getStatus().equals("In progress")) {
                    inProgress = true;
                } else if (subTask.getStatus().equals("Done")) {
                    allDone = true;
                }
            }
                if ((inProgress || allDone) && allNew) {
                    epic.setStatus("In progress");
                } else if (inProgress) {
                    epic.setStatus("In progress");
                } else if (allDone) {
                    epic.setStatus("Done");
                } else if (allNew) {
                    epic.setStatus("New");
                }
    }

}
