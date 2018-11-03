package com.github.fernthedev.exceptions;

public class NonPacketException extends Exception {

    private Object packet;

    public NonPacketException(Object packet) {
        this.packet = packet;
    }

    @Override
    public void printStackTrace() {
        System.out.println(System.err + "Object: " + packet);
        super.printStackTrace();
    }

}
