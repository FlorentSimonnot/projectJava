package com.project.featuresInstruction;

import com.project.simpleInstruction.Instruction;
import com.project.simpleInstruction.InstructionsCollector;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author SIMONNOT Florent
 * A Class that allows to write a try-with-resources in bytecode into different versions.
 * It is stored as an Instruction of a Method. And this Method object is used into a MethodVisitor also used into a ClassVisitor.
 * The ClassVisitor will visit a .class file and write a code block in bytecode corresponding to a try-with-resources from the .class file on a new .class file according to the version required.
 *
 */

public class TryWithResourcesInstruction implements Instruction {
    private static final int VERSION = Opcodes.V1_7;
    private final InstructionsCollector instructions;

    /**
     * Creates a new TryWithResourcesInstruction.
     * @param instructions - the instructions block attached to this try-with-resources instruction
     */
    public TryWithResourcesInstruction(InstructionsCollector instructions) {
        this.instructions = instructions;
    }

    /**
     * Writes the bytecode corresponding to the try-with-resources instruction according to the version given.
     * @param version - the target version of the try-with-resources instruction
     * @param mv - the MethodVisitor attached to this TryWithResourcesInstruction
     * @param lastInstruction - the instruction preceding this TryWithResourcesInstruction
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {

    }

    /**
     * Returns if this instruction is an aload instruction.
     */
    @Override
    public boolean isAloadInstruction() {
        return false;
    }
}
