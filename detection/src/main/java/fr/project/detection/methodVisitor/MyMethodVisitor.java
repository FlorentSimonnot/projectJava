package fr.project.detection.methodVisitor;

import fr.project.instructions.simple.*;
import fr.project.instructions.simple.Utils;
import fr.project.detection.observers.FeatureObserver;
import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMethodVisitor extends MethodVisitor{
    private final List<FeatureObserver> observers;
    private final List<Method> methods;
    private final Method myMethod;
    private final MyClass ownerClass;
    private String lastInvoke = "";
    private final Map<Label, TryCatchBlockInstruction> tryCatchBlockList = new HashMap<>();
    private boolean inTryCatchBlock = false;
    private boolean closeCalled = false;
    private TryCatchBlock tryCatchBlock = null;

    public MyMethodVisitor(MethodVisitor methodVisitor, List<FeatureObserver> observers, List<Method> methods, Method myMethod, MyClass ownerClass, String[] exceptions) {
        super(Opcodes.ASM7, methodVisitor);
        this.observers = observers;
        this.methods = methods;
        this.myMethod = myMethod;
        this.ownerClass = ownerClass;
    }

    private void addInstruction(Instruction instruction){
        if(tryCatchBlock == null)
            myMethod.addInstruction(instruction);
        else{
            tryCatchBlock.addInstruction(instruction);
        }
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
        addInstruction(new LineNumberInstruction(line, start));
        super.visitLineNumber(line, start);
        ownerClass.setLineNumber(line);
    }

    @Override
    public void visitLabel(Label label){
         /* Add a new try catch block or create a try catch block */
        if(tryCatchBlockList.containsKey(label)){
            var insn = tryCatchBlockList.get(label);
            if(tryCatchBlock == null){
                tryCatchBlock = TryCatchBlock.createTryCatchBlock(insn);
            }else{
                tryCatchBlock.addTryCatchBlockGroup(insn);
            }
            tryCatchBlockList.remove(label);
            addInstruction(insn);
        }

        addInstruction(new LabelInstruction(label));

        /*Verify if this label doesn't close the current try catch block*/
        if(tryCatchBlock != null){
            tryCatchBlock.closeLastTryCatchBlock(label);
            if(tryCatchBlock.isClosed()){
                myMethod.addInstruction(tryCatchBlock);
                tryCatchBlock.allOfClosedTryCatchBlock().forEach(tryCatchBlockList::remove);
                tryCatchBlock = null;
            }
        }

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
        addInstruction(new JumpInstruction(opcode, label));
        super.visitJumpInsn(opcode, label);
    }

    private void getInstructionCalledBeforeConcatenation(Object... args){
        var arguments = (String) args[0];
        var listFormat = Utils.createListOfConstantForConcatenation(arguments);
        myMethod.createConcatenationInstruction(Utils.numberOfOccurrence(listFormat, "arg"), listFormat);
    }

    private void getInstructionCalledBeforeLambda(Object... args){
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
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
        //System.out.println(opcode + " " + owner + " " + name + " " + descriptor);
        if(opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE){
            if(!name.equals("addSuppressed"))
                lastInvoke = owner;
        }

        if(name.equals("close")){
            closeCalled = true;
        }

        if(name.equals("addSuppressed") && inTryCatchBlock && closeCalled) {
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
        addInstruction(new NopInstruction(opcode));
        super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        addInstruction(new FrameInstruction(type, numLocal, local, numStack, stack));
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        System.out.println(typeRef + " " + typePath + " " + typeRef);
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        System.err.println("LOCAL VARIABLE VISIT " + name + " " + descriptor + " " + signature + " " + start + " " + end + " " + index);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        //System.err.println("TRY CATCH BLOCK " + start + " " + end);

        tryCatchBlockList.put(start, new TryCatchBlockInstruction(start, end, handler, type));

        if(inTryCatchBlock){
            closeCalled = false;
        }
        else{
            inTryCatchBlock = true;
        }

    	super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
    }

    @Override
    public void visitEnd() {
        methods.add(myMethod);
        //myMethod.printInstructions();
        super.visitEnd();
    }

}
