package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class BeginTryCatchBlock implements Instruction {
    private final Label start;


    public BeginTryCatchBlock(Label start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "BEGIN TRY CATCH BLOCK AT " + start ;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        //Nothing
    }

}
