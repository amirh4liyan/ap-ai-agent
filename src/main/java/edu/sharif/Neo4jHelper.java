package edu.sharif;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import java.util.Map;

public class Neo4jHelper {

    private static Neo4jHelper instance;
    private final Driver driver;
    private final String USER = "neo4j";
    private final String PASSWORD = "12345678";
    private String url = "bolt://localhost:7687";

    private Neo4jHelper() {
        // Connect to Neo4j Database
        driver = GraphDatabase.driver(url, AuthTokens.basic(USER, PASSWORD));
    }

    public static Neo4jHelper getInstance() {
        if (instance == null) {
            instance = new Neo4jHelper();
        }
        return instance;
    }

    // Should Stay Private
    private void createNode(GraphEntity graphEntity) {
        String nodeLabel = graphEntity.getLabel();
        String uniqueProperty = graphEntity.getUniqueKey();
        String query = "CREATE (n:" +
                nodeLabel +
                " {" +
                uniqueProperty +
                ": $props." + uniqueProperty + "}) " +
                "SET n += $props";
        driver.session().writeTransaction(tx -> tx.run(query, Values.parameters("props", graphEntity.getMap())));
    }
    public void mergeNode(GraphEntity graphEntity) {
        String nodeLabel = graphEntity.getLabel();
        String uniqueProperty = graphEntity.getUniqueKey();
        String query = "MERGE (n:" +
                nodeLabel +
                " {" +
                uniqueProperty +
                ": $props." +
                uniqueProperty +
                "}) ON CREATE SET n += $props";
        driver.session().writeTransaction(tx -> tx.run(query, Values.parameters("props", graphEntity.getMap())));
    }
    public Map<String, Object> getNode(GraphEntity graphEntity) {
        String nodeLabel = graphEntity.getLabel();
        String uniqueProperty = graphEntity.getUniqueKey();
        String query = "MATCH (n:" + nodeLabel + " {" + uniqueProperty + ": $VALUE}) RETURN n";

        return driver.session().readTransaction(tx -> {
            Record record = tx.run(query, Values.parameters(
                    "VALUE", graphEntity.getUniqueValue()
            )).single();
            Node node = record.get("n").asNode();
            return node.asMap();
        });
    }
    public void updateNode(GraphEntity graphEntity) {
        String nodeLabel = graphEntity.getLabel();
        String uniqueProperty = graphEntity.getUniqueKey();
        String query = "MERGE (n:" +
                nodeLabel +
                " {" +
                uniqueProperty +
                ": $props." +
                uniqueProperty +
                "}) ON MATCH SET n += $props";
        driver.session().writeTransaction(tx -> tx.run(query, Values.parameters("props", graphEntity.getMap())));
    }
    public void deleteNode(GraphEntity graphEntity) {
        String nodeLabel = graphEntity.getLabel();
        String uniqueProperty = graphEntity.getUniqueKey();
        Object uniqueValue = graphEntity.getUniqueValue();
        Map<String, Object> map = graphEntity.getMap();
        String cypherQuery;
        cypherQuery = String.format("MATCH (n:%s {%s: %s}) DETACH DELETE n", nodeLabel, uniqueProperty, uniqueValue);

        try {
            driver.session().run(cypherQuery);
        } catch (Exception e) {
            System.err.println("[Neo4jHelper-LOG]: Error executing Cypher query: " +
                    e.getMessage());
        }
    }

    private void deleteAllNodes() {
        String query = "MATCH (n) DETACH DELETE n";
        driver.session().run(query);
    }
    public void deleteNodeType(GraphEntity graphEntity) {
        String query = "MATCH (n:" + graphEntity.getLabel() + ") DETACH DELETE n";
        driver.session().run(query);
    }

    public Result execQuery(String query) {
        try {
            return driver.session().run(query);
        } catch (Exception e) {
            System.err.println("[Neo4jHelper-LOG]: Error executing Cypher query: " + e.getMessage());
            return null;
        }
    }

    private void setUrl(String url) {
        this.url = url;
    }
    private String getUrl() {
        return url;
    }
}