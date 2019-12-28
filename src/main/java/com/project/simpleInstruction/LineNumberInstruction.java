package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a line number instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class LineNumberInstruction implements Instruction {
    private final int lineNumber;
    private final Label start;

    /**
     * Creates a new LineNumberInstruction.
     * @param lineNumber - a line number
     * @param start - the first instruction corresponding to this line number
     */
    public LineNumberInstruction(int lineNumber, Label start) {
        this.lineNumber = lineNumber;
        this.start = start;
    }

    @Override
    public String toString() {
        return "VISIT LINE " + lineNumber + " BEGIN LABEL " + start;
    }

    @Override
    /**
     * Writes the line number instruction according to a version into a .class file.
     */
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitLineNumber(lineNumber, start);
    }
}
