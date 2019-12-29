package fr.project.instructions.features;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LambdaCollector {
    private final Map<LambdaInstruction, Integer> lambdas = new HashMap<>();

    public int addLambda(LambdaInstruction lambdaInstruction){
        lambdas.put(Objects.requireNonNull(lambdaInstruction), lambdas.size());
        return lambdas.size()-1;
    }

    public void forEach(BiConsumer<? super LambdaInstruction, ? super Integer> consumer){
        lambdas.forEach(consumer);
    }

    public boolean lambdaAlreadyExists(String name, String owner){
        for(LambdaInstruction l : lambdas.keySet()){
            if(l.getOwnerClass().equals(owner) && l.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Integer getLambdaIndex(String name, String owner){
        for(LambdaInstruction l : lambdas.keySet()){
            if(l.getOwnerClass().equals(owner) && l.getName().equals(name)){
                return lambdas.get(l);
            }
        }
        throw new IllegalStateException();
    }

    public LambdaInstruction getLambda(String name, String owner){
        for(LambdaInstruction l : lambdas.keySet()){
            if(l.getOwnerClass().equals(owner) && l.getName().equals(name)){
                return l;
            }
        }
        throw new IllegalStateException();
    }
}
