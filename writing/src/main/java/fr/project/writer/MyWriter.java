package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.*;
import fr.project.optionsCommand.Options;
import fr.project.warningObservers.WarningNestMemberObserver;
import fr.project.warningObservers.WarningObserver;
import fr.project.warningObservers.WarningsManager;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/* *************************************** *\

              NOTE FOR DEVELOPERS
              *******************
    Writer will be divided in a classWriter
    and RecordClassWriter in order to simplify
    methods.


\* ****************************************** */


/**
 * 
 * A class that allows to write a new .class file according to a .class file you visit.
 * @author CHU Jonathan
 *
 */
public class MyWriter {
    private final MyClass myClass;
    private final ClassWriter cw;
    private final int version;
    private MethodVisitor mw;
    private final List<WarningObserver> warningObservers;
    private final Options options;

    /**
     * Creates a new MyWriter.
     * @param myClass - the .class file you want to link your MyWriter with
     * @param version - the target version of your new .class file
     * @param warningObservers - a list of WarningObserver
     * @param options - an Options object
     */
    public MyWriter(MyClass myClass, int version, List<WarningObserver> warningObservers, Options options){
        this.myClass = myClass;
        this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        this.version = version;
        this.warningObservers = warningObservers;
        this.options = options;
    }

    /**
     * Creates the new .class file according to its privacy and its name.
     */
    public void createClass(){
        if(myClass.isRecordClass()){
            warningObservers.forEach(o -> o.onWarningDetected("[WARNING] : " + myClass.getClassName() + " is a record class", "record"));
        }
        cw.visit(version, myClass.getPrivacy(), myClass.getClassName(), null, "java/lang/Object", myClass.getInterfaces());
    }

    /**
     * Writes all the lambdas of a .class file into a new .class file according to a target version.
     */
    public void writeLambdaInnerClasses(){
        if(version < LambdaInstruction.VERSION && options.forceIsDemanding())
            myClass.getLambdaCollector().forEach(this::writeLambdaInnerClass);
        else{
            myClass.getLambdaCollector().forEach( (l, k) -> {
                warningObservers.forEach(o -> o.onWarningDetected("We have detected a lambda", "lambda"));
            });
        }
    }

    private void writeLambdaInnerClass(LambdaInstruction lambdaInstruction, int index){
        cw.visitInnerClass(myClass.getClassName()+"$MyLambda"+index, myClass.getClassName(), myClass.getClassName()+"$MyLambda"+index, Opcodes.ACC_PRIVATE+Opcodes.ACC_STATIC);
    }

    public void writeLambdaFiles(){
        if(version < LambdaInstruction.VERSION && options.forceIsDemanding())
            myClass.getLambdaCollector().forEach(this::writeLambdaFile);
    }

    private void writeSourceFile(String name){
        cw.visitSource(name, null);
    }

    private void writeNestHost(String host){
        cw.visitNestHost(host);
    }

    private void writeInnerClass(String name){
        cw.visitInnerClass(myClass.getClassName(), name, myClass.getClassName().split("\\$")[1], 10);
    }

    private void writeLambdaFile(LambdaInstruction lambdaInstruction, int index) {
        var lambdaClass = new MyClass(Opcodes.ACC_STATIC+Opcodes.ACC_PRIVATE, myClass.getClassName()+"$MyLambda"+index, "java/lang/Object", null);

        LambdaWriter.createFields(lambdaClass, lambdaInstruction);
        LambdaWriter.createConstructor(lambdaClass, lambdaInstruction);
        LambdaWriter.createLambdaFactory(lambdaClass, lambdaInstruction, myClass.getClassName(), index);
        LambdaWriter.createLambdaCalledMethod(lambdaClass, lambdaInstruction, myClass.getClassName(), index);

        var lambdaWriter = new MyWriter(lambdaClass, version, null, null);
        lambdaWriter.createClass();

        lambdaWriter.writeSourceFile(myClass.getClassName()+".java");
        lambdaWriter.writeNestHost(myClass.getClassName());
        lambdaWriter.writeInnerClass(myClass.getClassName());

        lambdaWriter.writeFields();
        lambdaWriter.writeConstructors();
        lambdaWriter.writeMethods();

        try {
            lambdaWriter.createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all fields of the current class.
     */
    public void writeFields(){
        myClass.getFields().forEach(this::writeField);
    }

    private void writeField(Field f){
        FieldVisitor fv = cw.visitField(f.getAccess(), f.getName(), f.getDescriptor(), f.getSignature(), f.getValue());
        fv.visitEnd();
    }

    /**
     * Writes all constructors of the current class.
     */
    public void writeConstructors(){
        myClass.getConstructors().forEach(this::writeMethod);
    }

    /**
     * Writes all methods of the current class.
     */
    public void writeMethods(){
        myClass.getMethods().forEach(this::writeMethod);
    }

    private void writeMethod(Method m) {
        if(myClass.isRecordClass()){
            switch(m.getName()){
                case "<init>" : writeConstructor(m); break;
                case "toString" : writeToStringMethodForRecord(m); break;
                case "equals" : writeEqualsMethodForRecord(m); break;
                case "hashCode" : writeHashCodeMethodForRecord(m); break;
                default: {
                    mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());
                    m.writeAllInstructions(version, mw);
                    // this code uses a maximum of one stack element and one local variable
                    mw.visitMaxs(0, 0);
                    mw.visitEnd();
                } break;
            }
        }else{
            if(m.getName().contains("$"))
                mw = cw.visitMethod(Opcodes.ACC_PUBLIC, m.getName(), m.getDescriptor(), null, m.getExceptions());
            else{
                mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());
            }
            mw.visitCode();
            m.writeAllInstructions(version, mw);
            mw.visitMaxs(0, 0);
            mw.visitEnd();
        }
    }

