package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.UniversalPlayer;

public class SendPlayerInfoPacket extends Packet {

    private final UniversalPlayer playerObject;

    public SendPlayerInfoPacket(UniversalPlayer playerObject) {
        this.playerObject = playerObject;

        //new DebugException("SendPlayerInfo has been called.").printStackTrace();
    }

    public UniversalPlayer getPlayerObject() {
        return playerObject;
    }
}
