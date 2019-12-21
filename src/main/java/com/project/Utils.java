package com.project;
import org.objectweb.asm.Opcodes;

public class Utils {

    public static String takeOwnerFunction(String name){
        var split = name.split("L");
        if(split.length != 2){
            throw new IllegalStateException();
        }
        return split[1].replace(";", "") + " capture [" + split[0].replace("(", "").replace(")", "") + "]";
    }

    public static String getOwnerOfVarInstruction(int opcode){
        System.out.println("MY OPCODE " + opcode);
        switch(opcode){
            case Opcodes.ILOAD : return "(I)";
            case Opcodes.LLOAD : return "(J)";
            case Opcodes.DLOAD : return "(D)";
            case Opcodes.ALOAD : return "(java/lang/Object;)";
            default: throw new IllegalArgumentException();
        }
    }

}
