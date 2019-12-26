package com.project.visitor;

import com.project.simpleInstruction.Field;
import com.project.simpleInstruction.Method;
import com.project.simpleInstruction.MyClass;
import com.project.featuresObserver.FeatureObserver;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to visit a .class file. It contains observers that can react when the visitor detects some features.
 * It also contains a list of nest-mates of the class and store them into a field.
 *
 */
public class MyClassVisitor extends ClassVisitor {
    private final List<FeatureObserver> observers;
    private final List<String> nestMates = new ArrayList<>();
    private MyClass myClass;

    /**
     * Creates a new MyClassVisitor.
     * @param api - the version of the ASM api
     * @param cv - a ClassVisitor you want to link with
     * @param observers - a list of FeatureObserver you want to link with
     */
    public MyClassVisitor(int api, ClassVisitor cv, List<FeatureObserver> observers) {
        super(api, cv);
        this.observers = observers;
    }

    /**
     * Gets the MyClass of the visitor.
     * @return the MyClass of the visitor.
     */
    public MyClass getMyClass(){return myClass;}

    @Override
    /**
     * Visits the header of the .class file. Also, it tests if the class is a record class.
     */
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        myClass = new MyClass(access, name, superName, interfaces);
        if(myClass.isRecordClass()){
            observers.forEach(o -> o.onFeatureDetected(name + " is a record class", "record"));
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        return super.visitModule(name, access, version);
    }

    @Override
    public void visitSource(String source, String debug) {
        myClass.setSourceName(source);
        super.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    @Override
    /**
     * Makes the observers (it detects a nest-host).
     */
    public void visitNestHost(String nestHost) {
        observers.forEach(o -> {
            o.onFeatureDetected("NESTMATES at " + myClass.getClassName() + " nestmate of " + nestHost
                    , "nestMember");
        });
        super.visitNestHost(nestHost);
    }

    @Override
    /**
     * Adds a new nest-member into the field nestMates.
     */
    public void visitNestMember(String nestMember) {
        nestMates.add(nestMember);
        super.visitNestMember(nestMember);
    }

    @Override
    /**
     * Adds a new field into the field fields of the class MyClass.
     */
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        myClass.addField(new Field(access, name, descriptor, signature, value));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    /**
     * Returns a custom MethodVisitor (MyMethodVisitor) that contains methods that allow to detect features and write them.
     */
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        //allows to provide a custom MethodVisitor that can transform actual methods
        MethodVisitor mv;
        mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null) {
            mv = new MyMethodVisitor(mv, observers, myClass.getAllMethods(), new Method(access, name, descriptor, signature, false, exceptions), myClass, exceptions);
        }
        return mv;
    }

    @Override
    /**
     * Displays all the nest-hosts detected in the .class file.
     */
    public void visitEnd() {
        if(nestMates.size() > 0)
            addNestHost();
        super.visitEnd();
    }

    private void addNestHost(){
        var joiner = new StringJoiner(", ");
        for(String s : nestMates){
            joiner.add(s);
        }
        observers.forEach(o -> {
            o.onFeatureDetected("NESTMATES at " + myClass.getClassName() + " nest host of " + myClass.getClassName() + " members [" + joiner.toString() + "]"
                    , "nestMember");
        });
    }
    
}
