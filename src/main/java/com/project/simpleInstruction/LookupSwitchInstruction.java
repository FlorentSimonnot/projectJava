package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a lookup switch instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class LookupSwitchInstruction implements Instruction {
	private final Label dflt;
	private final int[] keys;
	private final Label[] labels;
	
	/**
	 * Creates a new LookipSwitchInstruction.
	 * @param dflt - beginning of the default handler block
	 * @param keys - the values of the keys
	 * @param labels - beginnings of the handler blocks
	 */
	public LookupSwitchInstruction(Label dflt, int[] keys, Label[] labels) {
		this.dflt = dflt;
		this.keys = keys;
		this.labels = labels;
	}
	
	
	@Override
	/**
	 * Writes the lookup switch instruction into a .class file.
	 */
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruciton) {
		mv.visitLookupSwitchInsn(dflt, keys, labels);
	}
	
	@Override
	public String toString() {
		return "LOOKUP SWITCH INSN";
	}
}
