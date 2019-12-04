package com.project.files;

import com.project.files.FileInterface;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class DirectoryClass implements FileInterface {
    private final String path;
    private final List<FileClass> files;

    public DirectoryClass(String path, List<FileClass> files){
        this.path = path;
        this.files = Objects.requireNonNull(files);
    }

    @Override
    public int getVersion(){
        files.forEach(f -> {
            try {
                var reader = new ClassReader(Files.readAllBytes(Paths.get(path+ "/"+f.getName())));
                System.out.println(reader.readByte(7) - 44);
            } catch (IOException e) {
                throw new IllegalStateException("Can't read the file " + path+ "/"+f.getName());
            }
        });
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
