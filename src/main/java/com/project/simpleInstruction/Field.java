package com.project.simpleInstruction;

public class Field {
    private final int access;
    private final String name;
    private final String descriptor;
    private final String signature;
    private final Object value;

    public Field(int access, String name, String descriptor, String signature, Object value){
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.value = value;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getSignature() {
        return signature;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return access + " " + name + " " + descriptor + " " + signature + " " + value;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Field))
            return false;
        var field = (Field) obj;
        return name.equals(field.name) && access == field.access && descriptor.equals(field.descriptor)
                && signature.equals(field.signature) && value == field.value;
    }
}
