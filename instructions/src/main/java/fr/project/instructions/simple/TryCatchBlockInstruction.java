package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class TryCatchBlockInstruction implements Instruction {
	private final Label start;
	private final Label end;
	private final Label handler;
	private final String type;
	
	public TryCatchBlockInstruction(Label start, Label end, Label handler, String type) {
		this.start = start;
		this.end = end;
		this.handler = handler;
		this.type = type;
	}
	
	public Label getStart() {
		return start;
	}
	
	public Label getEnd() {
		return end;
	}
	
	public Label getHandler() {
		return handler;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
		mv.visitTryCatchBlock(start, end, handler, type);
	}
	
	@Override
	public String toString() {
		return "TRY CATCH BLOCK INSN " + type ;
	}

}
