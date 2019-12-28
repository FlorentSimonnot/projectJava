package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to detect and write a multi a new array instruction of a .class file.
 * It is stored as an Instruction of a Method.
 *
 */
public class MultiANewArrayInstruction implements Instruction {
	private final String descriptor;
	private final int numDimensions;

	/**
	 * Creates a new MultiANewArrayInstruction.
	 * @param descriptor - an array type descriptor
	 * @param numDimensions - the number of dimensions of the array to allocate
	 */
	public MultiANewArrayInstruction(String descriptor, int numDimensions) {
		this.descriptor = descriptor;
		this.numDimensions = numDimensions;
	}
	
	@Override
	/**
	 * Writes the multi a new array instruction into a .class file.
	 */
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitMultiANewArrayInsn(descriptor, numDimensions);
	}
	
	@Override
	public String toString() {
		return "MULTI A NEW ARRAY INSN";
	}
	
}
