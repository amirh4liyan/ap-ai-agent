package edu.sharif;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.types.Node;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    final static String FILENAME = "/home/amirhossein/IdeaProjects/ap-ai-agent/data/profs.csv";
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
        //String prompt = "i want take Data Structures course this term, which professor you suggest?";
        String prompt = "";
        System.out.print("[in-str]: ");
        Scanner sc = new Scanner(System.in);
        prompt = sc.nextLine();

        var llm = OllamaClient.getInstance();
        llm.generateCypherQuery(prompt);
        String response = llm.getQuery();

        System.out.println("Extracted Content: " + response);

        Result result = Neo4jHelper.getInstance().execQuery(response);

        if (result != null) {
            while (result.hasNext()) {
                Record record = result.next();

                if (record.containsKey("p")) {
                    Node node = record.get("p").asNode();
                    String first_name = node.get("first_name").asString();
                    String last_name = node.get("last_name").asString();
                    String email = node.get("email").asString();
                    System.out.println(first_name + "    " + last_name + "    " + email);
                } else {
                    System.out.println("No 'p' field in this record.");
                }
            }
        } else {
            System.err.println("Query execution returned null.");
        }
    }
}