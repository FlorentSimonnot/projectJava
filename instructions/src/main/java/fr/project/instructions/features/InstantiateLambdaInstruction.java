package fr.project.instructions.features;

import fr.project.instructions.simple.Instruction;
import fr.project.instructions.simple.Utils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class InstantiateLambdaInstruction implements Instruction{
    private final LambdaInstruction lambda;
    private final int index;
    private final String className;

    public InstantiateLambdaInstruction(LambdaInstruction lambda, int index, String className){
        if(index < 0) throw new IllegalArgumentException("Index must be positive");
        this.lambda = Objects.requireNonNull(lambda);
        this.index = index;
        this.className = className;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(version < lambda.getVersion()){
            writeOldVersion(mv, lastInstruction);
            return;
        }
        writeNewVersion(mv, lastInstruction);
    }

    private void writeOldVersion(MethodVisitor mv, Instruction lastInstruction){
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, className+"$MyLambda"+index, "myLambdaFactory$"+index, "("+Utils.takeCapture(lambda.getDescriptor())+")L"+className+"$MyLambda"+index+";", false);
    }


    private void writeNewVersion(MethodVisitor mv, Instruction lastInstruction){
        mv.visitInvokeDynamicInsn(lambda.getName(), lambda.getDescriptor(), lambda.getMethodHandle(), lambda.getBootstrapMethodArguments());
    }

}
