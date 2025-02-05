package edu.sharif;

import java.net.*;

public abstract class AbstractLLMClient {

    protected String APIKEY = "";
    protected String APIURL = "";
    protected HttpURLConnection connection;
    protected URL url;

    public AbstractLLMClient(String APIKEY, String APIURL) {
        this.APIKEY = APIKEY;
        this.APIURL = APIURL;
    }

    public abstract String generateCypherQuery(String userInput);

    public void main(String[] args) {
        String userInput = "What is the relationship between Ali and Maryam?";

        String cypherQuery = generateCypherQuery(userInput);
        System.out.println("Generated Cypher Query: " + cypherQuery);
    }
}
