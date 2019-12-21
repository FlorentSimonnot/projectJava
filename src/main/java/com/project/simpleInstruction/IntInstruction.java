package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

public class IntInstruction implements Instruction {
    private final int opcode;
    private final int operand;

    public IntInstruction(int opcode, int operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public int getVar() {
        return operand;
    }

    public String getType() {
        return "(I)";
    }

    @Override
    public String toString() {
        return "INT INSN " + opcode + " name : " + operand;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitVarInsn(opcode, operand);
    }
}
