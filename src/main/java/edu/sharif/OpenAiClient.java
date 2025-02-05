package edu.sharif;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenAiClient extends AbstractLLMClient {

    private static OpenAiClient instance;

    private OpenAiClient() {
        super("",
                "https://api.openai.com/v1/completions"
        );
    }

    public static OpenAiClient getInstance() {
        if (instance == null) {
            instance = new OpenAiClient();
        }
        return instance;
    }

    @Override
    public String generateCypherQuery(String userInput) {
        try {
             String prompt1 = String.format("USER_REQUEST='%s'", userInput);
            String jsonPayload = getString(prompt1);

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
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating query";
        }
    }

    private String getString(String prompt1) {
        String prompt2 = " Please create a Cypher query based on USER_REQUEST for a graph in Neo4j where nodes represent Professors. Each Professor node has the following properties: professor_id (which is unique and should be used to identify the professor), first_name, last_name, email, department, teaching, collaboration_start_year, research_interests, and publications_file. The professor_id should be used as the unique identifier for each node, and the query should create nodes for Professors with the given properties. IMPORTANT: When filtering based on the request, the query should not require an exact match for fields like 'teaching' or 'research_interests', but should instead search for partial matches (e.g., using the CONTAINS operator) for the keyword Computer Vision . Additionally, the results should be sorted by collaboration_start_year in ascending order (i.e., from the smallest number upward). No additional explanations are needed, just the Cypher query.";
        return "{\"model\":\"gpt-3.5-turbo\", \"prompt\":\"" + prompt1 + prompt2 + "\", \"max_tokens\":200}";
    }

}
