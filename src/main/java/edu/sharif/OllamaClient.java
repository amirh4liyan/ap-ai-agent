package edu.sharif;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OllamaClient extends AbstractLLMClient {

    private static OllamaClient instance;

    private OllamaClient() {
        super("", "http://localhost:11434/run");
    }

    public static OllamaClient getInstance() {
        if (instance == null) {
            instance = new OllamaClient();
        }
        return instance;
    }

    @Override
    public String generateCypherQuery(String userInput) {
        try {
            String jsonPayload = "{\"model\":\"llama3.2\", \"prompt\":\"" + escapeJson(userInput) + "\"}";

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending request";
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}
