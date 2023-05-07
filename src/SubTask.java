public class SubTask extends Task {

    public SubTask(String title, String description, int id, String status) {
        super(title, description, id, status);
    }

    public SubTask(String title, String description, String status) {
        super(title, description, status);
    }
}
