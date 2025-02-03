package edu.sharif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CSVHelper {

    private static CSVHelper instance;

    private CSVHelper() {
    }

    public static CSVHelper getInstance() {
        if (instance == null) {
            instance = new CSVHelper();
        }
        return instance;
    }

    public List<Professor> readProfessors(String FILENAME) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            return reader.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(data ->
                            new Professor (
                                Integer.parseInt(data[0]),
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                data[5],
                                data[6],
                                data[7],
                                data[8]
                            )
                    )
                    .collect(Collectors.toList());
        }
    }
}
