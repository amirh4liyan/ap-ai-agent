package edu.sharif;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.neo4j.driver.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Neo4jHelper {

    private static Neo4jHelper instance;

    private final String USER = "neo4j";
    private final String PASSWORD = "12345678";
    private String URL = "bolt://localhost:7687";

    private final Driver driver;
    private List<String[]> rows;

    private Neo4jHelper() {
        // Connect to Neo4j Database
        driver = GraphDatabase.driver(URL, AuthTokens.basic(USER, PASSWORD));
    }

    public static Neo4jHelper getInstance() {
        if (instance == null) {
            instance = new Neo4jHelper();
        }
        return instance;
    }

    public void readDataFromCSV(String FILENAME) {
        try (CSVReader reader = new CSVReader(new FileReader(FILENAME))) {
            rows = reader.readAll();
            String msg = "[Neo4jHelper-LOG]: Read " + rows.size() + " rows from " + FILENAME;
            System.out.println(msg);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAllNodes() {
        rows.removeFirst(); // remove first row since it isn't professor
        for (String[] row : rows) {
            insertNode(row);
        }
    }
    private void insertNodeType(String nodeType) {

    }
    private void insertNode(String[] items) {
        // TODO: handle empty or invalid input
        int professor_id = Integer.parseInt(items[0]);
        String first_name = items[1];
        String last_name = items[2];
        String email = items[3];
        String department = items[4];
        String teaching = items[5];
        int collaboration_start_year = Integer.parseInt(items[6]);
        String research_interests = items[7];
        String publications_file = items[8];

        String cypherQuery = "CREATE (n:Professor {" +
                "professor_id: $professor_id," +
                "first_name: $first_name," +
                "last_name: $last_name," +
                "email: $email," +
                "department: $department," +
                "teaching: $teaching," +
                "collaboration_start_year: $collaboration_start_year," +
                "research_interests: $research_interests," +
                "publications_file: $publications_file})";

        driver.session().run(
                cypherQuery,
                Values.parameters(
                        "professor_id", professor_id,
                        "first_name", first_name,
                        "last_name", last_name,
                        "email", email,
                        "department", department,
                        "teaching", teaching,
                        "collaboration_start_year", collaboration_start_year,
                        "research_interests", research_interests,
                        "publications_file", publications_file
                )
        );
    }

    private void deleteAllNodes() {
        String query = "MATCH (n) DETACH DELETE n";
        driver.session().run(query);
    }
    public void deleteNode(String nodeType, String property, String value) {
        String cypherQuery;
        if (value.matches("\\d+"))
            cypherQuery = String.format("MATCH (n:%s {%s: %s}) DETACH DELETE n", nodeType, property, value);
        else
            cypherQuery = String.format("MATCH (n:%s {%s: '%s'}) DETACH DELETE n", nodeType, property, value);

        try {
            driver.session().run(cypherQuery);
        } catch (Exception e) {
            System.err.println("[Neo4jHelper-LOG]: Error executing Cypher query: " +
                    e.getMessage());
        }
    }

    private void setURL(String URL) {
        this.URL = URL;
    }
    private String getURL() {
        return URL;
    }
}