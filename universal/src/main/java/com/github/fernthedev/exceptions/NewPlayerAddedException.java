package com.github.fernthedev.exceptions;

import com.github.fernthedev.universal.entity.EntityPlayer;

public class NewPlayerAddedException extends Exception {

    private EntityPlayer universalPlayer;

    public NewPlayerAddedException(EntityPlayer universalPlayer) {
        this.universalPlayer = universalPlayer;
    }

    public EntityPlayer getUniversalPlayer() {
        return universalPlayer;
    }
}
