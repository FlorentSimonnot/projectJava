package com.project.featuresInstruction;

import com.project.simpleInstruction.Instruction;
import com.project.simpleInstruction.InstructionsCollector;
import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.StringJoiner;

public class ConcatenationInstruction implements Instruction {
    private static final int VERSION = Opcodes.V9;
    private final int nArgs;
    private final InstructionsCollector instructions;
    private final String format;

    public ConcatenationInstruction(int nArgs, InstructionsCollector instructions, String format){
        this.nArgs = nArgs;
        this.instructions = instructions;
        this.format = format;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(version < VERSION){
            writeInstructionOldVersion(version, mv, lastInstruction);
        }
        else {
            writeInstructionNewVersion(version, mv, lastInstruction);
        }
    }

    private void writeInstructionNewVersion(int version, MethodVisitor mv, Instruction lastInstruction) {
        instructions.forEach(i -> {
            System.out.println(i);
            i.writeInstruction(version, mv, lastInstruction);
        });
    }

    private void writeInstructionOldVersion(int version, MethodVisitor mv, Instruction lastInstruction) {
        var type = Type.getType(StringBuilder.class);
        mv.visitTypeInsn(Opcodes.NEW, type.getInternalName());
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, type.getInternalName(), "<init>", "()V", false);
        instructions.forEach(System.out::println);
        var count = 0;
        var splitFormat = format.split(" ");
        for (String s : splitFormat) {
            if(s.equals("arg")){
                var insn = instructions.getInstruction(count);
                insn.writeInstruction(version, mv, lastInstruction);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", instructions.getInstruction(count).getType()+"Ljava/lang/StringBuilder;", false);
                count++;
            }
            else{
                mv.visitLdcInsn(s);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
        }
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "toString", "()Ljava/lang/String;", false);
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner("\n---> ");
        instructions.forEach(i -> joiner.add(i.toString()));
        return "Concatenation : \n---> " + joiner;
    }

    @Override
    public boolean isAloadInstruction() {
        return false;
    }
}
