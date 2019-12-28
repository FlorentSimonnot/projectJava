package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a zero instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class NopInstruction implements Instruction {
    private final int opcode;

    /**
     * Creates a new NopInstruction.
     * @param opcode - the opcode of the instruction to be visited
     */
    public NopInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return "NOP INSN " + opcode;
    }

    @Override
    /**
     * Tests if the zero instruction is an aload instruction.
     */
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD;
    }

    @Override
    /**
     * Writes the zero instruction into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitInsn(opcode);
    }

}
