package fr.project.instructions.features;

import fr.project.instructions.simple.Instruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class CalledLambdaInstruction implements Instruction {
    private final LambdaInstruction lambdaCalled;
    /* Save the descriptor for write early*/
    private final String descriptor;
    private final int index;
    private final String className;

    public CalledLambdaInstruction(LambdaInstruction lambdaCalled, String descriptor, int index, String className){
        this.lambdaCalled = Objects.requireNonNull(lambdaCalled);
        this.descriptor = Objects.requireNonNull(descriptor);
        this.index = index;
        this.className = className;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(version < lambdaCalled.getVersion()){
            writeOldVersion(mv, lastInstruction);
            return;
        }
        writeNewVersion(mv, lastInstruction);
    }

    private void writeOldVersion(MethodVisitor mv, Instruction lastInstruction){
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className+"$MyLambda"+index, "myLambdaFunction$"+index, descriptor, false);
        //mv.visitVarInsn(Opcodes.ISTORE, 0);
    }

    private void writeNewVersion(MethodVisitor mv, Instruction lastInstruction){
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, lambdaCalled.getOwnerClass(), lambdaCalled.getName(), descriptor, true);
    }
}
