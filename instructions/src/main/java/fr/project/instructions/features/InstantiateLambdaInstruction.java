package fr.project.instructions.features;

import fr.project.instructions.simple.Instruction;
import org.objectweb.asm.MethodVisitor;

import java.util.Objects;

public class InstantiateLambdaInstruction implements Instruction{
    private final LambdaInstruction lambda;
    private final int index;

    public InstantiateLambdaInstruction(LambdaInstruction lambda, int index){
        if(index < 0) throw new IllegalArgumentException("Index must be positive");
        this.lambda = Objects.requireNonNull(lambda);
        this.index = index;
    }

    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction) {
        if(version < lambda.getVersion()){
            writeOldVersion(version, mv, lastInstruction);
            return;
        }
        writeNewVersion(version, mv, lastInstruction);
    }

    private void writeOldVersion(int version, MethodVisitor mv, Instruction lastInstruction){
        
    }

    private void writeNewVersion(int version, MethodVisitor mv, Instruction lastInstruction){
        mv.visitInvokeDynamicInsn(lambda.getName(), lambda.getDescriptor(), lambda.getMethodHandle(), lambda.getBootstrapMethodArguments());
    }

}
