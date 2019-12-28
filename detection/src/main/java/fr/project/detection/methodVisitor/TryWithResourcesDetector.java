package fr.project.detection.methodVisitor;

import org.objectweb.asm.Label;

import java.util.HashMap;
import java.util.Map;

class TryWithResourcesDetector {
    private final Map<Label, Label> tryCatchBlock = new HashMap<>();
    private final Map<Label, Label> catchFinallyBlock = new HashMap<>();

    private final boolean firstCloseInFinally = false;
    private final boolean secondCloseInCatchBlock = false;
    private final boolean addSuppressedInCatchBlock = false;

    void addTryCatchFinallyBlock(Label start, Label end, Label handle){
        tryCatchBlock.put(start, end);
        catchFinallyBlock.put(end, handle);
    }

    void seeClose(){

    }

    boolean isTryWithResources(){
        return firstCloseInFinally && secondCloseInCatchBlock && addSuppressedInCatchBlock;
    }

}
