package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * A class that allows to detect and write a method instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class MethodInstruction implements Instruction {
    private final String name;
    private final String owner;
    private final int opcode;
    private final String descriptor;
    private final boolean isInterface;

    /**
     * Creates a new MethodInstruction.
     * @param opcode - the opcode of the type instruction to be visited
     * @param owner - the internal name of the method's owner class
     * @param name - the method's name
     * @param descriptor - the method's descriptor
     * @param isInterface - if the method's owner class is an interface
     */
    public MethodInstruction(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.name = name;
        this.owner = owner;
        this.opcode = opcode;
        this.descriptor = descriptor;
        this.isInterface = isInterface;
    }

    /**
     * Gets the method instruction's type.
     */
    public String getType() {
        switch(opcode){
            case Opcodes.ILOAD : return "(I)";
            case Opcodes.LLOAD: return "(J)";
            case Opcodes.DLOAD: return "(D)";
            case Opcodes.ALOAD : return "(Ljava/lang/Object;)";
            case Opcodes.INVOKEVIRTUAL: {
                var split = descriptor.split("\\)");
                return "("+split[1]+")";
            }
            default : return "()";
        }
    }


    @Override
    public String toString() {
        return "name : " + name + " owner : " + owner + " opcode : " + opcode + " descriptor : " + descriptor;
    }

    @Override
    /**
     * Tests if the method instruction is an aload instruction.
     */
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD;
    }

    @Override
    /**
     * Writes the method instruction into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(isRecordInvokeInit()){
            mv.visitMethodInsn(opcode, "java/lang/Object", name, descriptor, isInterface);
            return;
        }
        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    /**
     * Tests if the method instruction is a record invoke init instruction.
     */
    public boolean isRecordInvokeInit() {
        return owner.equals("java/lang/Record");
    }

    /**
     * Tests if the method instruction is an invoke virtual instruction.
     */
    public boolean isInvokeVirtual(){return !descriptor.equals("()V");}
}
