package ru.yandex.praktikum.server;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.endpoints.EndPoint;
import ru.yandex.praktikum.managers.HttpTaskManager;
import ru.yandex.praktikum.managersInterfaces.TaskManager;
import ru.yandex.praktikum.taskTypes.Epic;
import ru.yandex.praktikum.taskTypes.SubTask;
import ru.yandex.praktikum.taskTypes.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpTaskServer {
    private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final int port;
    private final TaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer() {
        this.port = 8080;
        this.taskManager = new HttpTaskManager();
        this.gson = new Gson();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void startServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", port), 0);
        httpServer.createContext("/tasks", new AllTaskHandler());
        httpServer.start();
    }

    private class AllTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            EndPoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_TASKS: {
                    getAllTasksHandler(exchange);
                    break;
                }
                case GET_TASK_BY_ID: {
                    getTaskByIdHandler(exchange);
                    break;
                }
                case GET_ALL_SUBTASKS: {
                    getAllSubTasksHandler(exchange);
                    break;
                }
                case GET_SUBTASK_BY_ID: {
                    getSubTaskByIdHandler(exchange);
                    break;
                }
                case GET_ALL_EPICS: {
                    getAllEpicsHandler(exchange);
                    break;
                }
                case GET_EPIC_BY_ID: {
                    getEpicByIdHandler(exchange);
                    break;
                }
                case GET_EPIC_SUBTASKS: {
                    getEpicSubTasksHandler(exchange);
                    break;
                }
                case GET_HISTORY: {
                    getHistoryHandler(exchange);
                    break;
                }
                case GET_PRIORITIZED_TASKS: {
                    getPrioritizedTasksHandler(exchange);
                    break;
                }
                case DELETE_ALL_TASKS: {
                    deleteAllTasksHandler(exchange);
                    break;
                }
                case DELETE_TASK_BY_ID: {
                    deleteTaskByIdHandler(exchange);
                    break;
                }
                case DELETE_ALL_SUBTASKS: {
                    deleteAllSubTasksHandler(exchange);
                    break;
                }
                case DELETE_SUBTASK_BY_ID: {
                    deleteSubTaskByIdHandler(exchange);
                    break;
                }
                case DELETE_ALL_EPICS: {
                    deleteAllEpicsHandler(exchange);
                    break;
                }
                case DELETE_EPIC_BY_ID: {
                    deleteEpicByIdHandler(exchange);
                    break;
                }
                case POST_TASK: {
                    postTaskHandler(exchange);
                    break;
                }
                case POST_SUBTASK: {
                    postSubTaskHandler(exchange);
                    break;
                }
                case POST_EPIC: {
                    postEpicHandler(exchange);
                    break;
                }
                default:
                    writeResponse(exchange, "Такого метода нет", 404);
            }
        }

        private void getAllTasksHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            List<Task> tasks = manager.getListOfAllTasks();
            String json = gson.toJson(tasks);
            writeResponse(exchange, json, 200);
        }

        private void getTaskByIdHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int taskId = optId.get();
            Task task = manager.getTaskById(taskId);
            String json = gson.toJson(task);
            writeResponse(exchange, json, 200);
        }

        private void getAllSubTasksHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            List<SubTask> subTasks = manager.getListOfAllSubTasks();
            String json = gson.toJson(subTasks);
            writeResponse(exchange, json, 200);
        }

        private void getSubTaskByIdHandler(HttpExchange exchange) throws IOException{
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int subTaskId = optId.get();
            SubTask subTask = manager.getSubTaskById(subTaskId);
            String json = gson.toJson(subTask);
            writeResponse(exchange, json, 200);
        }

        private void getAllEpicsHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            List<Epic> epics = manager.getListOfAllEpics();
            String json = gson.toJson(epics);
            writeResponse(exchange, json, 200);
        }

        private void getEpicByIdHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int epicId = optId.get();
            Epic epic = manager.getEpicById(epicId);
            String json = gson.toJson(epic);
            writeResponse(exchange, json, 200);
        }

        private void getEpicSubTasksHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int epicId = optId.get();
            Epic epic = manager.getEpicById(epicId);
            List<SubTask> subTaskList = manager.getAllEpicSubTasks(epic);
            String json = gson.toJson(subTaskList);
            writeResponse(exchange, json, 200);
        }

        private void getHistoryHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            List<Task> history = manager.getHistory();
            String json = gson.toJson(history);
            writeResponse(exchange, json, 200);
        }

        private void getPrioritizedTasksHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            String json = gson.toJson(prioritizedTasks);
            writeResponse(exchange, json, 200);
        }

        private void deleteAllTasksHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            manager.deleteAllTasks();
            writeResponse(exchange, "Все задачи удалены", 200);
        }

        private void deleteTaskByIdHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int taskId = optId.get();
            manager.deleteTaskById(taskId);
            writeResponse(exchange, "Задача с Id " + taskId + " удалена", 200);
        }

        private void deleteAllSubTasksHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            manager.deleteAllSubTasks();
            writeResponse(exchange, "Все подзадачи удалены", 200);
        }

        private void deleteSubTaskByIdHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int subTaskId = optId.get();
            manager.deleteSubTaskById(subTaskId);
            writeResponse(exchange, "Подзадача с Id " + subTaskId + " удалена", 200);
        }

        private void deleteAllEpicsHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            manager.deleteAllEpics();
            writeResponse(exchange, "Все эпики удалены", 200);
        }

        private void deleteEpicByIdHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            Optional<Integer> optId = getTaskId(exchange);
            if (optId.isEmpty()) {
                writeResponse(exchange, "Задачи с таким id не существует", 400);
                return;
            }

            int epicId = optId.get();
            manager.deleteEpicById(epicId);
            writeResponse(exchange, "Эпик с Id " + epicId + " удалена", 200);
        }

        private void postTaskHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(body, Task.class);
            manager.createTask(task);
            writeResponse(exchange, "Задача добавлена", 201);
        }

        private void postSubTaskHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            SubTask subTask = gson.fromJson(body, SubTask.class);
            manager.createSubTask(subTask);
            writeResponse(exchange, "Подзадача добавлена", 201);
        }

        private void postEpicHandler(HttpExchange exchange) throws IOException {
            TaskManager manager = getTaskManager();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);
            manager.createEpic(epic);
            writeResponse(exchange, "Эпик добавлен", 201);
        }
    }

    private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        private Optional<Integer> getTaskId(HttpExchange exchange) {
                String[] pathParts = exchange.getRequestURI().getPath().split("/");
                String id = pathParts[3];
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(id);
                if (matcher.find()) {
                    id = matcher.group();
                    try {
                        return Optional.of(Integer.parseInt(id));
                    } catch (NumberFormatException exception) {
                        return Optional.empty();
                    }
                } else {
                    return Optional.empty();
                }
            }

        private EndPoint getEndpoint(String requestPath, String requestMethod) {
            String[] pathParts = requestPath.split("/");

            if (pathParts.length == 3 && pathParts[2].equals("task")) {
                if (requestMethod.equals("GET")) {
                    return EndPoint.GET_ALL_TASKS;
                }
                if (requestMethod.equals("DELETE")) {
                    return EndPoint.DELETE_ALL_TASKS;
                }
                if (requestMethod.equals("POST")) {
                    return EndPoint.POST_TASK;
                }
            }

            if (pathParts.length == 4 && pathParts[2].equals("task")) {
                if (requestMethod.equals("GET")) {
                    return EndPoint.GET_TASK_BY_ID;
                }
                if(requestMethod.equals("DELETE")) {
                    return EndPoint.DELETE_TASK_BY_ID;
                }
            }

            if (pathParts.length == 3 && pathParts[2].equals("subtask")) {
                if (requestMethod.equals("GET")) {
                    return EndPoint.GET_ALL_SUBTASKS;
                }
                if (requestMethod.equals("DELETE")) {
                    return EndPoint.DELETE_ALL_SUBTASKS;
                }
                if (requestMethod.equals("POST")) {
                    return EndPoint.POST_SUBTASK;
                }
            }

            if (pathParts.length == 4 && pathParts[2].equals("subtask")) {
                if (requestMethod.equals("GET")) {
                    return EndPoint.GET_SUBTASK_BY_ID;
                }
                if (requestMethod.equals("DELETE")) {
                    return EndPoint.DELETE_SUBTASK_BY_ID;
                }
            }

            if (pathParts.length == 3 && pathParts[2].equals("epic")) {
                if (requestMethod.equals("GET")) {
                    return EndPoint.GET_ALL_EPICS;
                }
                if (requestMethod.equals("POST")) {
                    return EndPoint.POST_EPIC;
                }
                if (requestMethod.equals("DELETE")) {
                    return EndPoint.DELETE_ALL_EPICS;
                }
            }

            if (pathParts.length == 4 && pathParts[2].equals("epic")) {
                if (requestMethod.equals("GET")) {
                    return EndPoint.GET_EPIC_BY_ID;
                }
                if (requestMethod.equals("DELETE")) {
                    return EndPoint.DELETE_EPIC_BY_ID;
                }
            }

            if(pathParts.length == 5 && pathParts[3].equals("epic")) {
                return EndPoint.GET_EPIC_SUBTASKS;
            }

            if(pathParts.length == 3 && pathParts[2].equals("history")) {
                return EndPoint.GET_HISTORY;
            }

            if(pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return EndPoint.GET_PRIORITIZED_TASKS;
            }

            return EndPoint.UNKNOWN;
        }
    }




