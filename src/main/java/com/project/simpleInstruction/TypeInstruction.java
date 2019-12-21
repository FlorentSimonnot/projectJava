package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TypeInstruction implements Instruction {
    private final int opcode;
    private final String owner;

    public TypeInstruction(int opcode, String owner) {
        this.opcode = opcode;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "TYPE INSN " + opcode + " owner : " + owner;
    }

    @Override
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD || opcode == Opcodes.ILOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitTypeInsn(opcode, owner);
    }
}
