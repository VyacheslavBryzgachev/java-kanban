import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private ArrayList<SubTask> subTaskList = new ArrayList<>();

    private int counter = 1;

    public int getNextId() {
            return counter++;
    }

    public int getCounter() {
            return counter;
    }

    public Task createTask(Task task) {
            taskHashMap.put(getNextId(), task);
            return task;
    }

    public SubTask createSubTask(SubTask subTask, int epicId) {
        if (epicHashMap.containsKey(epicId)) {
            subTaskHashMap.put(getNextId(), subTask);
            return subTask;
        } else
            return null;
    }

    public Epic createEpic(Epic epic) {
        epicHashMap.put(getNextId(), epic);
        return epic;
    }
    
    public ArrayList<Task> getListOfAllTasks() {
            ArrayList<Task> arrayList = new ArrayList<>();
            for (Task task : taskHashMap.values()) {
                arrayList.add(task);
            }
            for (SubTask subTask : subTaskHashMap.values()) {
                arrayList.add(subTask);
            }
            for (Epic epic : epicHashMap.values()) {
                arrayList.add(epic);
            }
        return arrayList;
    }

    public ArrayList<SubTask> getListOfAllSubTasks() {
        ArrayList<SubTask> arrayList = new ArrayList<>();
        for (SubTask subTask : subTaskHashMap.values()) {
            arrayList.add(subTask);
        }
        return arrayList;
    }

    public ArrayList<Epic> getListOfAllEpics() {
        ArrayList<Epic> arrayList = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            arrayList.add(epic);
        }
        return arrayList;
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

    public void upgradeTask(Task task) {
       taskHashMap.put(task.getId(), task);
    }

    public void upgradeSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
    }

    public void upgradeEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
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

    public ArrayList<SubTask> getAllEpicSubTasks(int id) {
        if (epicHashMap.containsKey(id)) {
            subTaskList.addAll(subTaskHashMap.values());
       }
        return subTaskList;
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
        subTaskHashMap.clear();
        epicHashMap.clear();
    }

    public void checkEpicStatus(int id) {
        ArrayList<SubTask> subTasks = getAllEpicSubTasks(id);
        boolean allNew = false;
        boolean allDone = false;
        boolean inProgress = false;

            for (SubTask task : subTasks) {
                if (task.getStatus().equals("New")) {
                    allNew = true;
                }  else if (task.getStatus().equals("In progress")) {
                    inProgress = true;
                } else if (task.getStatus().equals("Done")) {
                    allDone = true;
                }
                if ((inProgress || allDone) && allNew) {
                    epicHashMap.get(id).setStatus("In progress");
                } else if (inProgress) {
                    epicHashMap.get(id).setStatus("In progress");
                } else if (allDone) {
                    epicHashMap.get(id).setStatus("Done");
                } else if (allNew) {
                    epicHashMap.get(id).setStatus("New");
                }
            }
    }
}


