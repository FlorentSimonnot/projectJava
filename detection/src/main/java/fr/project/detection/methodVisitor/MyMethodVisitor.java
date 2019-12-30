package fr.project.detection.methodVisitor;

import fr.project.instructions.features.CalledLambdaInstruction;
import fr.project.instructions.features.InstantiateLambdaInstruction;
import fr.project.instructions.features.LambdaCollector;
import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.*;
import fr.project.instructions.simple.Utils;
import fr.project.instructions.simple.*;
import fr.project.detection.observers.FeatureObserver;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A visitor to visit a method of a .class file.
 * It contains observers that can react when the visitor detects some features.
 * @author CHU Jonathan
 *
 */
public class MyMethodVisitor extends MethodVisitor{
	private final List<Label> labelBeginTry = new ArrayList<>();
	private final Map<Label, Label> labelEndTry = new HashMap<>();
	private final List<Label> jumpLabels = new ArrayList<>();
    private final List<FeatureObserver> observers;
    private final List<Method> methods;
    private final Method myMethod;
    private final MyClass ownerClass;

    private String lastInvoke = "";
    private final Map<Label, TryCatchBlockInstruction> tryCatchBlockList = new HashMap<>();
    private boolean inTryCatchBlock = false;
    private boolean closeCalled = false;
    private TryCatchBlock tryCatchBlock = null;
    private final LambdaCollector lambdaCollector;

    /**
	 * Creates a new MyMethodVisitor.
	 * @param methodVisitor - a MethodVisitor you want to link with
	 * @param observers - a list of FeatureObserver you want to link with
	 * @param methods - a list of Method
	 * @param lambdaCollector - a LambdaCollector object
	 * @param myMethod - an object that represents the method you are visiting
	 * @param ownerClass - the class that contains the method myMethod
	 * @param exceptions - all the exceptions thrown by the class
	 */
    public MyMethodVisitor(MethodVisitor methodVisitor, List<FeatureObserver> observers, List<Method> methods, LambdaCollector lambdaCollector, Method myMethod, MyClass ownerClass, String[] exceptions) {
        super(Opcodes.ASM7, methodVisitor);
        this.observers = observers;
        this.methods = methods;
        this.myMethod = myMethod;
        this.ownerClass = ownerClass;
        this.lambdaCollector = lambdaCollector;
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
		super.visitAnnotableParameterCount(parameterCount, visible);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return super.visitAnnotationDefault();
	}

