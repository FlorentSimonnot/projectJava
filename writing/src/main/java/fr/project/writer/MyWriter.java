package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.Field;
import fr.project.instructions.simple.Instruction;
import fr.project.instructions.simple.Method;
import fr.project.instructions.simple.MyClass;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author CHU Jonathan
 * A class that allows to write a new .class file according to a .class file you visit.
 *
 */
public class MyWriter {
    private final MyClass myClass;
    private final ClassWriter cw;
    private final int version;
    private MethodVisitor mw;

    /**
     * Creates a new MyWriter.
     * @param myClass - the .class file you want to link your MyWriter with
     * @param version - the target version of your new .class file
     */
    public MyWriter(MyClass myClass, int version){
        this.myClass = myClass;
        this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        this.version = version;
    }

    /**
     * Creates the new .class file according to its privacy and its name.
     */
    public void createClass(){
        cw.visit(version, myClass.getPrivacy(), myClass.getClassName(), null, "java/lang/Object", myClass.getInterfaces());
    }

    /**
     * Writes all the lambdas of a .class file into a new .class file according to a target version.
     */
    public void writeLambdaInnerClasses(){
        if(version < LambdaInstruction.VERSION)
            myClass.getLambdaCollector().forEach(this::writeLambdaInnerClass);
    }

    private void writeLambdaInnerClass(LambdaInstruction lambdaInstruction, int index){
        System.out.println("LAMBDA " + index);

        var newClassVisitor = new ClassVisitor(Opcodes.ASM7){

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
            }
        };

        newClassVisitor.visit(version, Opcodes.ACC_PRIVATE+Opcodes.ACC_STATIC, "MyLambda$"+index, null, "java/lang/Object", null);
        var methodVisitor = newClassVisitor.visitMethod(
                Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC,
                "MyLambdaMethod"+index,
                "(V)", null, new String[]{});
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, myClass.getClassName(), "lambda$0", "(V)", false);
        methodVisitor.visitEnd();
        newClassVisitor.visitEnd();
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
                default: {
                    mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());
                    m.writeAllInstructions(version, mw);
                    // this code uses a maximum of one stack element and one local variable
                    mw.visitMaxs(0, 0);
                    mw.visitEnd();
                } break;
            }
        }else{
            mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions());
            mw.visitCode();
            m.writeAllInstructions(version, mw);
            mw.visitMaxs(0, 0);
            mw.visitEnd();
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

            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
            mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "("+f.getDescriptor()+")Ljava/lang/StringBuilder;", false);

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

    private void writeEqualsMethodForRecord(){

    }

}
