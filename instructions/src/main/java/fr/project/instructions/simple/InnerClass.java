package fr.project.instructions.simple;

public class InnerClass {
    private final String name;
    private final String outerName;
    private final String innerName;
    private final int access;

    public InnerClass(String name, String outerName, String innerName, int access) {
        this.name = name;
        this.outerName = outerName;
        this.innerName = innerName;
        this.access = access;
    }
}
