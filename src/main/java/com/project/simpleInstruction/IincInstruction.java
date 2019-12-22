package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

public class IincInstruction implements Instruction {
	private final int var;
	private final int increment;
	
	public IincInstruction(int var, int increment) {
		this.var = var;
		this.increment = increment;
	}
	
	public int getVar() {
		return var;
	}
	
	public int getIncrement() {
		return increment;
	}
	
	@Override
	void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitIincInsn(var, increment);
	}
	
	@Override
	public String toString() {
		return "IINC INSN var: " + var + " increment: " + increment;
	}
	
}
