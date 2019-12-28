package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a table switch instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class TableSwitchInstruction implements Instruction {
	private final int min;
	private final int max;
	private final Label dflt;
	private final Label[] labels;
	
	/**
	 * Creates a new TableSwitchInstruction.
	 * @param min - the minimum key value
	 * @param max - the maximum key value
	 * @param dflt - beginning of the default handler block
	 * @param labels - beginnings of the handler blocks
	 */
	public TableSwitchInstruction(int min, int max, Label dflt, Label... labels) {
		this.min = min;
		this.max = max;
		this.dflt = dflt;
		this.labels = labels;
	}
	
	@Override
	/**
	 * Writes the table switch instruction into a .class file.
	 */
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitTableSwitchInsn(min, max, dflt, labels);
	}
	
	@Override
	public String toString() {
		return "TABLE SWITCH INSN";
	}

}
