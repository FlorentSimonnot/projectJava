package com.project.simpleInstruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class TryCatchBlock implements Instruction{
    private final Label start;
    private final InstructionsCollector instructions;
    private boolean blockIsFinish;
    private final Label end;

    public TryCatchBlock(Label start, Label end) {
        this.instructions = new InstructionsCollector();
        blockIsFinish = false;
        this.start = start;
        this.end = end;
        instructions.add(new BeginTryCatchBlock(start));
    }

    /**
     * void addInstruction(Instruction instruction)
     * Add an instruction in the try catch block.
     * @param instruction - the instruction we want to add.
     */
    public void addInstruction(Instruction instruction){
        if(blockIsFinish){
            throw new IllegalStateException("Can't add instruction because the block is closed");
        }
        instructions.add(instruction);
    }

    /**
     * boolean isBlockIsFinish()
     * Verify if the current try catch block is close.
     * @return true - if the current block is close, false else.
     */
    public boolean isBlockFinish(){
        return blockIsFinish;
    }

    /**
     * void closeTryCatchBlock()
     * Close the current try catch block.
     */
    public void closeTryCatchBlock(){
        instructions.add(new EndTryCatchBlock(start, end));
        blockIsFinish = true;
    }

    /**
     * boolean isTheEndLabel(Label label)
     * Verify if the label passed in parameter is the label which close the block.
     * @param label - A label
     * @return true if the label close the current block, false else.
     */
    public boolean isTheEndLabel(Label label){
        return this.end == label;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("----------BLOCK--------------\n");
        instructions.forEach(i -> {
            sb.append(i).append("\n");
        });
        sb.append("--------------END BLOCK-------------------\n");
        return sb.toString();
    }

    @Override
    public boolean isTryCatchBlock() {
        return true;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {

    }
}
