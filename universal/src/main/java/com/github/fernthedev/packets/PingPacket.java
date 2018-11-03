package com.github.fernthedev.packets;

public class PingPacket extends Packet {

    private final long time;

    public long getTime() {
        return System.nanoTime() - time;
    }

    public PingPacket() {
        time = System.nanoTime();
    }
}
