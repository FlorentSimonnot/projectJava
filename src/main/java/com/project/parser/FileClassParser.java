package com.project.parser;

import com.project.files.FileClass;
import com.project.files.FilesCollector;

import java.util.Objects;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse a .class file.
 * It simply creates a .class file according to a file name.
 *
 */
public class FileClassParser implements FileParserInterface {

    @Override
    /**
     * Collects a .class file according to a name given.
     * @param name - the name of the file you want to collect
     * @return the FilesCollector of .class file of the file given
     */
    public FilesCollector parseMyFile(String name) {
        Objects.requireNonNull(name);
        var collector = new FilesCollector();
        if(!name.endsWith(".class"))
            throw new IllegalArgumentException();
        collector.addFile(new FileClass(name));
        return collector;
    }
}
