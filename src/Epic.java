import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> subTaskList = new ArrayList<>();

    public Epic(String title, String description, int id, String status) {
        super(title, description, id, status);
    }

    public Epic(String title, String description, String status) {
        super(title, description, status);
    }

    public ArrayList<Integer> getSubTaskList() {
        return subTaskList;
    }

    public ArrayList<Integer> addSubTaskToList(Integer id) {
        subTaskList.add(id);
        return subTaskList;
    }

    public void deleteSubTaskFromList(SubTask subTask) {
        subTaskList.remove(subTask.getId());
    }

    public void deleteAllSubTasksFromList() {
        subTaskList.clear();
    }

}
