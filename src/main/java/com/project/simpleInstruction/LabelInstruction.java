package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LabelInstruction implements Instruction {
    private final Label label;


    public LabelInstruction(Label start) {
        this.label = start;
    }

    @Override
    public String toString() {
        return "LABEL " + label ;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitLabel(label);
    }

}
