package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class JumpInstruction implements Instruction {
    private final int opcode;
    private final Label label;

    public JumpInstruction(int opcode, Label label) {
        this.opcode = opcode;
        this.label = label;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitJumpInsn(opcode, label);
    }
}
