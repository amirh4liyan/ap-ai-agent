package edu.sharif;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Values;

import java.io.FileReader;
import java.io.IOException;
import java.security.Signature;
import java.util.List;

public class Neo4jHelper {

    private final String USER = "neo4j";
    private final String PASSWORD = "12345678";
    private String URL = "bolt://localhost:7687";

    private Driver driver;
    private CSVReader reader;
    private List<String[]> rows;

    public Neo4jHelper() {
        // Connect to Neo4j Database
        driver = GraphDatabase.driver(URL, AuthTokens.basic(USER, PASSWORD));
    }

    public void readDataFromCSV(String FILENAME) {
        try {
            reader = new CSVReader(new FileReader(FILENAME));
            rows = reader.readAll();
            reader.close();
            String msg = "[Neo4jHelper-LOG]: Read " + rows.size() + " rows from " + FILENAME;
            System.out.println(msg);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
    private void insertNode(String[] items) {
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
    public void insertAllNodes() {
        rows.removeFirst(); // remove first row since it isn't professor
        for (String[] row : rows) {
            insertNode(row);
        }
    }

    private void setURL(String URL) {
        this.URL = URL;
    }
    private String getURL() {
        return URL;
    }
}