	@Override
	/**
	 * Visits a line number declaration.
	 * Sets the line number of the method you are visiting.
	 */
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
		ownerClass.setLineNumber(line);
	}

	@Override
	/**
	 * Visits a label.
	 * Adds a LabelInstruction into the field instructions of the field myMethod.
	 */
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
	/**
	 * Visits a field instruction.
	 * Adds a FieldInstruction into the field instructions of the field myMethod.
	 */
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		addInstruction(new FieldInstruction(name, owner, opcode, descriptor));
		super.visitFieldInsn(opcode, owner, name, descriptor);
	}

	@Override
	/**
	 * Visits an IINC instruction.
	 * Adds a IincInstruction into the field instructions of the field myMethod.
	 */
	public void visitIincInsn(int var, int increment) {
		addInstruction(new IincInstruction(var, increment));
		super.visitIincInsn(var, increment);
	}

	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
		return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
	}

	@Override
	/**
	 * Visits an instruction with a single int operand.
	 * Adds a IntInstruction into the field instructions of the field myMethod.
	 */
	public void visitIntInsn(int opcode, int operand) {
		addInstruction(new IntInstruction(opcode, operand));
		super.visitIntInsn(opcode, operand);
	}

	@Override
	/**
	 * Visits a jump instruction.
	 * Adds a JumpInstruction into the field instructions of the field myMethod and adds Label into the field jumpLabels.
	 */
	public void visitJumpInsn(int opcode, Label label) {
		jumpLabels.add(label);
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
	/**
	 * Visits an invokedynamic instruction.
	 * Adds a InvokeDynamicInstruction into the field instructions of the field myMethod.
	 * Makes the observers react when you detect a concatenation or a lambda feature.
	 */
	public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        if(bootstrapMethodHandle.getName().equals("makeConcatWithConstants")){
            addInstruction(new InvokeDynamicInstruction(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
            observers.forEach(o -> o.onFeatureDetected(
                    "CONCATENATION at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+") : pattern " + bootstrapMethodArguments[0].toString().replace("\u0001", "%1")
                    , "concatenation"));
            getInstructionCalledBeforeConcatenation(bootstrapMethodArguments);
        }

        else if(bootstrapMethodHandle.getName().equals("metafactory")){
            var bootstrap = bootstrapMethodArguments[1].toString();
            observers.forEach(o -> o.onFeatureDetected(
                    "LAMBDA at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+ ") : lambda " + Utils.takeOwnerFunction(descriptor) + Utils.takeCapture(descriptor) + " calling " + bootstrap.split(" ")[0],
                    "lambda"));

            var myLambda = new LambdaInstruction(name, Utils.takeOwnerFunction(descriptor), descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
            var index = lambdaCollector.addLambda(myLambda);
            addInstruction(new InstantiateLambdaInstruction(myLambda, index));
        }

        else{
            addInstruction(new InvokeDynamicInstruction(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
        }

        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

	@Override
	/**
	 * Visits a LDC instruction.
	 * Adds a LdcInstruction into the field instructions of the field myMethod.
	 */
	public void visitLdcInsn(Object value) {
		addInstruction(new LdcInstruction(value));
		super.visitLdcInsn(value);
	}

	@Override
	/**
	 * Visits a method instruction.
	 * Adds a MethodInstruction into the field instructions of the field myMethod.
	 * Makes the observers react when you detect a try-with-resources feature and updates the method that calls the addSuppressed.
	 * 
	 */
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

        if(opcode == Opcodes.INVOKEINTERFACE && owner.startsWith("java/util/function")){
            if(lambdaCollector.lambdaAlreadyExists(name, owner)){
                var myLambda = lambdaCollector.getLambda(name, owner);
                addInstruction(new CalledLambdaInstruction(myLambda, descriptor));
            }
            else{
                addInstruction(new MethodInstruction(opcode, owner, name, descriptor, isInterface));
            }
        }else{
            addInstruction(new MethodInstruction(opcode, owner, name, descriptor, isInterface));
        }

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

	@Override
	/**
	 * Visits a LOOKUPSWITCH instruction
	 * Adds a LookupSwitchInstruction into the field instructions of the field myMethod.
	 */
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		addInstruction(new LookupSwitchInstruction(dflt, keys, labels));
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	/**
	 * Visits a MULTIANEWARRAY instruction.
	 * Adds a MultiANewArrayInstruction into the field instructions of the field myMethod.
	 */
	public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
		addInstruction(new MultiANewArrayInstruction(descriptor, numDimensions));
		super.visitMultiANewArrayInsn(descriptor, numDimensions);
	}

	@Override
	/**
	 * Visits a TABLESWITCH instruction.
	 * Adds a TableSwitchInstruction into the field instructions of the field myMethod.
	 */
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		addInstruction(new TableSwitchInstruction(min, max, dflt, labels));
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	/**
	 * Visits a type instruction.
	 * Adds a TypeInstruction into the field instructions of the field myMethod.
	 */
	public void visitTypeInsn(int opcode, String type) {
		addInstruction(new TypeInstruction(opcode, type));
		super.visitTypeInsn(opcode, type);
	}

	@Override
	/**
	 * Visits a local variable instruction.
	 * Adds a VarInstruction into the field instructions of the field myMethod.
	 */
	public void visitVarInsn(int opcode, int var) {
		addInstruction(new VarInstruction(opcode, var));
		super.visitVarInsn(opcode, var);
	}

	@Override
	/**
	 * Visits a zero operand instruction.
	 * Adds a NopInstruction into the field instructions of the field myMethod when the opcode is not a athrow instruction.
	 */
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
		return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
	}

	@Override
	public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, descriptor, signature, start, end, index);
	}

	@Override
	/**
	 * Visits a try catch block.
	 * Sets the start and the end of a try catch block when you detect it.
	 */
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		labelBeginTry.add(start);
		labelEndTry.put(end, start);
		super.visitTryCatchBlock(start, end, handler, type);
	}

	@Override
	public void visitParameter(String name, int access) {
		super.visitParameter(name, access);
	}

	@Override
	/**
	 * Visits the end of the method.
	 * Displays all the instructions of the method after visiting it.
	 */
	public void visitEnd() {
		methods.add(myMethod);
		myMethod.printInstructions();
		super.visitEnd();
	}

}