package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a iinc instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class IincInstruction implements Instruction {
	private final int var;
	private final int increment;
	
	/**
	 * Creates a new IincInstruction.
	 * @param var - index of the local variable to be incremented
	 * @param increment - amount to increment the local variable by
	 */
	public IincInstruction(int var, int increment) {
		this.var = var;
		this.increment = increment;
	}
	
	@Override
	/**
	 * Writes the iinc instruction according to a version into a .class file.
	 */
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitIincInsn(var, increment);
	}
	
	@Override
	public String toString() {
		return "IINC INSN var: " + var + " increment: " + increment;
	}
	
}
