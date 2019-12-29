package fr.project.detection.methodVisitor;

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
 * A class that allows to visit a method of a .class file. It contains observers that can react when the visitor detects some features.
 * @author CHU Jonathan
 *
 */
public class MyMethodVisitor extends MethodVisitor{
	private final List<FeatureObserver> observers;
	private final List<Method> methods;
	private final List<Label> labelBeginTry = new ArrayList<>();
	private final Map<Label, Label> labelEndTry = new HashMap<>();
	private final Method myMethod;
	private final MyClass ownerClass;
	private String lastInvoke = "";
	private final List<Label> jumpLabels = new ArrayList<>();

	/**
	 * Creates a new MyMethodVisitor
	 * @param methodVisitor - a MethodVisitor you want to link with
	 * @param observers - a list of FeatureObserver you want to link with
	 * @param methods - a list of Method
	 * @param myMethod - an object that represents the method you are visiting
	 * @param ownerClass - the class that contains the method myMethod
	 * @param exceptions - all the exceptions thrown by the class
	 */
	public MyMethodVisitor(MethodVisitor methodVisitor, List<FeatureObserver> observers, List<Method> methods, Method myMethod, MyClass ownerClass, String[] exceptions) {
		super(Opcodes.ASM7, methodVisitor);
		this.observers = observers;
		this.methods = methods;
		this.myMethod = myMethod;
		this.ownerClass = ownerClass;
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
		super.visitAnnotableParameterCount(parameterCount, visible);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return super.visitAnnotationDefault();
	}

	@Override
	/**
	 * Sets the line number of the method you are visiting.
	 */
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
		ownerClass.setLineNumber(line);
	}

	@Override
	/**
	 * Adds a LabelInstruction into the field instructions of the field myMethod.
	 */
	public void visitLabel(Label label){
		if(jumpLabels.contains(label))
			addInstruction(new LabelInstruction(label));
		super.visitLabel(label);
	}

	@Override
	/**
	 * Adds a FieldInstruction into the field instructions of the field myMethod.
	 */
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		addInstruction(new FieldInstruction(name, owner, opcode, descriptor));
		super.visitFieldInsn(opcode, owner, name, descriptor);
	}

	@Override
	/**
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
	 * Adds a IntInstruction into the field instructions of the field myMethod.
	 */
	public void visitIntInsn(int opcode, int operand) {
		addInstruction(new IntInstruction(opcode, operand));
		super.visitIntInsn(opcode, operand);
	}

	@Override
	/**
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
	 * Adds a InvokeDynamicInstruction into the field instructions of the field myMethod.
	 * Makes the observers react when you detect a concatenation or a lambda feature.
	 */
	public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
		addInstruction(new InvokeDynamicInstruction(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
		if(bootstrapMethodHandle.getName().equals("makeConcatWithConstants")){
			observers.forEach(o -> o.onFeatureDetected(
					"CONCATENATION at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+") : pattern " + bootstrapMethodArguments[0].toString().replace("\u0001", "%1")
					, "concatenation"));
			getInstructionCalledBeforeConcatenation(bootstrapMethodArguments);
		}

		if(bootstrapMethodHandle.getName().equals("metafactory")){
			var bootstrap = bootstrapMethodArguments[1].toString();
			observers.forEach(o -> o.onFeatureDetected(
					"LAMBDA at " + ownerClass.getClassName() + "." + myMethod.getName() + myMethod.getDescriptor() + " (" + ownerClass.getSourceName() + ":"+ownerClass.getLineNumber()+ ") : lambda " + Utils.takeOwnerFunction(descriptor) + " calling " + bootstrap.split(" ")[0],
					"lambda"));
			getInstructionCalledBeforeLambda(bootstrapMethodArguments);
		}
		super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
	}

	@Override
	/**
	 * Adds a LdcInstruction into the field instructions of the field myMethod.
	 */
	public void visitLdcInsn(Object value) {
		addInstruction(new LdcInstruction(value));
		super.visitLdcInsn(value);
	}

	@Override
	/**
	 * Adds a MethodInstruction into the field instructions of the field myMethod.
	 * Makes the observers react when you detect a try-with-resources feature and updates the method that calls the addSuppressed.
	 * 
	 */
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
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
	/**
	 * Adds a LookupSwitchInstruction into the field instructions of the field myMethod.
	 */
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		addInstruction(new LookupSwitchInstruction(dflt, keys, labels));
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	/**
	 * Adds a MultiANewArrayInstruction into the field instructions of the field myMethod.
	 */
	public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
		addInstruction(new MultiANewArrayInstruction(descriptor, numDimensions));
		super.visitMultiANewArrayInsn(descriptor, numDimensions);
	}

	@Override
	/**
	 * Adds a TableSwitchInstruction into the field instructions of the field myMethod.
	 */
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		//        System.out.println("\tTABLE SWITCH INSN\t" + min + " " + max + " " + dflt);
		addInstruction(new TableSwitchInstruction(min, max, dflt, labels));
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	/**
	 * Adds a TypeInstruction into the field instructions of the field myMethod.
	 */
	public void visitTypeInsn(int opcode, String type) {
		addInstruction(new TypeInstruction(opcode, type));
		super.visitTypeInsn(opcode, type);
	}

	@Override
	/**
	 * Adds a VarInstruction into the field instructions of the field myMethod.
	 */
	public void visitVarInsn(int opcode, int var) {
		addInstruction(new VarInstruction(opcode, var));
		super.visitVarInsn(opcode, var);
	}

	@Override
	/**
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
	 * Sets the start and the end of a try-catch block when you detect it.
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
	 * Displays all the instructions of the method after visiting it.
	 */
	public void visitEnd() {
		methods.add(myMethod);
		myMethod.printInstructions();
		super.visitEnd();
	}

}