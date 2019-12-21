package com.project.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

public class JarFileC implements FileInterface {
    private final String name;
    private final String entry;
    private final String zipName;

    public JarFileC(String name, String entry, String zipName){
        this.name = name;
        this.entry = entry;
        this.zipName = zipName;
    }

    @Override
    public String getName() {
        return zipName+"/"+entry+"/"+name;
    }

    @Override
    public int getVersion() {
        return getClassReader().readByte(7)-44;
    }

    public ClassReader getClassReader(){
        try {
            return new ClassReader(new java.util.jar.JarFile(zipName).getInputStream(new ZipEntry(entry)));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name + " in " + zipName);
        }
    }
}