    private void writeEqualsMethodForRecord(Method m) {
        mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());
        mw.visitCode();
        mw.visitVarInsn(Opcodes.ALOAD, 1);
        mw.visitTypeInsn(Opcodes.INSTANCEOF, myClass.getClassName());
        var l1 = new Label();
        mw.visitJumpInsn(Opcodes.IFNE, l1);
        //False
        mw.visitInsn(Opcodes.ICONST_0);
        mw.visitInsn(Opcodes.IRETURN);

        mw.visitLabel(l1);
        //mw.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

        mw.visitVarInsn(Opcodes.ALOAD, 1);
        mw.visitTypeInsn(Opcodes.CHECKCAST, myClass.getClassName());
        mw.visitVarInsn(Opcodes.ASTORE, 2);
        var notEqualLabel = new Label();
        myClass.getFields().forEach(f -> {
            mw.visitVarInsn(Opcodes.ALOAD, 2);
            mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
            writeCompareNotEqual(f.getDescriptor(), mw, notEqualLabel);
        });
        var returnLabel = new Label();
        //Here, all comparisons are true
        mw.visitInsn(Opcodes.ICONST_1);
        mw.visitJumpInsn(Opcodes.GOTO, returnLabel);
        //Here we visit the label because one of fields are not equals
        mw.visitLabel(notEqualLabel);
        //mw.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"RecordClassTestEquals"}, 0, null);
        mw.visitInsn(Opcodes.ICONST_0);
        //Return the result of comparisons
        mw.visitLabel(returnLabel);
        //mw.visitFrame(Opcodes.F_SAME1, 0, new Object[]{"RecordClassTestEquals"}, 1, new Object[]{1});
        mw.visitInsn(Opcodes.IRETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    private void writeCompareNotEqual(String type, MethodVisitor mv, Label label){
        if(type.startsWith("[")){
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "equals", "("+type+type+")Z", false);
            mv.visitJumpInsn(Opcodes.IFEQ, label);
            return;
        }
        switch(type){
            case "I" :
            case "Z" :
            case "B" :
                mv.visitJumpInsn(Opcodes.IF_ICMPNE, label); break;
            case "F" : mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFNE, label); break;
            case "J" : mv.visitInsn(Opcodes.LCMP); mv.visitJumpInsn(Opcodes.IFNE, label); break;
            case "D" : mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFNE, label); break;
            default: mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.replace("L", "").replace(";", ""), "equals", "(Ljava/lang/Object;)Z", false); mv.visitJumpInsn(Opcodes.IFEQ, label); break;
        }
    }

    private void writeHashCodeMethodForRecord(Method m){
        mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());
        mw.visitCode();
        mw.visitVarInsn(Opcodes.BIPUSH, 7);
        mw.visitVarInsn(Opcodes.ISTORE, 1);
        myClass.getFields().forEach(f -> {
            mw.visitVarInsn(Opcodes.BIPUSH, 31);
            mw.visitVarInsn(Opcodes.ILOAD, 1);
            mw.visitInsn(Opcodes.IMUL);
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
            writeHashCodeInAccordingWithType(f.getDescriptor(), mw, f);
            mw.visitVarInsn(Opcodes.ISTORE, 1);
        });
        mw.visitVarInsn(Opcodes.ILOAD, 1);
        mw.visitInsn(Opcodes.IRETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    private void writeHashCodeInAccordingWithType(String type, MethodVisitor mv, Field f){
        //This is an Array
        if(type.startsWith("[")){
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "hashCode", "("+type+")I", false);
            mv.visitInsn(Opcodes.IADD);
            return;
        }
        switch (type){
            case "B" :
            case "I" : mv.visitInsn(Opcodes.IADD); break;
            case "Z" : {
                var labelBool = new Label();
                mv.visitJumpInsn(Opcodes.IFEQ, labelBool);
                mv.visitInsn(Opcodes.ICONST_0);
                var labelAddBoolHashCode = new Label();
                mv.visitJumpInsn(Opcodes.GOTO, labelAddBoolHashCode);
                mv.visitLabel(labelBool);
                //mv.visitFrame(Opcodes.F_FULL, 2,  new Object[]{"RecordClassTestEquals", 1}, 1, new Object[]{1});
                mv.visitInsn(Opcodes.ICONST_1);
                mv.visitLabel(labelAddBoolHashCode);
                //mv.visitFrame(Opcodes.F_FULL, 2,  new Object[]{"RecordClassTestEquals", 1}, 2, new Object[]{1, 1});
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            case "F" : {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "floatToIntBits", "("+type+")I", false );
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            case "J" : {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
                mv.visitVarInsn(Opcodes.BIPUSH, 32);
                mv.visitInsn(Opcodes.LUSHR);
                mv.visitInsn(Opcodes.LXOR);
                mv.visitInsn(Opcodes.L2I);
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            case "D" :{
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "doubleToLongBits", "("+type+")J", false );
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "doubleToLongBits", "("+type+")J", false );
                mv.visitVarInsn(Opcodes.BIPUSH, 32);
                mv.visitInsn(Opcodes.LUSHR);
                mv.visitInsn(Opcodes.LXOR);
                mv.visitInsn(Opcodes.L2I);
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            default: mv.visitMethodInsn(Opcodes.INVOKESTATIC, type.replace("L", "").replace(";", ""), "hashCode", "(Ljava/lang/Object;)I", false ); mv.visitInsn(Opcodes.IADD); break;
        }
    }

    /**
     * Creates a new .class file with the bytecode.
     * @return the path of the file which contain the bytecode
     * @throws IOException - if problem occurred during the creating or writing of the file.
     */
    public String createFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(myClass.getClassName()+".class");
        fos.write(cw.toByteArray());
        fos.close();
        return myClass.getClassName()+".class";
    }

    private void writeToStringMethodForRecord(Method m){
        mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());

        mw.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mw.visitInsn(Opcodes.DUP);

        mw.visitLdcInsn(myClass.getClassName()+"[");

        mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mw.visitVarInsn(Opcodes.ASTORE, 1);
        mw.visitVarInsn(Opcodes.ALOAD, 1);

        var numberOfFields = myClass.getFields().size();
        var count = 0;
        for(Field f : myClass.getFields()){
            mw.visitLdcInsn(f.getName()+"=");
            mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            if(f.getDescriptor().equals("B")){
                mw.visitInsn(Opcodes.POP);
                mw.visitInsn(Opcodes.ICONST_1);
                mw.visitIntInsn(Opcodes.NEWARRAY, 8);
                mw.visitVarInsn(Opcodes.ASTORE, 2);
                mw.visitVarInsn(Opcodes.ALOAD, 2);
                mw.visitInsn(Opcodes.ICONST_0);
                mw.visitVarInsn(Opcodes.ALOAD, 0);
                mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
                mw.visitInsn(Opcodes.BASTORE);
                mw.visitVarInsn(Opcodes.ALOAD, 1);
                mw.visitVarInsn(Opcodes.ALOAD, 2);
                mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
            }
            else if(f.getDescriptor().startsWith("[")){
                mw.visitVarInsn(Opcodes.ALOAD, 0);
                mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
                mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
            }
            else{
                mw.visitVarInsn(Opcodes.ALOAD, 0);
                mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
                mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "("+f.getDescriptor()+")Ljava/lang/StringBuilder;", false);
            }

            if(count < numberOfFields-1) {
                mw.visitLdcInsn(", ");
                mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
            count++;
        }

        mw.visitLdcInsn("]");
        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        mw.visitInsn(Opcodes.POP);
        mw.visitVarInsn(Opcodes.ALOAD, 1);

        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);

        mw.visitInsn(Opcodes.ARETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    private void writeConstructor(Method m){
        mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, null);
        m.writeAllInstructions(version, mw);
        // this code uses a maximum of one stack element and one local variable
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }


}
