package ru.yandex.praktikum.server;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8078;
    private final String url;
    private final String token;

    public KVTaskClient() {
        this.url = "http://localhost:" + PORT;
        this.token = register();
    }

    public void put(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        HttpResponse<String> response;
        try {
           response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new HttpRetryException("Код ответа не соответствует ожидаемому. Ожидаемый код ответа 200", 400);
            }
        }
        catch (IOException | InterruptedException e) {
            System.out.println("Неудачное сохранение! Причина " + e.getMessage());
        }
    }

    public String load(String key) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response;

        try {
           response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new HttpRetryException("Код ответа не соответствует ожидаемому. Ожидаемый код ответа 200", 400);
            }
           return response.body();
        }
        catch (IOException | InterruptedException e) {
            System.out.println("Неудачная загрузка! Причина " + e.getMessage());
        }
        return null;
    }

    private String register() {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new HttpRetryException("Код ответа не соответствует ожидаемому.  Ожидаемый код ответа 200", 400);
            }
            return response.body();
        }
        catch (IOException | InterruptedException e) {
            System.out.println("Не удачная попытка регистрации! Причина " + e.getMessage());
        }
        return null;
    }
}
