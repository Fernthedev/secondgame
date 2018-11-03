package com.github.fernthedev.exceptions;

public class DebugException extends Exception {

    public DebugException() {
        super("Don't worry! This is just for debugging. All's good.");
    }

    public DebugException(String message) {
        super("Don't worry! This is just for debugging. All's good.\n" + message);
    }

}
