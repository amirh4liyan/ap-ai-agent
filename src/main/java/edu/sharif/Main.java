package edu.sharif;

import java.io.IOException;
import java.util.List;

public class Main {

    final static String FILENAME = "/home/amirhossein/Desktop/tmp2.csv";
    static List<Professor> professors;

    public static void main(String[] args) {
        // Helper classes Implemented Using Singleton Design Pattern
        Neo4jHelper neo = Neo4jHelper.getInstance();
        CSVHelper csv = CSVHelper.getInstance();


        try {
            professors = csv.readProfessors(FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String prompt = "i want to take algorithm course this term, which professor you suggest?";

        OpenAiClient gpt = OpenAiClient.getInstance();
        String response = gpt.generateCypherQuery(prompt);
        System.out.println(response);
    }
}