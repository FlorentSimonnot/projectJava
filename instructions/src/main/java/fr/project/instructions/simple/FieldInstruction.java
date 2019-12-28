package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FieldInstruction implements Instruction {
    private final String name;
    private final String owner;
    private final int opcode;
    private final String descriptor;

    public FieldInstruction(String name, String owner, int opcode, String descriptor) {
        this.name = name;
        this.owner = owner;
        this.opcode = opcode;
        this.descriptor = descriptor;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

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

    public String getDescriptor() {
        return descriptor;
    }

    @Override
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
