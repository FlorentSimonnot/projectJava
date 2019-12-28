package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LookupSwitchInstruction implements Instruction {
	private final Label dflt;
	private final int[] keys;
	private final Label[] labels;
	
	public LookupSwitchInstruction(Label dflt, int[] keys, Label[] labels) {
		this.dflt = dflt;
		this.keys = keys;
		this.labels = labels;
	}
	
	public Label getDflt() {
		return dflt;
	}
	
	public int[] getKeys() {
		return keys;
	}
	
	public Label[] getLabels() {
		return labels;
	}
	
	@Override
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruciton) {
		mv.visitLookupSwitchInsn(dflt, keys, labels);
	}
	
	@Override
	public String toString() {
		return "LOOKUP SWITCH INSN";
	}
}
