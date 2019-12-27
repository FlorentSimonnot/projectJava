package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a field instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class FieldInstruction implements Instruction {
    private final String name;
    private final String owner;
    private final int opcode;
    private final String descriptor;

    /**
     * Creates a new FieldInstruction.
     * @param name - the name of the field
     * @param owner - the name of the Method that contains the field
     * @param opcode - the opcode of the field instruction
     * @param descriptor - the descriptor of the field
     */
    public FieldInstruction(String name, String owner, int opcode, String descriptor) {
        this.name = name;
        this.owner = owner;
        this.opcode = opcode;
        this.descriptor = descriptor;
    }

    /**
     * Gets the name of the field.
     * @return the name of the field instruction
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the name of the method that contains the field instruction.
     * @return the name of the method that contains the field instruction
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the type of the field instruction.
     * @return the type of the field instruction
     */
    public String getType()    {
        switch(opcode) {
            case Opcodes.ILOAD:
                return "(I)";
            case Opcodes.LLOAD:
                return "(J)";
            case Opcodes.DLOAD:
                return "(D)";
            case Opcodes.ALOAD:
                return "(Ljava/lang/Object;)";
            case Opcodes.GETFIELD :
                return "("+descriptor+")";
            default:
                return "()";
        }
    }

    /**
     * Gets the descriptor of the field instruction.
     * @return the descriptor of the field instruction
     */
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    /**
     * Writes the field instruction according to a version into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(opcode == Opcodes.GETFIELD && !lastInstruction.isAloadInstruction()) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
        }
        mv.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public String toString() {
        return "FIELD INSN name : " + name + " owner : " + owner + " opcode : " + opcode + " descriptor : " + descriptor;
    }
}
