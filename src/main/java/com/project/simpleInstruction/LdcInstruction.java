package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

public class LdcInstruction implements Instruction {
    private final Object value;


    public LdcInstruction(Object value) {
        this.value = value;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitLdcInsn(value);
    }

    @Override
    public String toString() {
        return "LDC : " + value;
    }
}
