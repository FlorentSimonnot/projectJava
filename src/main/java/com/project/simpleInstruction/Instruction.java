package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * An interface that represents any type of instruction of a .class file.
 *
 */
public interface Instruction {

	/**
	 * Writes the Instruction according to a version into a .class file.
	 * @param version - the target version of the Instruction
	 * @param mv - the MethodVisitor attached to a .class file
	 * @param lastInstruction -  the Instruction that precedes this Instruction
	 */
    void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction);

    /**
     * Adds an Instruction into a Method.
     * @param instruction - the instruction you want to add
     */
    default void addInstruction(Instruction instruction){/*Nothing*/}
    
    /**
     * Tests if an Instruction is an aload instruction.
     * @return true if this Instruction is an aload, false if not
     */
    default boolean isAloadInstruction(){return false;}
    
    /**
     * Tests if an Instruction is a record invoke init instruction.
     * @return true if this Instruction is a record invoke init instruction, false if not
     */
    default boolean isRecordInvokeInit(){return false;}
    
    /**
     * Gets the type of an Instruction.
     * @return the type of an Instruction
     */
    default String getType(){return "(V)";}
    
    /**
     * Tests if an Instruction is an invoke virtual instruction.
     * @return true if this Instruction is an invoke virtual instruction, false if not
     */
    default boolean isInvokeVirtual(){return false;}
    
    /**
     * Tests if an Instruction is a new instruction.
     * @return true if this Instruction is a new instruction, false if not
     */
    default boolean isNew(){return false;}
    
    /**
     * Tests if an Instruction is a try-catch block instruction.
     * @return true if this Instruction is a try-catch block instruction, false if not
     */
    default boolean isTryCatchBlock(){return false;}
}
