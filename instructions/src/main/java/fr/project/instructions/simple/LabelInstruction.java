package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LabelInstruction implements Instruction {
    private final Label label;

    public LabelInstruction(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "LABEL INSN " + label;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitLabel(label);
    }
}
