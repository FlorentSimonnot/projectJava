package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * A class that allows to detect and write a type instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class TypeInstruction implements Instruction {
    private final int opcode;
    private final String owner;

    /**
     * Creates a new TypeInstruction.
     * @param opcode - the opcode of the type instruction to be visited
     * @param owner - the operand of the instruction to be visited
     */
    public TypeInstruction(int opcode, String owner) {
        this.opcode = opcode;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "TYPE INSN " + opcode + " owner : " + owner;
    }

    @Override
    /**
     * Tests if the type instruction is an load instruction.
     */
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD || opcode == Opcodes.ILOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD;
    }

    @Override
    /**
     * Writes the type instruction into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitTypeInsn(opcode, owner);
    }

    @Override
    /**
     * Tests if the type instruction is a new instruction.
     */
    public boolean isNew() {
        return opcode == Opcodes.NEW;
    }
}
