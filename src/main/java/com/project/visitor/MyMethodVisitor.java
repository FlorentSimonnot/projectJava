package com.project.visitor;

import com.project.Utils;
import com.project.simpleInstruction.*;
import com.project.featuresObserver.FeatureObserver;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMethodVisitor extends MethodVisitor{
    private final List<FeatureObserver> observers;
    private final List<Method> methods;
    private final List<Label> labelBeginTry = new ArrayList<>();
    private final Map<Label, Label> labelEndTry = new HashMap<>();
    private final Method myMethod;
    private final MyClass ownerClass;
    private String lastInvoke = "";
    private final String[] exceptions;
    private final List<Label> jumpLabels = new ArrayList<>();

    MyMethodVisitor(MethodVisitor methodVisitor, List<FeatureObserver> observers, List<Method> methods, Method myMethod, MyClass ownerClass, String[] exceptions) {
        super(Opcodes.ASM7, methodVisitor);
        this.observers = observers;
        this.methods = methods;
        this.myMethod = myMethod;
        this.ownerClass = ownerClass;
        this.exceptions = exceptions;
    }

    private void addInstruction(Instruction instruction){
        myMethod.addInstruction(instruction);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        System.out.println("\tANNOTABLE PARAMETER COUNT\t" + parameterCount + " " + visible);
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        System.out.println("\tANNOTATION DEFAULT\t");
        return super.visitAnnotationDefault();
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        ownerClass.setLineNumber(line);
    }

    @Override
    public void visitLabel(Label label){
        if(jumpLabels.contains(label))
            myMethod.addInstruction(new LabelInstruction(label));
        super.visitLabel(label);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        addInstruction(new FieldInstruction(name, owner, opcode, descriptor));
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        //System.out.println("\tIINC INSN\t" + var + " " + increment);
        addInstruction(new IincInstruction(var, increment));
    	super.visitIincInsn(var, increment);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        System.out.println("\tINSN ANNOTATION\t" + typeRef + " " + typePath + " " + descriptor);
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        addInstruction(new IntInstruction(opcode, operand));
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        jumpLabels.add(label);
        myMethod.addInstruction(new JumpInstruction(opcode, label));
        super.visitJumpInsn(opcode, label);
    }

    private void getInstructionCalledBeforeConcatenation(Object... args){
        var arguments = (String) args[0];
        var listFormat = Utils.createListOfConstantForConcatenation(arguments);
        myMethod.createConcatenationInstruction(Utils.numberOfOccurrence(listFormat, "arg"), listFormat);
    }

    private void getInstructionCalledBeforeLambda(Object... args){
//        System.out.println("---------LALALA--------------\n");
        for(Object arg : args){
//            System.out.println(arg);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
//        System.out.println("\tINVOKE DYNAMIC INSN\t" + name + " " + descriptor + " " + bootstrapMethodHandle.getName() + " " +bootstrapMethodHandle.getDesc());
        myMethod.addInstruction(new InvokeDynamicInstruction(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
        //System.out.println("\tINVOKE DYNAMIC INSN\t" + name + " " + descriptor + " " + bootstrapMethodHandle.getName() + " " +bootstrapMethodHandle.getDesc());
        addInstruction(new InvokeDynamicInstruction(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
        if(bootstrapMethodHandle.getName().equals("makeConcatWithConstants")){
            observers.forEach(o -> o.onFeatureDetected(
                    "CONCATENATION at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+") : pattern " + bootstrapMethodArguments[0].toString().replace("\u0001", "%1")
                    , "concatenation"));
            getInstructionCalledBeforeConcatenation(bootstrapMethodArguments);
        }

        if(bootstrapMethodHandle.getName().equals("metafactory")){
            var split = descriptor.split("/");
            var type = split[split.length-1].replace(";", "");
            var bootstrap = bootstrapMethodArguments[1].toString();
            observers.forEach(o -> o.onFeatureDetected(
                    "LAMBDA at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+ ") : lambda " + Utils.takeOwnerFunction(descriptor) + " calling " + bootstrap.split(" ")[0],
                    "lambda"));
            getInstructionCalledBeforeLambda(bootstrapMethodArguments);
        }
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitLdcInsn(Object value) {
        addInstruction(new LdcInstruction(value));
        super.visitLdcInsn(value);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        //System.out.println(owner + " / " + name + " / " + descriptor + " / " + opcode);
        if(opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE){
            if(!name.equals("addSuppressed"))
                lastInvoke = owner;
        }

        if(name.equals("addSuppressed")) {
            observers.forEach(o -> o.onFeatureDetected(
                    "TRY_WITH_RESOURCES at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":" + ownerClass.getLineNumber() + ") : try-with-resources on " + lastInvoke
                    , name));
        }

        addInstruction(new MethodInstruction(opcode, owner, name, descriptor, isInterface));
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
//        System.out.println("\tLOOKUP SWITCH INSN\t" + dflt + " " + keys + " " + labels);
        addInstruction(new LookupSwitchInstruction(dflt, keys, labels));
    	super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
//        System.out.println("\tMULTI A NEW ARRAY INSN\t" + descriptor + " " + numDimensions);
    	addInstruction(new MultiANewArrayInstruction(descriptor, numDimensions));
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
//        System.out.println("\tTABLE SWITCH INSN\t" + min + " " + max + " " + dflt);
    	addInstruction(new TableSwitchInstruction(min, max, dflt, labels));
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        addInstruction(new TypeInstruction(opcode, type));
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        addInstruction(new VarInstruction(opcode, var));
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitInsn(int opcode) {
        if(opcode != Opcodes.ATHROW)
            addInstruction(new NopInstruction(opcode));
        super.visitInsn(opcode);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        System.out.println(typeRef + " " + typePath + " " + typeRef);
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        //System.out.println(name + " " + descriptor + " " + signature + " " + start + " " + end + " " + index);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        labelBeginTry.add(start);
        labelEndTry.put(end, start);
        //addInstruction(new TryCatchBlock(start, end));
    	super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
    }

    @Override
    public void visitEnd() {
        methods.add(myMethod);
        myMethod.printInstructions();
        super.visitEnd();
    }

}
