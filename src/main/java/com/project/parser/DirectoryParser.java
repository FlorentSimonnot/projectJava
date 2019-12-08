package com.project.parser;

import com.project.files.DirectoryFile;
import com.project.files.FileClass;
import com.project.files.FilesCollector;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class DirectoryParser implements FileParserInterface {

    @Override
    public FilesCollector parseMyFile(String name) throws IllegalArgumentException {
        return listFilesForFolder(name);
    }

    private FilesCollector listFilesForFolder(String name) {
        var collector = new FilesCollector();
        var folder = new File(Objects.requireNonNull(name));
        if(folder.isDirectory()){
            for(File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (!fileEntry.isDirectory() && fileEntry.getName().endsWith(".class")) {
                    collector.addFile(new DirectoryFile(fileEntry.getName(), name));
                }
            }
        }
        else{
            throw new IllegalArgumentException("Your directory is empty");
        }
        return collector;
    }
}
