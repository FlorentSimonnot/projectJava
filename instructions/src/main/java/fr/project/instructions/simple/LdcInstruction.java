package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a ldc instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class LdcInstruction implements Instruction {
    private final Object value;

    /**
     * Creates a new LdcInstruction.
     * @param value - the constant to be loaded on the stack
     */
    public LdcInstruction(Object value) {
        this.value = value;
    }

    @Override
    /**
     * Writes the ldc instruction into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitLdcInsn(value);
    }

    @Override
    public String toString() {
        return "LDC : " + value;
    }
}
