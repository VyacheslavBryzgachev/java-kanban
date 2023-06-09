package ru.yandex.praktikum.managers;

import ru.yandex.praktikum.Node;
import ru.yandex.praktikum.managersInterfaces.HistoryManager;
import ru.yandex.praktikum.taskTypes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> receivedTasks = new HashMap<>();
    private Node<Task> tail;
    private Node<Task> head;

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkTask(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(receivedTasks.remove(id));
    }

    private void linkTask(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        receivedTasks.put(task.getId(), newNode);
        if(head == null) {
            head = newNode;
        }
        else
            oldTail.next = newNode;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if(node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            if(head == node && tail == node) {
                head = null;
                tail = null;
            }
            else if (head == node) {
                head = next;
                head.prev = null;
            } else if (tail == node) {
                tail = prev;
                tail.next = null;
            }
            else
                prev.next = next;
                next.prev = prev;
        }
    }
}


