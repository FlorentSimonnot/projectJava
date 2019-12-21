package com.project.visitor;

import com.project.featuresObserver.FeatureObserver;
import com.project.files.FileInterface;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyVisitor {
    private final FileInterface file;
    private final ClassWriter writer;
    private final MyClassVisitor visitor;
    private final List<FeatureObserver> observers = new ArrayList<>();

    public MyVisitor(FileInterface file, List<FeatureObserver> observers){
        this.file = Objects.requireNonNull(file);
        this.writer = new ClassWriter(0);
        this.visitor = new MyClassVisitor(Opcodes.ASM7, writer, observers);
    }

    public boolean addAll(List<FeatureObserver> observers){
        return addAll(observers);
    }

    public ClassReader getClassReader(){
        return file.getClassReader();
    }

    public ClassWriter getClassWriter(){
        return writer;
    }

    public MyClassVisitor getClassVisitor(){
        return visitor;
    }

    public List<FeatureObserver> getObservers(){
        return observers;
    }
}
