package com.project.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class FileClass implements FileInterface{
    private final String name;

    public FileClass(String name){
        this.name = Objects.requireNonNull(name);
    }

    public String getName(){return name;}

    @Override
    public int getVersion(){
        return getClassReader().readByte(7)-44;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getPath() {
        var split = name.split("/");
        var sb = new StringBuilder("");
        Arrays.stream(split).forEach(s -> {
            if(!s.contains(".class")){
                sb.append(s).append("/");
            }
        });
        return sb.toString();
    }

    public ClassReader getClassReader(){
        try {
            return new ClassReader(new FileInputStream(name));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name);
        }
    }
}
