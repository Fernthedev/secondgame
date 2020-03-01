package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.EntityPlayer;

public class SetCurrentPlayer extends Packet {

    private final EntityPlayer universalPlayer;

    public SetCurrentPlayer(EntityPlayer universalPlayer) {
        this.universalPlayer = universalPlayer;
       // new DebugException("SetCurrentPlayer has been called.").printStackTrace();

    }

    public EntityPlayer getUniversalPlayer() {
        return universalPlayer;
    }
}
