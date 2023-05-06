import java.util.ArrayList;

public class Task {

    public String title;
    public String description;
    public int id;
    public String status;

    static ArrayList<Task> taskList = new ArrayList<>();

    public Task(String title, String description, int id, String status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public static ArrayList<Task> createTask(String title, String description, int id) {
        Task task = new Task(title, description, id, "NEW");
        taskList.add(task);
        return taskList;
    }

    public static void getListOfAllTasks() {
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println(taskList.get(i));
        }
    }

    public static void deleteAllTasks() {
        taskList.clear();
        System.out.println("Удалено");
    }

    public static Task getTaskById(int id) {
        for (Task task : taskList) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public static boolean deleteTaskById(int id) {
        for (Task task : taskList) {
            if (task.getId() == id) {
                taskList.remove(task);
                return true;
            }
        }
        return false;
    }

    public static boolean upgradeTaskById(String title, String description, int id, String status) {
        for (Task task : taskList) {
            if (task.getId() == id) {
            task.setTitle(title);
            task.setDescription(description);
            task.setId(id);
            task.setStatus(status);
            return true;
            }
        }
        System.out.println("Такой задачи нет");
        return false;
    }
        @Override
        public String toString () {
            return "Task{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status='" + status + '\'' +
                    '}';
        }

        public String getTitle () {
            return title;
        }

        public String getDescription () {
            return description;
        }

        public int getId () {
            return id;
        }

        public String getStatus () {
            return status;
        }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
