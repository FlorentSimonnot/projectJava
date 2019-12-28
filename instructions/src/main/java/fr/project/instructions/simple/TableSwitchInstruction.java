package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class TableSwitchInstruction implements Instruction {
	private final int min;
	private final int max;
	private final Label dflt;
	private final Label[] labels;
	
	public TableSwitchInstruction(int min, int max, Label dflt, Label... labels) {
		this.min = min;
		this.max = max;
		this.dflt = dflt;
		this.labels = labels;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public Label getDflt() {
		return dflt;
	}
	
	public Label[] getLabels() {
		return labels;
	}

	@Override
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitTableSwitchInsn(min, max, dflt, labels);
	}
	
	@Override
	public String toString() {
		return "TABLE SWITCH INSN";
	}

}
