package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JumpInstruction implements Instruction {
    private final int opcode;
    private final Label label;

    public JumpInstruction(int opcode, Label label) {
        this.opcode = opcode;
        this.label = label;
    }

    @Override
    public String toString() {
        if(opcode == Opcodes.GOTO)
            return "GOTO INSN " + label;
        return "JUMP INSN " + label;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitJumpInsn(opcode, label);
    }
}
