package ru.yandex.praktikum.taskTypes;

import ru.yandex.praktikum.statuses.Statuses;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String title, String description, int id, Statuses status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, Statuses status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
