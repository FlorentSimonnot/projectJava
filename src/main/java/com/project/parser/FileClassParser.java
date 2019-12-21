package com.project.parser;

import com.project.files.FileClass;
import com.project.files.FilesCollector;

import java.util.Objects;

public class FileClassParser implements FileParserInterface {

    @Override
    public FilesCollector parseMyFile(String name) throws ParserException {
        Objects.requireNonNull(name);
        var collector = new FilesCollector();
        if(!name.endsWith(".class"))
            throw new ParserException();
        collector.addFile(new FileClass(name));
        return collector;
    }
}
