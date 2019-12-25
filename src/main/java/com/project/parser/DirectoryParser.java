package com.project.parser;

import com.project.files.DirectoryFile;
import com.project.files.FileClass;
import com.project.files.FilesCollector;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse a directory.
 *
 */
public class DirectoryParser implements FileParserInterface {

    @Override
    /**
     * Collects all the .class files of a directory.
     * @param name - the name of the directory you want to parse
     * @return the FilesCollector of all .class files in the directory given
     */
    public FilesCollector parseMyFile(String name) throws ParserException {
        return listFilesForFolder(name);
    }

    private FilesCollector listFilesForFolder(String name) throws ParserException {
        if(Objects.requireNonNull(name).contains("."))
            throw new ParserException("We can't accept this type of file");
        var collector = new FilesCollector();
        var folder = new File(name);
        if(folder.isDirectory()){
            for(File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (!fileEntry.isDirectory() && fileEntry.getName().endsWith(".class")) {
                    collector.addFile(new DirectoryFile(fileEntry.getName(), name));
                }
            }
        }
        else{
            throw new ParserException();
        }
        return collector;
    }
}
