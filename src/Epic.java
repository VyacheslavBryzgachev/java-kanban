import java.util.ArrayList;

public class Epic extends Task {

    public Epic(String title, String description, int id, String status) {
        super(title, description, id, status);
    }

    public Epic(String title, String description, String status) {
        super(title, description, status);
    }
}
