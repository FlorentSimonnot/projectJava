package com.project.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;

public class DirectoryFile implements FileInterface {
    private final String name;
    private final String path;

    public DirectoryFile(String name, String path){
        this.name = name;
        this.path = path;
    }

    @Override
    public String getName() {
        return path+name;
    }

    @Override
    public int getVersion() {
        return createClassReader().readByte(7)-44;
    }

    private ClassReader createClassReader(){
        try {
            return new ClassReader(new FileInputStream(path+"/"+name));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name + " in " + path);
        }
    }
}
