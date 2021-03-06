package com.project.featuresInstruction;

import com.project.simpleInstruction.Instruction;
import com.project.simpleInstruction.InstructionsCollector;
import com.project.simpleInstruction.MethodInstruction;
import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ConcatenationInstruction implements Instruction {
    private static final int VERSION = Opcodes.V9;
    private final int nArgs;
    private final InstructionsCollector instructions;
    private final List<String> format;

    public ConcatenationInstruction(int nArgs, InstructionsCollector instructions, List<String> format){
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
        for(var i = 0; i < instructions.size(); i++) {
            var instruction = instructions.getInstruction(i);
            instruction.writeInstruction(version, mv, lastInstruction);
            lastInstruction = instruction;
        }
    }

    private void writeInstructionOldVersion(int version, MethodVisitor mv, Instruction lastInstruction) {
        var type = Type.getType(StringBuilder.class);
        mv.visitTypeInsn(Opcodes.NEW, type.getInternalName());
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, type.getInternalName(), "<init>", "()V", false);
        var list = splitInstructions();
        var li = lastInstruction;
        var index = 0;
        for(String f : format){
            if(f.equals("arg")){
                var instructionsList = list.get(index);
                for(var i = 0; i < instructionsList.size(); i++){
                    instructionsList.getInstruction(i).writeInstruction(version, mv, li);
                    li = instructionsList.getInstruction(i);
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", li.getType() + "Ljava/lang/StringBuilder;", false);
                index++;
            }
            else{
                mv.visitLdcInsn(f);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                li = new MethodInstruction(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
        }
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "toString", "()Ljava/lang/String;", false);
    }

    private List<InstructionsCollector> splitInstructions(){
        var list = new ArrayList<InstructionsCollector>();
        var collector = new InstructionsCollector();
        for(var i = 0; i < instructions.size()-1; i++){
            var instruction = instructions.getInstruction(i);
            if(instruction.isAloadInstruction() || instruction.isNew()){
                if(collector.size() > 0){
                    list.add(collector);
                    collector = new InstructionsCollector();
                    collector.add(instruction);
                }
                else{
                    collector.add(instruction);
                }
            }
            else {
                collector.add(instruction);
            }
        }
        list.add(collector);
        return list;
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
