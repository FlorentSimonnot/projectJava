package com.project.featuresInstruction;

import com.project.simpleInstruction.Instruction;
import com.project.simpleInstruction.InstructionsCollector;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TryWithResourcesInstruction implements Instruction {
    private static final int VERSION = Opcodes.V1_7;
    private final InstructionsCollector instructions;

    public TryWithResourcesInstruction(InstructionsCollector instructions) {
        this.instructions = instructions;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {

    }

    @Override
    public boolean isAloadInstruction() {
        return false;
    }
}
