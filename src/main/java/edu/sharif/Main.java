package edu.sharif;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.types.Node;

import java.io.IOException;
import java.util.List;

public class Main {

    final static String FILENAME = "/home/amirhossein/Desktop/tmp2.csv";
    static List<Professor> professors;

    public static void main(String[] args) {
        // Helper classes Implemented Using Singleton Design Pattern
        var neo = Neo4jHelper.getInstance();
        var csv = CSVHelper.getInstance();

        try {
            professors = csv.readProfessors(FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //String prompt = "USER_PROMPT=\"i want to take Data Structures course this term, which professor you suggest?\" Write a Cypher query based on USER_PROMPT that retrieves professors from the database. they should be match (use CONTAIN) based on n.teaching or n.research_interest field. Only return the Cypher query, no extra text, explanations, or comments. Example 1: MATCH (n:Professor) WHERE n.teaching CONTAINS '' OR n.research_interests CONTAINS ''  RETURN n ORDER BY n.collaboration_start_year ASC\"";
        String prompt = "i want take Data Structures course this term, which professor you suggest?";


        var llm = OpenAiClient.getInstance();
        llm.generateCypherQuery(prompt);
        String response = llm.getQuery();
        //String response = "MATCH (p:Professor) WHERE p.teaching CONTAINS 'Data Structures' RETURN p ORDER BY p.collaboration_start_year ASC";

        //String response = "{ \"id\": \"chatcmpl-AxalNTxFT7PWZJ0J7oeR3JdpBIHYv\", \"object\": \"chat.completion\", \"created\": 1738766033, \"model\": \"gpt-4-0613\", \"choices\": [ { \"index\": 0, \"message\": { \"role\": \"assistant\", \"content\": \"MATCH (p:Professor) WHERE p.teaching CONTAINS 'Data Structures' RETURN p.first_name, p.last_name, p.email ORDER BY p.collaboration_start_year ASC\", \"refusal\": null }, \"logprobs\": null, \"finish_reason\": \"stop\" } ], \"usage\": { \"prompt_tokens\": 229, \"completion_tokens\": 37, \"total_tokens\": 266, \"prompt_tokens_details\": { \"cached_tokens\": 0, \"audio_tokens\": 0 }, \"completion_tokens_details\": { \"reasoning_tokens\": 0, \"audio_tokens\": 0, \"accepted_prediction_tokens\": 0, \"rejected_prediction_tokens\": 0 } }, \"service_tier\": \"default\", \"system_fingerprint\": null }";

        System.out.println("Extracted Content: " + response);

        Result result = Neo4jHelper.getInstance().execQuery(response);

        if (result != null) {
            while (result.hasNext()) {
                Record record = result.next();

                if (record.containsKey("p")) {
                    Node node = record.get("p").asNode();
                    String name = node.get("last_name").asString();
                    System.out.println("Last Name: " + name);
                } else {
                    System.out.println("No 'p' field in this record.");
                }
            }
        } else {
            System.err.println("Query execution returned null.");
        }
    }
}