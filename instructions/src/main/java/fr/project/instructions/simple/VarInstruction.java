package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a variable instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class VarInstruction implements Instruction {
    private final int var;
    private final int opcode;

    /**
     * Creates a new VarInstruction.
     * @param opcode - the opcode of the local variable instruction to be visited
     * @param var - the operand of the instruction to be visited
     */
    public VarInstruction(int opcode, int var) {
        this.opcode = opcode;
        this.var = var;
    }

    @Override
    /**
     * Gets the type of the variable instruction.
     */
    public String getType() {
        switch(opcode){
            case Opcodes.ILOAD : return "(I)";
            case Opcodes.LLOAD: return "(J)";
            case Opcodes.DLOAD: return "(D)";
            case Opcodes.ALOAD : return "(Ljava/lang/Object;)";
            default : return "()";
        }
    }

    @Override
    public String toString() {
        return "VAR INSN " + opcode + " name : " + var;
    }

    @Override
    /**
     * Tests if the variable instruction is an load instruction.
     */
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD || opcode == Opcodes.ILOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD;
    }

    @Override
    /**
     * Writes the variable instruction into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitVarInsn(opcode, var);
    }
}
