public class SubTask extends Task {

    int epicId;

    public SubTask(String title, String description, int id, String status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, String status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}
