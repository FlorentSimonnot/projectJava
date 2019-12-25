package com.project.simpleInstruction;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.*;

public class Method {
    private final int access;
    private final String name;
    private final String descriptor;
    private final String signature;
    private final boolean isInterface;
    private final String[] exceptions;
    private final InstructionsCollector instructions;

    public Method(int access, String name, String descriptor, String signature, boolean isInterface, String[] exceptions) {
        this.name = name;
        this.signature = signature;
        this.descriptor = descriptor;
        this.access = access;
        this.isInterface = isInterface;
        this.exceptions = exceptions;
        this.instructions = new InstructionsCollector();
    }

    /* ********************************** *\
                    GETTERS
    \* ********************************** */

    public String getName(){return name;}

    public int getAccess(){return access;}

    public String getDescriptor() {
        return descriptor;
    }

    public String getSignature(){return signature;}

    public String[] getExceptions(){return exceptions;}

    public Instruction getLastInstruction(){return instructions.getInstruction(instructions.size()-1);}

    /* ********************************** *\
                    OTHERS
    \* ********************************** */
    public void addInstruction(Instruction instruction){
        instructions.add(instruction);
    }

    public void writeAllInstructions(int version, MethodVisitor mv){
        instructions.writeAllInstruction(version, mv);
    }

    public void createConcatenationInstruction(int nArgs, List<String> format){
        var map = instructions.createConcatenationInstruction(nArgs, format);
        instructions.clear();
        instructions.addAll(map);
    }

    public boolean collectorIsEmpty(){return instructions.size() == 0;}

    /* ********************************** *\
                    DEBUG
    \* ********************************** */
    public void printInstructions(){
        System.out.println("-----------------------METHOD " + name+"----------------------\n");
        instructions.forEach(i -> System.out.println(i.toString()));
        System.out.println("-----------------------END--------------------------\n");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Method)) return false;
        Method method = (Method) o;
        return getAccess() == method.getAccess() &&
                isInterface == method.isInterface &&
                Objects.equals(getName(), method.getName()) &&
                Objects.equals(getDescriptor(), method.getDescriptor()) &&
                Objects.equals(signature, method.signature) &&
                Arrays.equals(exceptions, method.exceptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getAccess(), getName(), getDescriptor(), signature, isInterface);
        result = 31 * result + Arrays.hashCode(exceptions);
        return result;
    }
}
