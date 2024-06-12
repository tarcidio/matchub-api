package com.matchub.api.matchub_api.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class JsonExtractor {
    private Path directoryPath;
    private ObjectMapper mapper;

    public JsonExtractor(String directoryPath) {
        this.directoryPath = Paths.get(directoryPath);
        this.mapper = new ObjectMapper(); // Create an instance of ObjectMapper
    }

    public List<AbstractMap.SimpleEntry<Long, String>> extractChampionsInfo() {
        List<AbstractMap.SimpleEntry<Long, String>> infoList = new ArrayList<>();

        // Using try-with-resources to ensure that the DirectoryStream is closed
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, path -> path.toString().endsWith(".json"))) {
            StreamSupport.stream(stream.spliterator(), false)
                    .forEach(filePath -> {
                        try {
                            // Parse the JSON file using Jackson
                            JsonNode rootNode = mapper.readTree(filePath.toFile());

                            // Get the json file name
                            String fileName = filePath.getFileName().toString();

                            // Get the champion name
                            String nameChampion = fileName.substring(0, fileName.lastIndexOf(".json"));
                            // Get the champion id
                            String idNumber = rootNode.path("data").path(nameChampion).path("key").asText();

                            // Add the pair to the list
                            infoList.add(new AbstractMap.SimpleEntry<>(Long.valueOf(idNumber), nameChampion));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return infoList;
    }
}