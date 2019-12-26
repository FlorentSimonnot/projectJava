package com.project.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * 
 * @author SIMONNOT Florent
 * A class that describes a .jar file.
 *
 */
public class JarFileC implements FileInterface {
    private final String name;
    private final String entry;
    private final String zipName;

    /**
     * Creates a new JarFileC.
     * @param name - the name of the .class file from the .jar file
     * @param entry - 
     * @param zipName - the name of .jar file
     */
    public JarFileC(String name, String entry, String zipName){
        this.name = name;
        this.entry = entry;
        this.zipName = zipName;
    }

    @Override
    /**
     * Gets the name of the .jar file.
     */
    public String getName() {
        return zipName+"/"+entry+"/"+name;
    }

    @Override
    /**
     * Gets the java version of the .jar file.
     */
    public int getVersion() {
        return getClassReader().readByte(7)-44;
    }

    @Override
    /**
     * Gets the full path of the .jar file.
     */
    public String getPath() {
        return zipName;
    }

    /**
     * Gets the ClassReader of the .jar file.
     */
    public ClassReader getClassReader(){
        try {
            return new ClassReader(new java.util.jar.JarFile(zipName).getInputStream(new ZipEntry(entry)));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name + " in " + zipName);
        }
    }
}
