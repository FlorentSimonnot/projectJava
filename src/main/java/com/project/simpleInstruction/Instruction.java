package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

public interface Instruction {

    void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction);
    default boolean isAloadInstruction(){return false;}
    default boolean isRecordInvokeInit(){return false;}
    default String getType(){return "()";}
}
