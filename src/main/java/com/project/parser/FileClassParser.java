package com.project.parser;

import com.project.files.FileClass;
import com.project.files.FilesCollector;

import java.util.Objects;

public class FileClassParser implements FileParserInterface {

    @Override
    public FilesCollector parseMyFile(String name) throws IllegalArgumentException {
        var collector = new FilesCollector();
        if(!name.endsWith(".class"))
            throw new IllegalArgumentException();
        collector.addFile(new FileClass(Objects.requireNonNull(name)));
        return collector;
    }
}
