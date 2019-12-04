package com.project.parser;

import com.project.files.DirectoryClass;
import com.project.files.FileClass;
import com.project.files.FileInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DirectoryParser implements FileParserInterface {

    @Override
    public FileInterface parseMyFile(String name) {
        return new DirectoryClass(name, listFilesForFolder(name));
    }

    private List<FileClass> listFilesForFolder(String name) {
        var list = new ArrayList<FileClass>();
        var folder = new File(name);
        if(folder.isDirectory()){
            for(File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (!fileEntry.isDirectory()) {
                    list.add(new FileClass(fileEntry.getName()));
                }
            }
        }
        return list;
    }
}
