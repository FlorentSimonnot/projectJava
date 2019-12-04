package com.project.files;

import com.project.files.FileInterface;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class JarClass implements FileInterface {
    private final String path;
    private final List<FileClass> files;

    public JarClass(String path, List<FileClass> files){
        this.path = path;
        this.files = Objects.requireNonNull(files);
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public String toString() {
        var stringJoiner = new StringJoiner(" \n ");
        files.forEach(f -> {
            stringJoiner.add(f.toString());
        });
        return stringJoiner.toString();
    }
}
