package fr.project.instructions.simple;

import fr.project.instructions.features.LambdaCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyClass {
    private final String className;
    private String sourceName;
    private final List<Field> fields;
    private final List<Method> methods;
    private final List<InnerClass> innerClasses;
    private LambdaCollector lambdaCollector = new LambdaCollector();
    private final String ownerClassName;
    private final int privacy;
    private int lineNumber;
    private final String[] interfaces;

    public MyClass(int privacy, String className, String ownerClassName, String[] interfaces){
        this.privacy = privacy;
        this.className = className;
        this.ownerClassName = ownerClassName;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.interfaces = interfaces;
    }

    /* *************************************** */
    /*                GETTERS                  */
    /* *************************************** */

    public String getClassName() {
        return className;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getConstructors(){
        var constructors = new ArrayList<Method>();
        methods.forEach(m -> {
            if(m.getName().equals("<init>")){
                constructors.add(m);
            }
        });
        return constructors;
    }

    public List<Method> getMethods(){
        var notConstructors = new ArrayList<Method>();
        methods.forEach(m -> {
            if(!m.getName().equals("<init>")){
                notConstructors.add(m);
            }
        });
        return notConstructors;
    }

    public LambdaCollector getLambdaCollector(){return lambdaCollector;}

    public List<InnerClass> getInnerClasses(){
        return new ArrayList<>(innerClasses);
    }

    public int getPrivacy() {
        return privacy;
    }

    public String[] getInterfaces(){return interfaces;}

    public void setSourceName(String sourceName){this.sourceName = sourceName;}

    public void setLineNumber(int lineNumber){this.lineNumber = lineNumber;}

    public void setLambdaCollector(LambdaCollector lambdaCollector){
        this.lambdaCollector = lambdaCollector;
    }

    public int getLineNumber(){return lineNumber;}

    public void addField(Field field){
        fields.add(field);
    }

    public List<Method> getAllMethods() {
        return methods;
    }

    public String getSourceName() {
        return sourceName;
    }

    public boolean isRecordClass(){
        return ownerClassName.equals("java/lang/Record");
    }
}
