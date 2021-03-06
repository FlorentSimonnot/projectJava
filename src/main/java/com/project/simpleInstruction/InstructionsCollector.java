package com.project.simpleInstruction;

import com.project.featuresInstruction.ConcatenationInstruction;
import org.objectweb.asm.MethodVisitor;

import java.util.*;
import java.util.function.Consumer;

public class InstructionsCollector {
    private final List<Instruction> instructions;

    public InstructionsCollector(){instructions = new LinkedList<>();}

    public int size(){return instructions.size();}

    public Instruction getInstruction(int index){return instructions.get(index);}

    public void add(Instruction instruction){
        instructions.add(instructions.size(), instruction);
    }

    void addAll(Collection<? extends Instruction> collector){
        instructions.addAll(collector);
    }

    public void clear(){instructions.clear();}

    public void forEach(Consumer<? super Instruction> consumer) {
        instructions.forEach(consumer);
    }

    void writeAllInstruction(int version, MethodVisitor methodVisitor){
        Instruction lastInstruction = new NopInstruction(0);
        for(Instruction i : instructions){
            //System.out.println("LST " + lastInstruction);
            i.writeInstruction(version, methodVisitor, lastInstruction);
            lastInstruction = i;
        }
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(" \n");
        instructions.forEach(i -> joiner.add(i.toString()));
        return joiner.toString();
    }

    List<Instruction> createConcatenationInstruction(int nArgs, List<String> format) {
        var newCollector = new InstructionsCollector();
        var concatCollector = new InstructionsCollector();
        var count = 0;

        for(var i = size()-1; i >= 0; i--){
            var e = instructions.get(i);
            if(count < nArgs){
                if(e.isAloadInstruction() || e.isNew()){
                    count++;
                }
                concatCollector.add(e);
            }
            else{
                newCollector.add(e);
            }
        }

        var res = new InstructionsCollector();
        Collections.reverse(concatCollector.instructions);
        Collections.reverse(newCollector.instructions);
        res.addAll(newCollector.instructions);
        res.add(new ConcatenationInstruction(nArgs, concatCollector, format));
        return res.instructions;
    }
}
