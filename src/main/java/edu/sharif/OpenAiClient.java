package edu.sharif;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenAiClient extends AbstractLLMClient {

    private static OpenAiClient instance;

    private OpenAiClient() {
        super("",
                "https://api.openai.com/v1/chat/completions"
        );
    }

    public static OpenAiClient getInstance() {
        if (instance == null) {
            instance = new OpenAiClient();
        }
        return instance;
    }

    @Override
    public void generateCypherQuery(String userInput) {
        try {
            // آماده کردن داده‌های ورودی
            String prompt1 = String.format("USER_REQUEST='%s'", userInput);
            String jsonPayload = getString(prompt1);

            // ارسال درخواست به OpenAI API
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIURL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + APIKEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // پردازش پاسخ و استخراج محتوای بخش content
            JsonNode jsonNode = this.parseJson(response.body());

            JsonNode choicesNode = jsonNode.get("choices");
            JsonNode firstChoiceNode = choicesNode.get(0);
            JsonNode messageNode = firstChoiceNode.get("message");

            super.query = messageNode.get("content").asText().trim().replace("`", "");

        } catch (Exception e) {
            e.printStackTrace();
            super.query = "Error generating query";
        }
    }

    private String getString(String prompt1) {
        String prompt2 = "Please create a Cypher query based on USER_REQUEST for a graph in Neo4j where nodes represent Professor. Each Professor node has the following properties: professor_id (which is unique and should be used to identify the professor), first_name, last_name, email, department, teaching, collaboration_start_year, research_interests, and publications_file. The professor_id should be used as the unique identifier for each node, and the query should create nodes for Professors with the given properties. IMPORTANT: When filtering based on the request, the query should not require an exact match for fields like 'teaching' or 'research_interests', but should instead search for partial matches (e.g., using the CONTAINS operator) for the keyword. Additionally, the results should be sorted by collaboration_start_year in ascending order (i.e., from the smallest number upward)";
        return "{\n" +
                "  \"model\": \"gpt-4\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a Cypher query generator. Only provide Cypher queries in a single line, without any line breaks or extra formatting.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt1 + prompt2 + "\"}\n" +
                "  ],\n" +
                "  \"max_tokens\": 200\n" +
                "}";
    }

}
