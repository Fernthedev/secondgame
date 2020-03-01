package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.EntityPlayer;

public class GetToSendInfo extends Packet {

    private EntityPlayer keepPlayer;
    private EntityPlayer newPlayer;

    /**
     * This is used for ping pong player info. This is the request.
     * @param keepPlayer
     */
    public GetToSendInfo(EntityPlayer keepPlayer, EntityPlayer newPlayer) {
        this.keepPlayer = keepPlayer;
        this.newPlayer = newPlayer;
    }

    public EntityPlayer getKeepPlayer() {
        return keepPlayer;
    }

    public EntityPlayer getNewPlayer() {
        return newPlayer;
    }
}


