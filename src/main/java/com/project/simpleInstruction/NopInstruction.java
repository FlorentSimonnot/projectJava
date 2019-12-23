package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NopInstruction implements Instruction {
    private final int opcode;

    public NopInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return "NOP INSN " + opcode;
    }

    @Override
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitInsn(opcode);
    }

}
