package com.project.visitor;

import com.project.Utils;
import com.project.simpleInstruction.*;
import com.project.featuresObserver.FeatureObserver;
import org.objectweb.asm.*;

import java.util.List;

public class MyMethodVisitor extends MethodVisitor{
    private final List<FeatureObserver> observers;
    private final List<Method> methods;
    private final Method myMethod;
    private final MyClass ownerClass;
    private String lastInvoke = "";

    MyMethodVisitor(MethodVisitor methodVisitor, List<FeatureObserver> observers, List<Method> methods, Method myMethod, MyClass ownerClass) {
        super(Opcodes.ASM7, methodVisitor);
        this.observers = observers;
        this.methods = methods;
        this.myMethod = myMethod;
        this.ownerClass = ownerClass;
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
        super.visitLabel(label);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        myMethod.addInstruction(new FieldInstruction(name, owner, opcode, descriptor));
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        //System.out.println("\tIINC INSN\t" + var + " " + increment);
        myMethod.addInstruction(new IincInstruction(var, increment));
    	super.visitIincInsn(var, increment);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        System.out.println("\tINSN ANNOTATION\t" + typeRef + " " + typePath + " " + descriptor);
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        myMethod.addInstruction(new IntInstruction(opcode, operand));
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        //System.out.println("\tJUMP JNSN\t" + opcode + " " + label);
        super.visitJumpInsn(opcode, label);
    }

    private void getInstructionCalledBeforeInvokeDynamic(Object... args){
        var arguments = (String) args[0];
        var format = arguments.replace("\u0001", " arg ");
        var split = format.split(" ");
        var nArgs = 0;

        for(String c : split){
            if(c.equals("arg")){nArgs++;}
        }

        myMethod.createConcatenationInstruction(nArgs, format);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        //System.out.println("\tINVOKE DYNAMIC INSN\t" + name + " " + descriptor + " " + bootstrapMethodHandle.getName() + " " +bootstrapMethodHandle.getDesc());
        myMethod.addInstruction(new InvokeDynamicInstruction(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
        if(bootstrapMethodHandle.getName().equals("makeConcatWithConstants")){
            observers.forEach(o -> o.onFeatureDetected(
                    "CONCATENATION at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+") : pattern " + bootstrapMethodArguments[0].toString().replace("\u0001", "%1")
                    , "concatenation"));
            getInstructionCalledBeforeInvokeDynamic(bootstrapMethodArguments);
        }

        if(bootstrapMethodHandle.getName().equals("metafactory")){
            var split = descriptor.split("/");
            var type = split[split.length-1].replace(";", "");
            var bootstrap = bootstrapMethodArguments[1].toString();
            observers.forEach(o -> o.onFeatureDetected(
                    "LAMBDA at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+ ") : lambda " + Utils.takeOwnerFunction(descriptor) + " calling " + bootstrap.split(" ")[0],
                    "lambda"));
        }
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitLdcInsn(Object value) {
        myMethod.addInstruction(new LdcInstruction(value));
        super.visitLdcInsn(value);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        //System.out.println(owner + " / " + name + " / " + descriptor + " / " + opcode);
        if(opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE){
            if(!name.equals("addSuppressed"))
                lastInvoke = owner;
        }
        if(name.equals("addSuppressed"))
            observers.forEach(o -> o.onFeatureDetected(
                    "TRY_WITH_RESOURCES at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor()  + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+") : try-with-resources on " + lastInvoke
                    , name));

        myMethod.addInstruction(new MethodInstruction(opcode, owner, name, descriptor, isInterface));
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
//        System.out.println("\tLOOKUP SWITCH INSN\t" + dflt + " " + keys + " " + labels);
        myMethod.addInstruction(new LookupSwitchInstruction(dflt, keys, labels));
    	super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
//        System.out.println("\tMULTI A NEW ARRAY INSN\t" + descriptor + " " + numDimensions);
    	myMethod.addInstruction(new MultiANewArrayInstruction(descriptor, numDimensions));
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        System.out.println("\tTABLE SWITCH INSN\t" + min + " " + max + " " + dflt);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        myMethod.addInstruction(new TypeInstruction(opcode, type));
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        myMethod.addInstruction(new VarInstruction(opcode, var));
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitInsn(int opcode) {
        if(opcode != Opcodes.ATHROW)
            myMethod.addInstruction(new NopInstruction(opcode));
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        methods.add(myMethod);
        super.visitEnd();
    }

}
