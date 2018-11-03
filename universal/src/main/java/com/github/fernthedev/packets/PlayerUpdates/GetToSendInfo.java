package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.UniversalPlayer;

public class GetToSendInfo extends Packet {

    private UniversalPlayer keepPlayer;
    private UniversalPlayer newPlayer;

    /**
     * This is used for ping pong player info. This is the request.
     * @param keepPlayer
     */
    public GetToSendInfo(UniversalPlayer keepPlayer,UniversalPlayer newPlayer) {
        this.keepPlayer = keepPlayer;
        this.newPlayer = newPlayer;
    }

    public UniversalPlayer getKeepPlayer() {
        return keepPlayer;
    }

    public UniversalPlayer getNewPlayer() {
        return newPlayer;
    }
}


