package fr.project.instructions.features;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;

public class LambdaInstruction {
    public static final int VERSION = Opcodes.V1_8;
    private final String name;
    private final String ownerClass;
    private final String descriptor;
    private final Handle methodHandle;
    private final Object[] args;

    public LambdaInstruction(String name, String ownerClass, String descriptor, Handle methodHandle, Object[] args) {
        this.name = name;
        this.ownerClass = ownerClass;
        this.descriptor = descriptor;
        this.methodHandle = methodHandle;
        this.args = Arrays.copyOf(args, args.length);
    }

    @Override
    public String toString() {
        return "LAMBDA " + name + " " + ownerClass;
    }

    int getVersion(){return VERSION;}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LambdaInstruction)) return false;
        var lambda = (LambdaInstruction) obj;
        System.err.println(lambda.name.equals(name) + " / " + getReturnType().equals(lambda.getReturnType()) + " / " + haveSameArgumentsType(lambda) + " / " + ownerClass.equals(lambda.ownerClass));
        return lambda.name.equals(name) &&
                getReturnType().equals(lambda.getReturnType()) &&
                haveSameArgumentsType(lambda) &&
                ownerClass.equals(lambda.ownerClass);
    }

    private boolean haveSameArgumentsType(LambdaInstruction lambdaInstruction){
        var arguments = getArgumentsType();
        var lambdaInstructionArguments = lambdaInstruction.getArgumentsType();
        if(lambdaInstructionArguments.length != arguments.length){
            return false;
        }
        for(var i = 0; i < arguments.length; i++){
            if(!arguments[i].equals(lambdaInstructionArguments[i])) return false;
        }
        return true;
    }

    public String getName(){return name;}

    public Handle getMethodHandle(){return methodHandle;}

    public Object[] getBootstrapMethodArguments(){return args;}

    public String getOwnerClass(){return ownerClass;}

    public Type getReturnType(){
        var method = (Handle) args[1];
        return Type.getReturnType(method.getDesc());
    }

    public Type[] getArgumentsType(){
        var method = (Handle) args[1];
        return Type.getArgumentTypes(method.getDesc());
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ methodHandle.hashCode() ^ ownerClass.hashCode() ^ Arrays.hashCode(args) ^ descriptor.hashCode();
    }

    public String getDescriptor() {
        return descriptor;
    }
}
