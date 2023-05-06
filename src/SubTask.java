import java.util.ArrayList;

public class SubTask extends Task {

    public SubTask(String title, String description, int id, String status) {
        super(title, description, id, status);
    }

    public static SubTask createSubTask(String title, String description, int id) {
        SubTask subTask = new SubTask(title, description, id, "New");
        return subTask;
    }

    public static void getListOfAllSubTasks(ArrayList<SubTask> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\n" + tasks.get(i));
        }
    }

    public static void deleteAllSubTasks(ArrayList<SubTask> subTasks) {
        subTasks.clear();
        System.out.println("Удалено");
    }

    public static SubTask getSubTaskById(int id, ArrayList<SubTask> subTasks) {
        for(SubTask subTask : subTasks) {
            if(subTask.getId() == id) {
                return subTask;
            }
        }
        return null;
    }

    public static boolean deleteSubTaskById (int id, ArrayList<SubTask> subTasks) {
        for (SubTask subTask : subTasks) {
            if (subTask.getId() == id) {
                subTasks.remove(subTask);
                return true;
            }
        }
        return false;
    }

    public static boolean upgradeSubTaskById(String title, String description, int id, String status, ArrayList<SubTask> subTasks) {
        for (SubTask subTask : subTasks) {
            if (subTask.getId() == id) {
                subTask.setTitle(title);
                subTask.setDescription(description);
                subTask.setId(id);
                subTask.setStatus(status);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}\n";
    }
}
