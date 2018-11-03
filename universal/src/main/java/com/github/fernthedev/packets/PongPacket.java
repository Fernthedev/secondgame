package com.github.fernthedev.packets;

public class PongPacket extends Packet {
    private final long time;

    public long getTime() {
        return System.nanoTime() - time;
    }

    public PongPacket() {
       time = System.nanoTime();
    }

}
