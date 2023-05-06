import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Epic extends Task {

    static HashMap<Epic, ArrayList<SubTask>> epicList = new HashMap<>();

    public Epic(String title, String description, int id, String status) {
        super(title, description, id, status);
    }

    public static HashMap<Epic, ArrayList<SubTask>> createEpic
    (String title, String description, int id, ArrayList<SubTask> subTasks) {
        Epic epic = new Epic(title, description, id, "New");
        epicList.put(epic, subTasks);
        return epicList;
    }

    public static void getListOfAllEpics() {
        for (Map.Entry<Epic, ArrayList<SubTask>> epic : epicList.entrySet()) {
            System.out.println(epic.getKey() + " " + epic.getValue());
        }
    }

    public static void deleteAllEpicsTasks() {
        epicList.clear();
    }

    public static Epic getEpicById(int id) {
        for(Map.Entry<Epic, ArrayList<SubTask>> epic : epicList.entrySet()) {
            if(epic.getKey().getId() == id) {
                return epic.getKey();
            }
        }
        return null;
    }

    public static boolean upgradeEpicById(String title, String description, int id) {
        for (Map.Entry<Epic, ArrayList<SubTask>> epic : epicList.entrySet() ) {
            if (epic.getKey().getId() == id) {
                epic.getKey().setTitle(title);
                epic.getKey().setDescription(description);
                epic.getKey().setId(id);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteEpicById(int id) {
        for (Iterator<Map.Entry<Epic, ArrayList<SubTask>>> iterator = epicList.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Epic, ArrayList<SubTask>> entry = iterator.next();
            if (entry.getKey().getId() == id) {
                iterator.remove();
                System.out.println("Эпик удален");
                return true;
            }
        }
        System.out.println("Эпик не найден");
        return false;
    }

    public static ArrayList<SubTask> getAllSubTasksFromEpic(int id) {
        for (Map.Entry<Epic, ArrayList<SubTask>> epic : epicList.entrySet()) {
            if (epic.getKey().getId() == id) {
                return epic.getValue();
            }
        }
        System.out.println("Эпик не найден");
        return null;
    }

    public static void checkEpicStatus() {
        boolean allNew = false;
        boolean allDone = false;
        boolean inProgress = false;

        for (ArrayList<SubTask> subTask : epicList.values()) {
            for (SubTask task : subTask) {
                if (task.getStatus().equals("New")) {
                    allNew = true;
                }  else if (task.getStatus().equals("In progress")) {
                    inProgress = true;
                } else if (task.getStatus().equals("Done")) {
                    allDone = true;
                }
            }
            for (Epic epic : epicList.keySet()) {
                if ((inProgress || allDone) && allNew) {
                    epic.setStatus("In progress");
                } else if (inProgress) {
                    epic.setStatus("In progress");
                }
                else if (allDone) {
                    epic.setStatus("Done");
                } else if (allNew) {
                    epic.setStatus("New");
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}' + "\n";
    }
}
