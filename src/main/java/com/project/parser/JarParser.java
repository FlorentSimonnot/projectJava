package com.project.parser;

import com.project.files.FileClass;
import com.project.files.FileInterface;
import com.project.files.JarClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

public class JarParser implements FileParserInterface {

    @Override
    public FileInterface parseMyFile(String name) {
        Objects.requireNonNull(name);
        return new JarClass(name, listFilesForFolder(name));
    }

    private List<FileClass> listFilesForFolder(String name) {
        var list = new ArrayList<FileClass>();
        var zip = new ZipInputStream(new URL(name));
        //var folder = new File(name);
        /*for(File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                list.add(new FileClass(fileEntry.getName()));
            }
        }*/
        return list;
    }
}
