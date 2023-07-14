package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.managers.InMemoryTaskManager;
import ru.yandex.praktikum.statuses.Statuses;
import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.TaskTypes;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicTests {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Epic epic;

    @BeforeEach
    public void createEpic() {
        epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW);
    }

    @Test
    public void epicSubTaskListEmptyTest() {
        inMemoryTaskManager.createEpic(epic);
        List<SubTask> subTaskList = inMemoryTaskManager.getAllEpicSubTasks(epic);
        assertTrue(subTaskList.isEmpty());
        assertEquals(Statuses.NEW, epic.getStatus());
    }

    @Test
    public void allEpicSubTasksHaveStatusNewTest() {
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "SubTask1", "SubTaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "SubTask2", "SubTaskDesc2", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "SubTask3", "SubTaskDesc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        assertEquals(Statuses.NEW, epic.getStatus());
    }

    @Test
    public void allEpicSubTasksHaveStatusDneTest() {
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "SubTask1", "SubTaskDesc1", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "SubTask2", "SubTaskDesc2", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60,1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "SubTask3", "SubTaskDesc3", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60,1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        assertEquals(Statuses.DONE, epic.getStatus());
    }

    @Test
    public void allEpicSubTasksHaveStatusNewAndDoneTest() {
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "SubTask1", "SubTaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "SubTask2", "SubTaskDesc2", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "SubTask3", "SubTaskDesc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        assertEquals(Statuses.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void allEpicSubtasksHaveStatusInProgressTest() {
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "SubTask1", "SubTaskDesc1", Statuses.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "SubTask2", "SubTaskDesc2", Statuses.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "SubTask3", "SubTaskDesc3", Statuses.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        assertEquals(Statuses.IN_PROGRESS, epic.getStatus());
    }
}
