package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.UniversalPlayer;

public class SetCurrentPlayer extends Packet {

    private final UniversalPlayer universalPlayer;

    public SetCurrentPlayer(UniversalPlayer universalPlayer) {
        this.universalPlayer = universalPlayer;
       // new DebugException("SetCurrentPlayer has been called.").printStackTrace();

    }

    public UniversalPlayer getUniversalPlayer() {
        return universalPlayer;
    }
}
