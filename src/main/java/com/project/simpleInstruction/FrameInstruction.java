package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FrameInstruction implements Instruction {
    private final int type;
    private final int numLocal;
    private final Object[] local;
    private final int numStack;
    private final Object[] stack;

    public FrameInstruction(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.type = type;
        this.numLocal = numLocal;
        this.local = local;
        this.numStack = numStack;
        this.stack = stack;
    }

    @Override
    public String toString() {
        return "Frame Instruction " + type;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitFrame(type, numLocal, local, numStack, stack);
    }
}
