package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.EntityPlayer;

public class SendPlayerInfoPacket extends Packet {

    private final EntityPlayer playerObject;

    public SendPlayerInfoPacket(EntityPlayer playerObject) {
        this.playerObject = playerObject;

        //new DebugException("SendPlayerInfo has been called.").printStackTrace();
    }

    public EntityPlayer getPlayerObject() {
        return playerObject;
    }
}
