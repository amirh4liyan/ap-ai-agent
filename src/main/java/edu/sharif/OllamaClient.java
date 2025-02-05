package edu.sharif;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OllamaClient extends AbstractLLMClient {

    private static OllamaClient instance;

    private OllamaClient() {
        super("", "http://localhost:11434/api/generate");
    }

    public static OllamaClient getInstance() {
        if (instance == null) {
            instance = new OllamaClient();
        }
        return instance;
    }

    @Override
    public void generateCypherQuery(String userInput) {
        try {
            String userPrompt = String.format(
                    "USER_PROMPT='%s' Write a Cypher query based on USER_PROMPT that retrieves professors from the database. " +
                            "They should be matched (use CONTAIN) based on p.teaching or p.research_interest field. " +
                            "Only return the Cypher query, no extra text, explanations, or comments. " +
                            "Example 1: MATCH (p:Professor) WHERE p.teaching CONTAINS '' OR p.research_interests CONTAINS '' " +
                            "RETURN p ORDER BY p.collaboration_start_year ASC",
                    escapeJson(userInput)
            );

            String jsonPayload = String.format(
                    "{\"model\":\"codellama\", \"prompt\":\"%s\", \"stream\": false}",
                    userPrompt
            );

            // چاپ jsonPayload برای بررسی صحت آن
            System.out.println(jsonPayload);

            // ارسال درخواست به Ollama API
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
            // پردازش پاسخ و استخراج فیلد "response"
            JsonNode jsonNode = this.parseJson(response.body());

            // استخراج محتوای فیلد "response"
            if (jsonNode.has("response")) {
                super.query = jsonNode.get("response").asText().trim().replace("\n", " ");
                super.query = super.query.replace("`", "").replace("\n", " ").trim();
            } else {
                super.query = "Error: 'response' field is missing.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            super.query = "Error generating query";
        }
    }
}
