package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.managersInterfaces.TaskManager;
import ru.yandex.praktikum.statuses.Statuses;
import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;
import ru.yandex.praktikum.taskTypes.TaskTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskManagerTest <T extends TaskManager> {

    T manager;

    @Test
    public void createAndGetByIdTaskTest() {
        Task task = new Task(TaskTypes.TASK, "Task", "TaskDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60);
        manager.createTask(task);
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    public void createAndGetByIdSubTaskTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        SubTask subTask = new SubTask(TaskTypes.SUB_TASK, "SubTask", "SubTaskDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        assertEquals(subTask, manager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void createAndGetByIdEpicTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60);
        manager.createEpic(epic);
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void shouldNotEmptyListOfAllTasksTest() {
        Task task1 = new Task(TaskTypes.TASK, "Task1", "TaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        Task task2 = new Task(TaskTypes.TASK, "Task2", "TaskDesc2", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        Task task3 = new Task(TaskTypes.TASK, "Task3", "TaskDesc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        List<Task> listOfAllTasks = manager.getListOfAllTasks();

        List<Task> taskListForTest = new ArrayList<>();
        taskListForTest.add(task1);
        taskListForTest.add(task2);
        taskListForTest.add(task3);

        assertNotNull(listOfAllTasks);
        assertEquals(listOfAllTasks, taskListForTest);
    }

    @Test
    public void shouldEmptyListOfAllTasksTest() {
        List<Task> listOfAllTasks = manager.getListOfAllTasks();
        assertTrue(listOfAllTasks.isEmpty());
    }

    @Test
    public void shouldNotEmptyListOfAllSubTasksTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "SubTask1", "SubTaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "SubTask2", "SubTaskDesc2", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "SubTask3", "SubTaskDesc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        manager.createEpic(epic);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);

        List<SubTask> subTaskList = manager.getListOfAllSubTasks();

        List<SubTask> subTaskListForTest = new ArrayList<>();

        subTaskListForTest.add(subTask1);
        subTaskListForTest.add(subTask2);
        subTaskListForTest.add(subTask3);

        assertNotNull(subTaskList);
        assertEquals(subTaskList, subTaskListForTest);
    }

    @Test
    public void shouldEmptyListOfAllSubTasksTest() {
        List<SubTask> listOfAllSubTasks = manager.getListOfAllSubTasks();
        assertTrue(listOfAllSubTasks.isEmpty());
    }

    @Test
    public void shouldNotEmptyListOfAllEpicsTest() {
        List<Epic> epicList = manager.getListOfAllEpics();
        assertTrue(epicList.isEmpty());
    }

    @Test
    public void shouldEmptyListOfAllEpicsTest() {
        List<Epic> epicList = manager.getListOfAllEpics();
        assertTrue(epicList.isEmpty());
    }

    @Test
    public void taskCanUpgradedTest() {
        Task task = new Task(TaskTypes.TASK, "Task1", "TaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createTask(task);
        Task upgrade = manager.upgradeTask(new Task(1, TaskTypes.TASK, "UpTask", "UpDesc", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 ));
        assertEquals(task, upgrade);
    }

    @Test
    public void returnNullIfTaskNotUpgradedTest() {
        Task upgrade = new Task(100, TaskTypes.TASK, "UpTask", "UpDesc", Statuses.DONE,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        assertNull(manager.upgradeTask(upgrade));
    }

    @Test
    public void subTaskCanUpgradedTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        SubTask subTask = new SubTask(TaskTypes.SUB_TASK, "Sub", "Desc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        SubTask upgrade = new SubTask(2, TaskTypes.SUB_TASK, "UpSubTask", "UpDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60,1);
        assertEquals(upgrade, manager.upgradeSubTask(upgrade));
    }

    @Test
    public void returnNullIfSubTaskNotUpgradedTest() {
        SubTask upgrade = new SubTask(100, TaskTypes.SUB_TASK, "UpTask", "Desc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        assertNull(manager.upgradeSubTask(upgrade));
    }

    @Test
    public void epicCanUpgradedTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createEpic(epic);
        Epic upgrade = new Epic(1, TaskTypes.EPIC, "UpEpic", "UpEpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60);
        assertEquals(upgrade, manager.upgradeEpic(upgrade));
    }

    @Test
    public void returnNullIfEpicNotUpgradedTest() {
        Epic upgrade = new Epic(1, TaskTypes.EPIC, "UpEpic", "UpEpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        assertNull(manager.upgradeEpic(upgrade));
    }

    @Test
    public void createAndDeleteOneTaskTest() {
        Task task = new Task(TaskTypes.TASK, "Task1", "TaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createTask(task);
        manager.deleteTaskById(task.getId());
        assertTrue(manager.getListOfAllTasks().isEmpty());
    }

    @Test
    public void createAndDeleteOneSubTaskTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        SubTask subTask = new SubTask(TaskTypes.SUB_TASK, "Sub", "Desc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.deleteSubTaskById(subTask.getId());
        assertTrue(manager.getListOfAllSubTasks().isEmpty());
        assertTrue(manager.getAllEpicSubTasks(epic).isEmpty());
    }

    @Test
    public void createAndDeleteOneEpicTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createEpic(epic);
        manager.deleteEpicById(epic.getId());
        assertTrue(manager.getListOfAllEpics().isEmpty());
    }

    @Test
    public void createAndDeleteAllTasksTest() {
        Task task1 = new Task(TaskTypes.TASK, "Task1", "TaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        Task task2 = new Task(TaskTypes.TASK, "Task2", "TaskDesc2", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        Task task3 = new Task(TaskTypes.TASK, "Task3", "TaskDesc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.deleteAllTasks();
        assertTrue(manager.getListOfAllTasks().isEmpty());
    }

    @Test
    public void createAndDeleteAllSubTasksTest() {
        Epic epic = new Epic(TaskTypes.EPIC, "Epic", "EpicDesc", Statuses.NEW, LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "Sub1", "Desc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "Sub2", "Desc2", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "Sub3", "Desc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        manager.createEpic(epic);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);
        manager.deleteAllSubTasks();
        assertTrue(manager.getListOfAllTasks().isEmpty());
    }

    @Test
    public void createAndDeleteAllEpicsAndSubTasksTest() {
        Epic epic1 = new Epic(TaskTypes.EPIC, "Epic1", "EpicDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        Epic epic2 = new Epic(TaskTypes.EPIC, "Epic2", "EpicDesc2", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        SubTask subTask1 = new SubTask(TaskTypes.SUB_TASK, "Sub1", "Desc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask2 = new SubTask(TaskTypes.SUB_TASK, "Sub2", "Desc2", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        SubTask subTask3 = new SubTask(TaskTypes.SUB_TASK, "Sub3", "Desc3", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60, 1);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);
        manager.deleteAllEpics();
        assertTrue(manager.getListOfAllEpics().isEmpty());
        assertTrue(manager.getListOfAllSubTasks().isEmpty());
    }

    @Test
    public void getNotEmptyHistoryListTest() {
        Task task1 = new Task(TaskTypes.TASK, "Task1", "TaskDesc1", Statuses.NEW,
                LocalDateTime.of(2023, 10, 11, 21, 50), 60 );
        manager.createTask(task1);
        manager.getTaskById(task1.getId());
        assertFalse(manager.getHistory().isEmpty());
    }

    @Test
    public void getEmptyHistoryListTest() {
        assertTrue(manager.getHistory().isEmpty());
    }

}
