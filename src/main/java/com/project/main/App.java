package com.project.main;

import org.objectweb.asm.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {

    public static void main(String[] args){
        var list = new ArrayList<MethodVisitor>();

        var visitor = new ClassVisitor(Opcodes.ASM7){
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                //System.out.println("Method visited " + name + " " + descriptor + " " + signature);
                list.add(super.visitMethod(access, name, descriptor, signature, exceptions));
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                System.out.println("Field visited " + name + " " + descriptor + " " + signature);
                return super.visitField(access, name, descriptor, signature, value);
            }
        };


        try {
            ClassReader classReader = new ClassReader("java.lang.Integer");
            //classReader.accept(classVisitor, 0);
            System.out.println("Ma version c'est le java : " + classReader.readByte(7));
            classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
            System.out.println(list.size());
            list.forEach(l -> {
                l.visitCode();
                l.visitParameter("none", Opcodes.ACC_FINAL);
                l.visitEnd();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
