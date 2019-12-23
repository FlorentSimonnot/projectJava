package com.project;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<String> createListOfConstantForConcatenation(String format){
        var list = new ArrayList<String>();
        var word = new StringBuilder();

        for(char c : format.toCharArray()){
            if(c == '\u0001'){
                list.add(word.toString());
                list.add("arg");
                word = new StringBuilder("");
            }
            else{
                word.append(c);
            }
        }
        return list;
    }

    public static int numberOfOccurrence(List<String> format, String word){
        var count = 0;
        for(String s : format){
            if(s.equals(word)){count++;}
        }
        return count;
    }

}
