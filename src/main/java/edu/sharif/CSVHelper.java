package edu.sharif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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
                    .map(data -> {
                        String[] cleanedData = Arrays.stream(data)
                                .map(s -> s.replaceAll("^\"|\"$", ""))
                                .toArray(String[]::new);
                        return new Professor(
                                Integer.parseInt(cleanedData[0]),
                                cleanedData[1],
                                cleanedData[2],
                                cleanedData[3],
                                cleanedData[4],
                                cleanedData[5],
                                cleanedData[6],
                                cleanedData[7],
                                cleanedData[8]
                        );
                    })
                    .collect(Collectors.toList());
        }
    }
}
