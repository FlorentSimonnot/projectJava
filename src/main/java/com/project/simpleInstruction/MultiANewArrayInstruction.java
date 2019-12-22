package com.project.simpleInstruction;

import org.objectweb.asm.MethodVisitor;

public class MultiANewArrayInstruction implements Instruction {
	private final String descriptor;
	private final int numDimensions;

	public MultiANewArrayInstruction(String descriptor, int numDimensions) {
		this.descriptor = descriptor;
		this.numDimensions = numDimensions;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public int getNumDimensions() {
		return numDimensions;
	}
	
	@Override
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitMultiANewArrayInsn(descriptor, numDimensions);
	}
	
	@Override
	public String toString() {
		return "MULTI A NEW ARRAY INSN";
	}
	
}
