package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

public interface Instruction {

    void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction);

    default void addInstruction(Instruction instruction){/*Nothing*/}
    default boolean isAloadInstruction(){return false;}
    default boolean isRecordInvokeInit(){return false;}
    default String getType(){return "(V)";}
    default boolean isNew(){return false;}
}
