package com.project.files;

import com.project.files.FileInterface;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileClass implements FileInterface {
    private final String name;

    public FileClass(String name){
        this.name = Objects.requireNonNull(name);
    }

    public String getName(){return name;}

    @Override
    public int getVersion(){
        System.out.print(name);
        try {
            var reader = new ClassReader(Files.readAllBytes(Paths.get(name)));
            return reader.readByte(7)-44;
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
