package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class EndTryCatchBlock implements Instruction {
    private final Label end;
    private final Label start;


    public EndTryCatchBlock(Label end, Label start) {
        this.end = end; this.start = start;
    }

    @Override
    public String toString() {
        return "END TRY CATCH BLOCK AT " + end + " CLOSE TRY BEGIN AT " + start;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        //Nothing
    }

}
