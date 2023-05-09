import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();

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
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.addSubTaskToList(id);
        subTaskHashMap.put(id, subTask);
        checkEpicStatus(epic);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        int id = getNextId();
        epic.setId(id);
        return epicHashMap.put(id, epic);
    }
    
    public ArrayList<Task> getListOfAllTasks() {
       return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<SubTask> getListOfAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public Collection<Epic> getListOfAllEpics() {
        return new ArrayList<>(epicHashMap.values());
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
           return taskHashMap.put(task.getId(), task);

    } else
            return null;
    }

    public SubTask upgradeSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        if (epic != null) {
           subTaskHashMap.put(subTask.getId(), subTask);
           checkEpicStatus(epic);
           return subTask;
    } else
            return null;
    }

    public Epic upgradeEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            Epic epicToChange = epicHashMap.get(epic.getId());
            epicToChange.setTitle(epic.getTitle());
            epicToChange.setDescription(epic.getDescription());
            return epic;
    } else
            return null;
    }

    public void deleteTaskById(int id) {
        taskHashMap.remove(id);
    }

    public void deleteSubTaskById(int id) {
        subTaskHashMap.remove(id);
        Epic epic = getEpicById(id);
        epic.deleteSubTaskFromList(subTaskHashMap.get(id));
        checkEpicStatus(epic);
    }

    public void deleteEpicById(int id) {
        epicHashMap.remove(id);
        ArrayList<SubTask> subTasks = getAllEpicSubTasks(getEpicById(id));
        subTasks.clear();
    }

    public ArrayList<SubTask> getAllEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer id : epic.getSubTaskList()) {
               subTasks.add(subTaskHashMap.get(id));
            }
        return subTasks;
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllSubTasks() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.deleteAllSubTasksFromList();
            checkEpicStatus(epic);
        }
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
        deleteAllSubTasks();
    }

    private void checkEpicStatus(Epic epic) {
        boolean allNew = false;
        boolean allDone = false;
        boolean inProgress = false;

        if (epic != null) {
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
}
