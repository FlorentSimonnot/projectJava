package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write an int instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class IntInstruction implements Instruction {
    private final int opcode;
    private final int operand;

    /**
     * Creates a new IntInstruction.
     * @param opcode - the opcode of the instruction to be visited
     * @param operand - the operand of the instruction to be visited
     */
    public IntInstruction(int opcode, int operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    /**
     * Gets the operand of the IntInstruction.
     * @return the operand of the IntInstruction
     */
    public int getVar() {
        return operand;
    }

    /**
     * Gets the type of the IntInstruction as a java bytecode.
     */
    public String getType() {
        return "(I)";
    }

    @Override
    public String toString() {
        return "INT INSN " + opcode + " name : " + operand;
    }

    @Override
    /**
     * Writes the int instruction into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitIntInsn(opcode, operand);
    }
}
