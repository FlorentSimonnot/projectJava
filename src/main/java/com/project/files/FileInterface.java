package com.project.files;

import org.objectweb.asm.ClassReader;

public interface FileInterface {

    public String getName();
    public int getVersion();
    public ClassReader getClassReader();
}
