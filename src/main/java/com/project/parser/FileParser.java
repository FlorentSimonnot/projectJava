package com.project.parser;

import com.project.files.FilesCollector;

import java.util.Objects;
import java.util.regex.Pattern;

public class FileParser {


    public static FilesCollector parseFile(String name, FileFactory factory){
        Objects.requireNonNull(factory);
        Objects.requireNonNull(name);
        for(var entry : factory.map.entrySet()) {
            if (Pattern.matches(entry.getKey(), name)) {
                try {
                    return entry.getValue().parseMyFile(name);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Please verify your file extension");
                }
            }
        }
        throw new IllegalStateException();
    }
}
