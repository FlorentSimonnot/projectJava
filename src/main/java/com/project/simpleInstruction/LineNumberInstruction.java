package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LineNumberInstruction implements Instruction {
    private final int lineNumber;
    private final Label start;

    public LineNumberInstruction(int lineNumber, Label start) {
        this.lineNumber = lineNumber;
        this.start = start;
    }

    @Override
    public String toString() {
        return "VISIT LINE " + lineNumber + " BEGIN LABEL " + start;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        mv.visitLineNumber(lineNumber, start);
    }
}
