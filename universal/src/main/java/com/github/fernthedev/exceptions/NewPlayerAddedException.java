package com.github.fernthedev.exceptions;

import com.github.fernthedev.universal.entity.UniversalPlayer;

public class NewPlayerAddedException extends Exception {

    private UniversalPlayer universalPlayer;

    public NewPlayerAddedException(UniversalPlayer universalPlayer) {
        this.universalPlayer = universalPlayer;
    }

    public UniversalPlayer getUniversalPlayer() {
        return universalPlayer;
    }
}
