package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.EntityPlayer;

public class SendToGetInfo extends Packet {

    private EntityPlayer keepPlayer;

    /**
     * This is used for ping pong player info. The request's answer is this.
     * @param universalPlayer The new player
     */
    public SendToGetInfo(EntityPlayer universalPlayer) {
        this.keepPlayer = universalPlayer;
    }

    public EntityPlayer getKeepPlayer() {
        return keepPlayer;
    }
}
