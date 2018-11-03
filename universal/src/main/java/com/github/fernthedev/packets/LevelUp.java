package com.github.fernthedev.packets;

public class LevelUp extends Packet {

    private final int level;

    public LevelUp(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
