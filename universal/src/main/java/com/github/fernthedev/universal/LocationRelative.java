package com.github.fernthedev.universal;

public class LocationRelative {
    private final Side side;
    private final int value;

    public LocationRelative(Side side,int value) {
        this.side = side;
        this.value = value;
    }

    public Side getSide() {
        return side;
    }

    public int getValue() {
        return value;
    }

    public enum Side {
        WIDTH, HEIGHT
    }
}


