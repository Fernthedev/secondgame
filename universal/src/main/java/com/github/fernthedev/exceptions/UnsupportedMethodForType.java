package com.github.fernthedev.exceptions;

public class UnsupportedMethodForType extends Exception {

    private final Class thingClass;
    private final Class newClass;

    private String detailMessage;

    public Class getThingClass() {
        return thingClass;
    }

    public UnsupportedMethodForType() {
        super("An unsupported class type has been used for this method.");
        thingClass = null;
        newClass = null;
    }

    public UnsupportedMethodForType(Class foundClass,Class expectedClass) {
        super("An unsupported class type has been used for this method. Expected class " + expectedClass.getName() + " but found " + foundClass.getName());
        this.thingClass = foundClass;
        this.newClass = expectedClass;
    }
}
