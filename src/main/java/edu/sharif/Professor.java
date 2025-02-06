package edu.sharif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professor implements GraphEntity {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String teaching;
    private String collaboration_start_year;
    private String research_interests;
    private String publications_file;

    Professor(int id, String firstName, String lastName, String email, String department, String teaching, String collaborationStartYear, String researchInterests, String publicationsFile) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.teaching = teaching;
        collaboration_start_year = collaborationStartYear;
        research_interests = researchInterests;
        publications_file = publicationsFile;
    }

    @Override
    public void sync() {
        Neo4jHelper neo = Neo4jHelper.getInstance();
        System.out.println(neo.getNode(this));
    }

    @Override
    public String getLabel() {
        return "Professor";
    }

    @Override
    public String getUniqueKey() {
        return "professor_id";
    }

    @Override
    public Object getUniqueValue() {
        return id;
    }

    @Override
    public String[] getProperties() {
        return new String[]{
                "professor_id",
                "first_name",
                "last_name",
                "email",
                "department",
                "teaching",
                "collaboration_start_year",
                "research_interests",
                "publications_file"
        };
    }

    @Override
    public List<Object> getValues() {
        List<Object> mixedList = new ArrayList<>();
        mixedList.add(id);
        mixedList.add(firstName);
        mixedList.add(lastName);
        mixedList.add(email);
        mixedList.add(department);
        mixedList.add(teaching);
        mixedList.add(collaboration_start_year);
        mixedList.add(research_interests);
        mixedList.add(publications_file);
        return mixedList;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("professor_id", id);
        map.put("first_name", firstName);
        map.put("last_name", lastName);
        map.put("email", email);
        map.put("department", department);
        map.put("teaching", teaching);
        map.put("collaboration_start_year", collaboration_start_year);
        map.put("research_interests", research_interests);
        map.put("publications_file", publications_file);
        return map;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getTeaching() {
        return teaching;
    }

    public void setTeaching(String teaching) {
        this.teaching = teaching;
    }

    public String getCollaboration_start_year() {
        return collaboration_start_year;
    }

    public String getResearch_interests() {
        return research_interests;
    }

    public void setResearch_interests(String research_interests) {
        this.research_interests = research_interests;
    }

    public String getPublications_file() {
        return publications_file;
    }

}