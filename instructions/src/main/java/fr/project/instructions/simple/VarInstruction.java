package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class VarInstruction implements Instruction {
    private final int var;
    private final int opcode;

    public VarInstruction(int opcode, int var) {
        this.opcode = opcode;
        this.var = var;
    }

    public int getVar() {
        return var;
    }

    @Override
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
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD || opcode == Opcodes.ILOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitVarInsn(opcode, var);
    }
}
