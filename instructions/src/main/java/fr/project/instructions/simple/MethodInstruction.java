package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodInstruction implements Instruction {
    private final String name;
    private final String owner;
    private final int opcode;
    private final String descriptor;
    private final boolean isInterface;

    public MethodInstruction(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.name = name;
        this.owner = owner;
        this.opcode = opcode;
        this.descriptor = descriptor;
        this.isInterface = isInterface;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

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

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return "name : " + name + " owner : " + owner + " opcode : " + opcode + " descriptor : " + descriptor;
    }

    @Override
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(isRecordInvokeInit()){
            mv.visitMethodInsn(opcode, "java/lang/Object", name, descriptor, isInterface);
            return;
        }
        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public boolean isRecordInvokeInit() {
        return owner.equals("java/lang/Record");
    }

    public boolean isInvokeVirtual(){return !descriptor.equals("()V");}
}
