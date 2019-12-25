package com.project.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 
 * @author SIMONNOT Florent
 * A class that describes a directory that contains the .class files.
 *
 */
public class DirectoryFile implements FileInterface {
    private final String name;
    private final String path;

    /**
     * Creates a new DirectoryFile.
     * @param name - the directory's name
     * @param path - the path of the directory
     */
    public DirectoryFile(String name, String path){
        this.name = name;
        this.path = path;
    }

    @Override
    /**
     * Gets the full name of the directory.
     */
    public String getName() {
        return path+name;
    }

    @Override
    /**
     * Gets the java version of the DirectoryFile.
     */
    public int getVersion() {
        return getClassReader().readByte(7)-44;
    }

    @Override
    /**
     * Gets the full path of the directory.
     */
    public String getPath() {
        return  path;
    }

    /**
     * Gets a ClassReader of the directory's name.
     */
    public ClassReader getClassReader(){
        try {
            return new ClassReader(new FileInputStream(path+"/"+name));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name + " in " + path);
        }
    }
}
