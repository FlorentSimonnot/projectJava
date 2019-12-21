package com.project.parser;

import com.project.files.FilesCollector;

import javax.swing.text.html.parser.Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileParser {


    public static FilesCollector parseFile(String name) throws IOException, ParserException {
        Objects.requireNonNull(name);
        return ParserFactory.createParser(Paths.get(name)).parseMyFile(name);
    }
}
